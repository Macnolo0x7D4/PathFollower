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

package org.wint3794.debugger.graphics;

import org.wint3794.debugger.App;
import org.wint3794.debugger.utils.Constants;

public class Robot {
    public static void move(double x, double y){
        App.robot.setX(x * Constants.WIDTH / Constants.MAP_SQUARE - Constants.ROBOT_HEIGHT / 2 );
        App.robot.setY(y * Constants.HEIGHT / Constants.MAP_SQUARE - Constants.ROBOT_HEIGHT / 2);
    }

    public static void move(double[] pos){
        App.robot.setX(pos[0] * Constants.WIDTH / Constants.MAP_SQUARE - Constants.ROBOT_HEIGHT / 2 );
        App.robot.setY(pos[1] * Constants.HEIGHT / Constants.MAP_SQUARE - Constants.ROBOT_HEIGHT / 2);
    }
}