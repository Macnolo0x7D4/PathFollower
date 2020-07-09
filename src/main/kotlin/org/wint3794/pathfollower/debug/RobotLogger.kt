/*
 * Copyright 2020 WinT 3794 (Manuel Diaz Rojo and Alexis Obed Garcia Hernandez)
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
package org.wint3794.pathfollower.debug

import org.wint3794.pathfollower.controllers.Robot
import org.wint3794.pathfollower.debug.telemetries.SimulatorSender
import org.wint3794.pathfollower.geometry.Pose2d

/**
 * Sends logs from Robot controller to Telemetry Driver.
 */
object RobotLogger {
    private const val ORIGIN = "Robot"

    /**
     * Sends the robot location to the debug computer.
     */
    fun sendRobotLocation() {
        Log.println(
            "X -> " + Robot.Companion.xPos,
            ORIGIN
        )
        Log.println(
            "Y -> " + Robot.Companion.yPos,
            ORIGIN
        )
        Log.println(
            "Theta -> " + Robot.Companion.worldAngle,
            ORIGIN
        )
        SimulatorSender.Companion.sendRobotLocation(
            Pose2d(
                Robot.Companion.xPos,
                Robot.Companion.yPos,
                Robot.Companion.worldAngle
            )
        )
    }

    /**
     * Sends the location of any point you would like to send.
     *
     * @param floatPoint The point you want to send.
     */
    fun sendKeyPoint(floatPoint: Pose2d) {
        Log.println(
            "Key Point -> { X: " + floatPoint.x + ", Y: " + floatPoint.y + " }",
            ORIGIN
        )
        SimulatorSender.Companion.sendKeyPoint(floatPoint)
    }

    /**
     * This is a point you don't want to clear every update.
     *
     * @param floatPoint The point you want to send.
     */
    fun sendLogPoint(floatPoint: Pose2d) {
        Log.println(
            "Log Point -> { X: " + floatPoint.x + ", Y: " + floatPoint.y + " }",
            ORIGIN
        )
        SimulatorSender.Companion.sendLogPoint(floatPoint)
    }

    /**
     * Used for debugging lines.
     *
     * @param point1 InitialPoint.
     * @param point2 TargetPoint.
     */
    fun sendLine(point1: Pose2d, point2: Pose2d) {
        Log.println(
            "New Line -> { Initial Pos: [ X: " + point1.x + ", Y: " + point1.y + " ], Target Pos: [ X: " + point2.x + ", Y: " + point2.y + " ] }",
            ORIGIN
        )
        SimulatorSender.Companion.sendLine(point1, point2)
    }
}