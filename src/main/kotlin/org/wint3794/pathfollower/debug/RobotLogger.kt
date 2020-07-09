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
        Log.println("X -> " + Robot.xPos, ORIGIN)
        Log.println("Y -> " + Robot.yPos, ORIGIN)
        Log.println("Theta -> " + Robot.worldAngle, ORIGIN)

        SimulatorSender.sendRobotLocation(
            Pose2d(
                Robot.xPos,
                Robot.yPos,
                Robot.worldAngle
            )
        )
    }
}