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
import org.wint3794.pathfollower.debug.Telemetry
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
import org.wint3794.pathfollower.util.Constants
import org.wint3794.pathfollower.util.ExecutionModes
import org.wint3794.pathfollower.util.MovementVars
import java.util.*
import java.util.function.Consumer

/**
 * The main class of the library.
 */
class Follower @JvmOverloads constructor(
    private val chassisConfiguration: ChassisConfiguration,
    private val debugConfiguration: DebugConfiguration
) {
    /**
     * Returns the current chassis configuration, including objects and values.
     *
     * @return The current Chassis Configuration.
     */
    private var chassis: Chassis? = null
    private var robot: Robot? = null

    /**
     * Stops the motors, closes connections and finishes the execution of this API.
     */
    fun close() {
        chassisConfiguration.motors.forEach(Consumer { obj: DcMotorBase? -> obj!!.stop() })
        Log.println("Gracefully stopped!", ORIGIN)
        Log.update()
        Log.setDebuggingMode(false)
        Log.close()
    }

    /**
     * Returns a DcMotor object. Useful if you want to manually send instructions or get its data.
     *
     * @param id The DcMotor Identifier
     * @return Optional [DcMotorBase]
     */
    fun getDcMotor(id: Byte): DcMotorBase? {
        return chassisConfiguration.getMotor(id)
    }

    /**
     * Returns Robot Object if you are running COMPLEX mode.
     * @return Optional [Robot]
     */
    private fun getRobot(): Robot? {
        if (robot == null) {
            Log.println(
                "Robot is not available if you are not executing COMPLEX or ENCODER mode.",
                ORIGIN
            )
            return null
        }

        return robot
    }

    /**
     * Initialize Follower Engine. Method only available for COMPLEX or ENCODER mode.
     * @throws NotCompatibleConfigurationException Only if your configuration is invalid for this method.
     */
    @Throws(NotCompatibleConfigurationException::class)
    fun init(curvePoints: List<CurvePoint>) {
        Companion.curvePoints = curvePoints
        if (chassisConfiguration.mode != ExecutionModes.SIMPLE) {
            Log.println("Process initialized!", "Follower")

            when (chassisConfiguration.chassisType) {
                ChassisTypes.MECANUM -> chassis = MecanumChassis()
                else -> chassis = ClassicTankChassis()
            }

            Robot.Companion.xPos = curvePoints[0].x
            Robot.Companion.yPos = curvePoints[0].y
            Robot.Companion.worldAngle = curvePoints[0].slowDownTurnRadians
            SimulatorSender.Companion.clearLogPoints()
        } else {
            Log.println(
                "Your execution mode is not compatible with this method.",
                ORIGIN
            )
            throw NotCompatibleConfigurationException("Your execution mode is not compatible.")
        }
    }
    /**
     * Calculates and move your robot. Use in loop. Only for COMPLEX or ENCODER Mode.
     * @param followAngle The preferred turn angle. [-Math.PI - Math.PI]. The angle is in radians.
     * @throws NotCompatibleConfigurationException Only if your configuration is invalid for this method.
     */
    @JvmOverloads
    @Throws(NotCompatibleConfigurationException::class)
    fun calculate(followAngle: Double = Constants.DEFAULT_FOLLOW_ANGLE): Boolean {
        if (robot == null) {
            Log.println("Robot is null, please usea a valid configuration", ORIGIN)
            return true
        }

        if (chassisConfiguration.mode != ExecutionModes.SIMPLE) {
            if (chassis == null || curvePoints.isEmpty()) {
                Log.println(
                    "Chassis is not initialized. Please initialize chassis before executing this action.",
                    ORIGIN
                )
                Log.update()
                return true
            }

            try {
                Thread.sleep(30)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            getRobot()!!.update()

            RobotMovement.followCurve(curvePoints, followAngle)
            RobotLogger.sendRobotLocation()
            SimulatorSender.sendLogPoint(
                Point(
                    Robot.xPos,
                    Robot.yPos
                )
            )
            SimulatorSender.markEndOfUpdate()

            chassis!!.apply(Pose2d(Robot.xPos, Robot.yPos, Robot.worldAngle))

            robot!!.update()
            Log.update()

            return MovementVars.movementX == 0.0 && MovementVars.movementY == 0.0 && MovementVars.movementTurn == 0.0

        } else {
            throw NotCompatibleConfigurationException()
        }
    }

    companion object {
        private const val ORIGIN = "Main Thread"
        private var curvePoints: List<CurvePoint> = ArrayList()
    }

    /**
     * Creates an instance of the legendary PathFollower.
     *
     * @param config The Chassis Configuration that will be used in API runtime.
     */
    init {
        Log.println("The legendary PathFollower is Running!", ORIGIN)
        Log.println(
            "This API was created by Manuel Diaz and Obed Garcia from WinT 3794.",
            ORIGIN
        )
        Log.println(
            "Chassis Configuration Type is: " + chassisConfiguration.chassisType,
            ORIGIN
        )
        Log.println(
            "Chassis Configuration is set to: " + chassisConfiguration.mode,
            ORIGIN
        )

        Log.setTelemetry(debugConfiguration.telemetry)
        Log.init()

        Log.setDebuggingMode(debugConfiguration.debug)

        Log.println(
            "Debug Mode is set to: ${debugConfiguration.telemetry}",
            ORIGIN
        )

        if (debugConfiguration.debug && debugConfiguration.port != 0) {
            SimulatorSender(debugConfiguration.port, debugConfiguration.ip)

            Log.println(
                "Graphical Debugger is listening: ${debugConfiguration.ip}:${debugConfiguration.port}",
                ORIGIN
            )
        }

        if (chassisConfiguration.mode == ExecutionModes.COMPLEX || chassisConfiguration.mode == ExecutionModes.ENCODER) {
            robot = Robot(this)
            chassisConfiguration.motors
                .forEach(Consumer { motor: DcMotorBase? -> motor!!.inverted = (motor.id % 2 == 0) })
        } else {
            robot = null
        }
        
        Log.update()
    }
}