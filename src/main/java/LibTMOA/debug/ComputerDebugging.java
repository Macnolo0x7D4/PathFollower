/*
 * Copyright 2020 WinT 3794 (Manuel Díaz Rojo and Alexis Obed García Hernández)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package LibTMOA.debug;

import LibTMOA.controllers.Robot;
import LibTMOA.models.structures.Pose2D;

public class ComputerDebugging {
    private static final String ORIGIN = "Robot";

    /**
     * Sends the robot location to the debug computer
     */
    public static void sendRobotLocation() {
        Log.println("X -> " + Robot.getXPos(), ORIGIN);
        Log.println("Y -> " + Robot.getYPos(), ORIGIN);
        Log.println("Theta -> " + Robot.getWorldAngle(), ORIGIN);
    }

    /**
     * Sends the location of any point you would like to send
     *
     * @param floatPoint the point you want to send
     */
    public static void sendKeyPoint(Pose2D floatPoint) {
        Log.println("Key Point -> { X: " + floatPoint.getX() + ", Y: " + floatPoint.getY() + " }", ORIGIN);
    }


    /**
     * This is a point you don't want to clear every update
     *
     * @param floatPoint the point you want to send
     */
    public static void sendLogPoint(Pose2D floatPoint) {
        Log.println("Log Point -> { X: " + floatPoint.getX() + ", Y: " + floatPoint.getY() + " }", ORIGIN);
    }


    /**
     * Used for debugging lines
     *
     * @param point1 Pose2D InitialPoint
     * @param point2 Pose2D TargetPoint
     */
    public static void sendLine(Pose2D point1, Pose2D point2) {
        Log.println("New Line -> { Initial Pos: [ X: " + point1.getX() + ", Y: " + point1.getY() + " ], Target Pos: [ X: " + point2.getX() + ", Y: " + point2.getY() + " ] }", ORIGIN);
    }
}
