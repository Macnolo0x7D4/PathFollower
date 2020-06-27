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

package org.wint3794.debugger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.wint3794.debugger.driver.Client;
import org.wint3794.debugger.utils.Constants;

public class App extends Application {

    public static void main(String[] args) {
        Thread client = new Client();
        client.start();
        launch(args);
    }

    public static Rectangle robot = new Rectangle(Constants.ROBOT_WIDTH, Constants.ROBOT_HEIGHT, Color.RED);

    @Override
    public void start(Stage primaryStage) throws Exception{
        BackgroundImage image = new BackgroundImage(
                new Image(String.valueOf(this.getClass().getResource("/map.png"))),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, true, true ));

        Background background = new Background(image);

        Pane root = new Pane();
        root.setBackground(background);

        root.getChildren().add(robot);

        primaryStage.setTitle(Constants.NAME);
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        Scene scene = new Scene(root, Constants.WIDTH, Constants.HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
