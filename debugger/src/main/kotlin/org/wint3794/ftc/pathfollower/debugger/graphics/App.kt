package org.wint3794.ftc.pathfollower.debugger.graphics

import org.wint3794.ftc.pathfollower.debugger.util.CommandProcessor
import org.wint3794.ftc.pathfollower.debugger.geometry.Line
import org.wint3794.ftc.pathfollower.debugger.geometry.Point
import org.wint3794.ftc.pathfollower.debugger.util.Constants
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.transform.Affine
import javafx.scene.transform.Rotate
import javafx.stage.Stage
import org.wint3794.ftc.pathfollower.debugger.Main
import java.io.FileNotFoundException
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class App : Application() {

    fun invokeGui(args: Array<String>) {
        launch(*args)
    }

    private lateinit var fieldBackground: ImageView
    private lateinit var canvas: Canvas
    private lateinit var root: Group
    private lateinit var layer: HBox

    companion object{
        var drawSemaphore = Semaphore(1)
        var displayPoints = ArrayList<Point>()
        var displayLines = ArrayList<Line>()
    }

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        primaryStage.title = "PathFollower Debugger"

        primaryStage.onCloseRequest = EventHandler { _ -> Main.close() }

        root = Group()
        val scene = Scene(root)

        layer = HBox()
        layer.prefWidthProperty().bind(primaryStage.widthProperty())
        layer.prefHeightProperty().bind(primaryStage.heightProperty())

        val image = Image(this.javaClass.getResource("/field.png").toString())
        fieldBackground = ImageView()
        fieldBackground.image = image

        root.children.add(fieldBackground)

        canvas = Canvas(primaryStage.width, primaryStage.height)

        val context = canvas.graphicsContext2D
        root.children.add(canvas)

        val log = Group()
        val logImage = Image(this.javaClass.getResource("/log.png").toString())
        val logImageView = ImageView(logImage)

        logImageView.fitHeight = logImage.height / 2.5
        logImageView.fitWidth = logImage.width / 2.5
        log.translateY = 10.0
        log.children.add(logImageView)

        val debuggingLabel = Label()

        val font = this::class.java.getResource("/fonts/JetBrainsMono-ExtraBold.ttf").openStream()
        debuggingLabel.font = Font.loadFont(font, 16.0)

        debuggingLabel.textFillProperty().value = Color(0.5, 1.0, 1.0, 1.0)

        debuggingLabel.prefWidth = logImageView.fitWidth - 25
        debuggingLabel.layoutX = 16.0
        debuggingLabel.layoutY = logImageView.fitHeight / 4.7
        debuggingLabel.isWrapText = true
        log.children.add(debuggingLabel)

        layer.children.add(log)

        root.children.add(layer)

        scene.fill = Color.BLACK

        primaryStage.scene = scene
        primaryStage.width = Constants.WINDOW_SIZE.toDouble()
        primaryStage.height = Constants.WINDOW_SIZE + 37.0

        primaryStage.show()

        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                try {
                    drawSemaphore.acquire()

                    Screen.setDimensionsPixels(
                        scene.width,
                        scene.height
                    )
                    canvas.width =
                        Screen.fieldSizePixels
                    canvas.height =
                        Screen.fieldSizePixels

                    fieldBackground.fitWidth =
                        Screen.fieldSizePixels
                    fieldBackground.fitHeight =
                        Screen.fieldSizePixels

                    debuggingLabel.maxWidth = scene.width * 0.5
                    debuggingLabel.text = "Robot Position:\nX: ${CommandProcessor.robotX.roundToInt()} , Y: ${CommandProcessor.robotY.roundToInt()}, Angle: ${Math.toDegrees(
                        CommandProcessor.robotAngle).roundToInt()}°"

                    drawScreen(context)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                drawSemaphore.release()
            }
        }.start()
    }

    private fun drawScreen(context: GraphicsContext) {
        context.clearRect(0.0, 0.0,
            Screen.widthScreen,
            Screen.heightScreen
        )
        drawRobot(context)
        drawDebugLines(context)
        drawDebugPoints(context)
    }

    private fun drawDebugPoints(context: GraphicsContext) {
        for (i in displayPoints.indices) {
            val displayLocation =
                Screen.convertToScreen(
                    Point(
                        displayPoints[i].x,
                        displayPoints[i].y
                    )
                )
            val radius = 5.0
            context.stroke = Color(0.0, 1.0, 1.0, 0.6)
            context.strokeOval(displayLocation.x - radius, displayLocation.y - radius, 2 * radius, 2 * radius)
        }

        for (i in CommandProcessor.pointLog.indices) {
            val displayLocation =
                Screen.convertToScreen(
                    Point(
                        CommandProcessor.pointLog[i].x,
                        CommandProcessor.pointLog[i].y
                    )
                )
            val radius = 5.0
            context.fill = Color(1.0, 0.0 + i.toDouble() / CommandProcessor.pointLog.size, 0.0, 0.9)
            context.fillOval(displayLocation.x - radius, displayLocation.y - radius, 2 * radius, 2 * radius)
        }
    }

    private fun drawDebugLines(gc: GraphicsContext) {
        for (i in displayLines.indices) {
            val displayLocation1 =
                Screen.convertToScreen(
                    Point(
                        displayLines[i].x1,
                        displayLines[i].y1
                    )
                )
            val displayLocation2 =
                Screen.convertToScreen(
                    Point(
                        displayLines[i].x2,
                        displayLines[i].y2
                    )
                )
            gc.lineWidth = 3.0
            gc.stroke = Color(0.0, 1.0, 1.0, 0.6)
            gc.strokeLine(displayLocation1.x, displayLocation1.y, displayLocation2.x, displayLocation2.y)
        }
    }

    private fun drawRobot(context: GraphicsContext) {
        val robotRadius = sqrt(2.0) * 18.0 * 2.54 / 2.0
        val robotX: Double = CommandProcessor.interpolatedRobotX
        val robotY: Double = CommandProcessor.interpolatedRobotY
        val robotAngle: Double = CommandProcessor.interpolatedRobotAngle

        Screen.setCenterPoint(
            Screen.centimetersPerPixel * Screen.widthScreen / 2.0,
            Screen.centimetersPerPixel * Screen.heightScreen / 2.0
        )

        val originInPixels = Screen.convertToScreen(
            Point(
                0.0,
                Constants.FIELD_SIZE
            )
        )
        fieldBackground.x = originInPixels.x
        fieldBackground.y = originInPixels.y

        val topLeftX = robotX + robotRadius * cos(robotAngle + Math.toRadians(45.0))
        val topLeftY = robotY + robotRadius * sin(robotAngle + Math.toRadians(45.0))

        try {
            val bottomLeft = Screen.convertToScreen(
                Point(topLeftX, topLeftY)
            )
            val width = 1.0 / Screen.centimetersPerPixel * 18 * 2.54

            context.save()
            context.transform(Affine(Rotate(Math.toDegrees(-robotAngle) + 90, bottomLeft.x, bottomLeft.y)))

            val image = Image(this.javaClass.getResource("/robot.png").toString())
            context.drawImage(image, bottomLeft.x, bottomLeft.y, width, width)
            context.restore()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}