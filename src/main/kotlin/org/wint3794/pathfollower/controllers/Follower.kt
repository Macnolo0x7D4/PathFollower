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

import org.wint3794.pathfollower.debug.ComputerDebugging
import org.wint3794.pathfollower.debug.Log
import org.wint3794.pathfollower.debug.RobotLogger
import org.wint3794.pathfollower.debug.Telemetry
import org.wint3794.pathfollower.drivebase.ChassisConfiguration
import org.wint3794.pathfollower.drivebase.ChassisTypes
import org.wint3794.pathfollower.drivebase.Kinematic
import org.wint3794.pathfollower.drivebase.RobotMovement
import org.wint3794.pathfollower.drivebase.mecanum.MecanumKinematic
import org.wint3794.pathfollower.drivebase.tank.TankKinematic
import org.wint3794.pathfollower.exceptions.NotCompatibleConfigurationException
import org.wint3794.pathfollower.geometry.CurvePoint
import org.wint3794.pathfollower.geometry.Point
import org.wint3794.pathfollower.hardware.DcMotorBase
import org.wint3794.pathfollower.util.Constants
import org.wint3794.pathfollower.util.ExecutionModes
import java.util.*
import java.util.function.Consumer

/**
 * The main class of the library.
 */
class Follower @JvmOverloads constructor(
    private val chassisConfiguration: ChassisConfiguration,
    telemetry: Telemetry?,
    ip: String = "",
    port: Int = Constants.DEFAULT_CLIENT_PORT
) {
    /**
     * Returns the current chassis configuration, including objects and values.
     *
     * @return The current Chassis Configuration.
     */
    private var chassis: Kinematic? = null
    private var robot: Robot? = null

    constructor(config: ChassisConfiguration, telemetry: Telemetry?, port: Int) : this(config, telemetry, "", port) {}

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
            when (chassisConfiguration.type) {
                ChassisTypes.MECANUM -> chassis = MecanumKinematic()
                else -> chassis = TankKinematic(chassisConfiguration)
            }
            Robot.Companion.xPos = curvePoints[0].x
            Robot.Companion.yPos = curvePoints[0].y
            Robot.Companion.worldAngle = curvePoints[0].slowDownTurnRadians
            ComputerDebugging.Companion.clearLogPoints()
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
    /**
     * Calculates and move your robot. Sets the default
     * followAngle established in [org.wint3794.pathfollower.util.Constants]. Use in loop.
     * @throws NotCompatibleConfigurationException Only if your configuration is invalid for this method.
     */
    @JvmOverloads
    @Throws(NotCompatibleConfigurationException::class)
    fun calculate(followAngle: Double = Constants.DEFAULT_FOLLOW_ANGLE) {
        if (robot == null) {
            Log.println("Robot is null, please usea a valid configuration", ORIGIN)
            return
        }

        if (chassisConfiguration.mode != ExecutionModes.SIMPLE) {
            if (chassis == null || curvePoints.isEmpty()) {
                Log.println(
                    "Chassis is not initialized. Please initialize chassis before executing this action.",
                    ORIGIN
                )
                Log.update()
                return
            }
            try {
                Thread.sleep(30)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            getRobot()!!.update()

            RobotMovement.followCurve(curvePoints, followAngle)
            RobotLogger.sendRobotLocation()
            ComputerDebugging.sendLogPoint(
                Point(
                    Robot.Companion.xPos,
                    Robot.Companion.yPos
                )
            )
            ComputerDebugging.markEndOfUpdate()
            chassis!!.apply()
            robot!!.update()
            Log.update()
        } else throw NotCompatibleConfigurationException()
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
            "Chassis Configuration Type is: " + chassisConfiguration.type,
            ORIGIN
        )
        Log.println(
            "Chassis Configuration is set to: " + chassisConfiguration.mode,
            ORIGIN
        )
        if (telemetry == null) {
            Log.setDebuggingMode(false)
            Log.println(
                "The Telemetry Interface is invalid.",
                ORIGIN
            )
        } else {
            Log.setTelemetry(telemetry)
            Log.init()
            Log.setDebuggingMode(true)
            Log.println(
                "Debug Mode is set to: $telemetry",
                ORIGIN
            )
            if (port != 0) {
                if (ip == "") {
                    ComputerDebugging(port)
                } else {
                    ComputerDebugging(ip, port)
                }
                Log.println(
                    "Graphical Debugger is listening: " + if (ip == "") port else ip + port,
                    ORIGIN
                )
            }
        }
        if (chassisConfiguration.mode == ExecutionModes.COMPLEX || chassisConfiguration.mode == ExecutionModes.ENCODER) {
            robot = Robot(this)
            chassisConfiguration.motors
                .forEach(Consumer { motor: DcMotorBase? -> motor!!.setInverted(motor.id % 2 == 0) })
        } else {
            robot = null
        }
        Log.update()
    }
}