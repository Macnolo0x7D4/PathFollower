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

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.stage.Stage
import org.wint3794.debugger.util.Constants
import java.io.File
import java.net.URL

private lateinit var fieldBackground: ImageView

private lateinit var field: Canvas
private lateinit var root: Group
private lateinit var layer: HBox

class App: Application() {

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        primaryStage.title = Constants.NAME

        root = Group()
        val scene = Scene(root);

        layer = HBox()
        layer.prefWidthProperty().bind(primaryStage.widthProperty())
        layer.prefHeightProperty().bind(primaryStage.heightProperty())

        val imageFile: URL? = App::class.java.getResource("/field.png")
        val image: Image? = Image(imageFile.toString())

        fieldBackground = ImageView(image)

        root.children.add(fieldBackground)

        primaryStage.scene = scene
        primaryStage.width = Constants.WIDTH.toDouble()
        primaryStage.height = Constants.HEIGHT.toDouble()
        primaryStage.isResizable = false

        primaryStage.show()
    }

    fun invokeGui(args: Array<String>) {
        launch(*args)
    }
}