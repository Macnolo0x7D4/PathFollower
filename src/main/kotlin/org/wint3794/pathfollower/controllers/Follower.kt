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
package org.wint3794.pathfollower.controllers

import org.wint3794.pathfollower.debug.DebugConfiguration
import org.wint3794.pathfollower.debug.telemetries.SimulatorSender
import org.wint3794.pathfollower.debug.Log
import org.wint3794.pathfollower.debug.RobotLogger
import org.wint3794.pathfollower.drivebase.ChassisConfiguration
import org.wint3794.pathfollower.drivebase.ChassisTypes
import org.wint3794.pathfollower.drivebase.Chassis
import org.wint3794.pathfollower.drivebase.RobotMovement
import org.wint3794.pathfollower.drivebase.mecanum.MecanumChassis
import org.wint3794.pathfollower.drivebase.tank.ClassicTankChassis
import org.wint3794.pathfollower.exceptions.NotCompatibleConfigurationException
import org.wint3794.pathfollower.geometry.CurvePoint
import org.wint3794.pathfollower.geometry.Point
import org.wint3794.pathfollower.geometry.Pose2d
import org.wint3794.pathfollower.hardware.DcMotorBase
import org.wint3794.pathfollower.models.Path
import org.wint3794.pathfollower.util.Constants
import org.wint3794.pathfollower.util.ExecutionModes
import org.wint3794.pathfollower.util.MovementVars
import java.util.*
import java.util.function.Consumer

/**
 * The main class of the library.
 */
class Follower (
    private val chassisConfiguration: ChassisConfiguration,
    debugConfiguration: DebugConfiguration,
    private val path: Path
) {
    /**
     * Returns the current chassis configuration, including objects and values.
     *
     * @return The current Chassis Configuration.
     */
    private var chassis: Chassis
    private var robot: Robot

    private val origin = "Main Thread"

    /**
     * Creates an instance of the legendary PathFollower.
     */
    init {
        Log.init(debugConfiguration)

        Log.println("The legendary PathFollower is Running!", origin)
        Log.println("This API was created by Manuel Diaz and Obed Garcia from WinT 3794.", origin)

        Log.println(
            "Chassis Configuration Type is: " + chassisConfiguration.chassisType,
            origin
        )

        Log.println(
            "Chassis Configuration is set to: " + chassisConfiguration.mode,
            origin
        )

        Log.println(
            "Debug Mode is set to: ${debugConfiguration.telemetry}",
            origin
        )

        if (debugConfiguration.debug && debugConfiguration.port != 0) {
            SimulatorSender(debugConfiguration)

            Log.println(
                "Graphical Debugger is listening: ${debugConfiguration.ip}:${debugConfiguration.port}",
                origin
            )
        }

        robot = Robot(path.findFirst().x, path.findFirst().y, path.findFirst().slowDownTurnRadians)

        chassisConfiguration.motors.forEach(Consumer { motor: DcMotorBase? -> motor!!.inverted = (motor.id % 2 == 0) })

        if (chassisConfiguration.mode != ExecutionModes.SIMPLE) {
            Log.println("Process initialized!", "Follower")

            chassis = when (chassisConfiguration.chassisType) {
                ChassisTypes.MECANUM -> MecanumChassis()
                else -> ClassicTankChassis()
            }

            SimulatorSender.clearLogPoints()

        } else {
            Log.println(
                "Your execution mode is not compatible with this method.",
                origin
            )

            throw NotCompatibleConfigurationException("Your execution mode is not compatible.")
        }

        Log.update()
    }

    /**
     * Stops the motors, closes connections and finishes the execution of this API.
     */
    fun close() {
        chassisConfiguration.motors.forEach(Consumer { obj: DcMotorBase? -> obj!!.stop() })
        Log.println("Gracefully stopped!", origin)
        Log.update()
        Log.close()
    }

    /**
     * Calculates and move your robot. Use in loop. Only for COMPLEX or ENCODER Mode.
     * @param followAngle The preferred turn angle. [-Math.PI - Math.PI]. The angle is in radians.
     * @throws NotCompatibleConfigurationException Only if your configuration is invalid for this method.
     */
    @JvmOverloads
    @Throws(NotCompatibleConfigurationException::class)
    fun calculate(followAngle: Double = Constants.DEFAULT_FOLLOW_ANGLE): Boolean {
        if (chassisConfiguration.mode != ExecutionModes.SIMPLE) {
            try {
                Thread.sleep(30)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            robot.update()

            RobotMovement.followCurve(path, followAngle)
            RobotLogger.sendRobotLocation()
            SimulatorSender.sendLogPoint(
                Point(
                    Robot.xPos,
                    Robot.yPos
                )
            )
            SimulatorSender.markEndOfUpdate()

            chassis.apply(Pose2d(Robot.xPos, Robot.yPos, Robot.worldAngle))

            robot.update()
            Log.update()

            return MovementVars.movementX == 0.0 && MovementVars.movementY == 0.0 && MovementVars.movementTurn == 0.0

        } else {
            throw NotCompatibleConfigurationException()
        }
    }
}