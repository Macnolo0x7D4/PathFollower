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
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.transform.Affine
import javafx.scene.transform.Rotate
import javafx.stage.Stage
import org.wint3794.debugger.geometry.Pose2d
import org.wint3794.debugger.net.Client
import org.wint3794.debugger.util.CommandProcessor
import org.wint3794.debugger.util.Constants
import java.io.File
import java.net.URL
import java.util.concurrent.Semaphore
import kotlin.math.cos
import kotlin.math.sin
import kotlin.system.exitProcess

private lateinit var fieldBackground: ImageView

private lateinit var field: Canvas
private lateinit var root: Group
private lateinit var layer: HBox

class App: Application() {

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        primaryStage.title = Constants.NAME

        primaryStage.onCloseRequest = EventHandler { _ -> exitProcess(0) }

        root = Group()
        val scene = Scene(root);

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

        root.children.add(layer)

        // scene.fill = Color.BLACK

        primaryStage.scene = scene
        primaryStage.width = Constants.SCREEN_SIZE
        primaryStage.height = Constants.SCREEN_SIZE
        // primaryStage.isResizable = false

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

    private fun drawRobot(graphicsContext: GraphicsContext) {
        val radius = 91.44
        val buffer = Client.commands

        val pose2d = CommandProcessor.getFrom(buffer)

        println(pose2d.x)

        val toPixel = Screen.pixel

        Screen.centerPoint = arrayOf(toPixel * Screen.dimensions[0] / 2, toPixel * Screen.dimensions[1] / 2)

        val origin = Screen.toScreen(Pose2d(0.0, Constants.FIELD_SIZE))
        fieldBackground.x = origin.x
        fieldBackground.y = origin.y

        val topLeftX = pose2d.x + (radius * (cos(pose2d.angle + Math.toRadians(45.0))));
        val topLeftY = pose2d.y + (radius * (sin(pose2d.angle + Math.toRadians(45.0))));

        Screen.toScreen(Pose2d(topLeftX, topLeftY))
        val width = 1.0 / toPixel * 18 * 2.54

        graphicsContext.save()
        graphicsContext.transform(Affine(Rotate(Math.toDegrees(-pose2d.angle) + 90, topLeftX, topLeftY)))

        val imageFile: URL? = App::class.java.getResource("/robot.png")
        val image: Image? = Image(imageFile.toString())

        graphicsContext.drawImage(image, topLeftX, topLeftY, width, width)

        graphicsContext.restore()
    }
}