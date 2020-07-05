/*
 * Copyright 2020 WinT-3794
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.wint3794.debugger.graphics

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
import org.wint3794.debugger.geometry.Line
import org.wint3794.debugger.geometry.Point
import org.wint3794.debugger.net.Client
import org.wint3794.debugger.util.CommandProcessor
import org.wint3794.debugger.util.Constants
import java.net.URL
import java.text.DecimalFormat
import java.util.concurrent.Semaphore
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.system.exitProcess

class App: Application() {

    private lateinit var fieldBackground: ImageView
    private lateinit var field: Canvas
    private lateinit var root: Group
    private lateinit var layer: HBox

    private var point: Point = Point()
    private val format = DecimalFormat("#.00")

    private var displayPoints = arrayOfNulls<Point>(1)
    private var displayLines = arrayOfNulls<Line>(1)

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        primaryStage.title = Constants.NAME

        primaryStage.onCloseRequest = EventHandler { _ -> exitProcess(0) }

        root = Group()
        val scene = Scene(root)

        layer = HBox()
        layer.prefWidthProperty().bind(primaryStage.widthProperty())
        layer.prefHeightProperty().bind(primaryStage.heightProperty())

        val imageFile: URL? = App::class.java.getResource("/field.png")
        val image: Image? = Image(imageFile.toString())

        fieldBackground = ImageView(image)

        root.children.add(fieldBackground)

        field = Canvas(primaryStage.width, primaryStage.height)
        root.children.add(field)

        val graphics = field.graphicsContext2D

        val log = Group()

        val titleLabel = Label()
        titleLabel.font = Font("Jetbrains Mono", 16.0)
        titleLabel.textFillProperty().value = Color(1.0, 1.0, 1.0, 1.0)
        titleLabel.prefWidth = Constants.SCREEN_SIZE / 2
        titleLabel.prefHeight = Constants.SCREEN_SIZE / 10
        titleLabel.isWrapText = true

        log.children.add(titleLabel)

        layer.children.add(log)

        root.children.add(layer)

        scene.fill = Color.BLACK

        primaryStage.scene = scene
        primaryStage.width = Constants.SCREEN_SIZE
        primaryStage.height = Constants.SCREEN_SIZE + Constants.SCREEN_SIZE_OFFSET
        primaryStage.isResizable = true

        primaryStage.show()

        object: AnimationTimer() {
            private val semaphore: Semaphore = Semaphore(1)

            override fun handle(now: Long) {
                semaphore.acquire()

                Screen.dimensions = arrayOf(scene.width, scene.height)

                val scale = Screen.scale

                field.width = scale
                field.height = scale

                fieldBackground.fitWidth = scale
                fieldBackground.fitHeight = scale

                titleLabel.text = "X: " + point.x + " Y: " + point.y + " Angle: " + point.angle

                draw(graphics)

                semaphore.release()
            }
        }.start()
    }

    fun draw (graphicsContext: GraphicsContext) {
        graphicsContext.clearRect(0.0, 0.0, Screen.dimensions[0], Screen.dimensions[1])
        drawRobot(graphicsContext)
    }

    fun invokeGui(args: Array<String>) {
        launch(*args)
    }

    /*
    private fun drawDebugPoints(gc: GraphicsContext) {
        for (i in displayPoints.indices) {
            val displayLocation = Screen.toScreen(Point(displayPoints.get(i).x, displayPoints.get(i).y))
            val radius = 5.0
            gc.stroke = Color(0.0, 1.0, 1.0, 0.6)
            gc.strokeOval(displayLocation.x - radius, displayLocation.y - radius, 2 * radius, 2 * radius)
        }

        for (i in 0 until MessageProcessing.pointLog.size()) {
            val displayLocation = Screen.toScreen(Point(MessageProcessing.pointLog.get(i).x, MessageProcessing.pointLog.get(i).y))
            val radius = 5.0
            gc.fill = Color(1.0, 0.0 + i.toDouble() / MessageProcessing.pointLog.size(), 0, 0.9)
            gc.fillOval(displayLocation.x - radius, displayLocation.y - radius, 2 * radius, 2 * radius)
        }
    }

    private fun drawDebugLines(gc: GraphicsContext) {
        for (i in displayLines.indices) {
            val displayLocation1 = displayLines[i]?.x1?.let { displayLines[i]?.y1?.let { it1 -> Point(it, it1) } }?.let { Screen.toScreen(it) }
            val displayLocation2 = displayLines[i]?.x2?.let { displayLines[i]?.y2?.let { it1 -> Point(it, it1) } }?.let { Screen.toScreen(it) }

            gc.lineWidth = 3.0
            gc.stroke = Color(0.0, 1.0, 1.0, 0.6)

            if (displayLocation1 != null) {
                gc.strokeLine(displayLocation1.x, displayLocation1.y, displayLocation2.x, displayLocation2.y)
            }
        }
    }*/

    private fun drawRobot(graphicsContext: GraphicsContext) {
        val buffer = Client.commands

        point = CommandProcessor.getFrom(buffer)

        val toPixel = Screen.pixel

        Screen.centerPoint = arrayOf(toPixel * Screen.dimensions[0] / 2, toPixel * Screen.dimensions[1] / 2)

        val origin = Screen.toScreen(Point(0.0, Constants.FIELD_SIZE))
        fieldBackground.x = origin.x
        fieldBackground.y = origin.y

        val topLeftX = point.x + (Constants.ROBOT_RADIUS * (cos(point.angle + PI / 4)))
        val topLeftY = point.y + (Constants.ROBOT_RADIUS * (sin(point.angle + PI / 4)))

        Screen.toScreen(Point(topLeftX, topLeftY))
        val width = 1.0 / toPixel * Constants.ROBOT_SIZE

        graphicsContext.save()
        graphicsContext.transform(Affine(Rotate(Math.toDegrees(-point.angle) + 90, topLeftX, topLeftY)))

        val imageFile: URL? = App::class.java.getResource("/robot.png")
        val image: Image? = Image(imageFile.toString())

        graphicsContext.drawImage(image, topLeftX, topLeftY, width, width)
        graphicsContext.restore()
    }
}