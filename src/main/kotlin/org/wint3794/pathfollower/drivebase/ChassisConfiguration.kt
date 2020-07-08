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
package org.wint3794.pathfollower.drivebase

import org.wint3794.pathfollower.hardware.DcMotorBase
import org.wint3794.pathfollower.hardware.EncoderBase
import org.wint3794.pathfollower.models.EncoderProperties
import org.wint3794.pathfollower.util.ExecutionModes

/**
 * A class that must to be extended to create configurations for your Follower instance.
 */
class ChassisConfiguration {
    /**
     * Returns a List of Chassis DcMotors in current status.
     *
     * @return List of DcMotors
     */
    val motors: MutableList<DcMotorBase>

    /**
     * Returns a list with current additional encoders.
     *
     * @return Additional Encoders
     */
    val additionalEncoders: List<EncoderBase>?

    /**
     * Returns ExecutionMode (enum) of Follower runtime.
     *
     * @return ExecutionMode [SIMPLE, ENCODER, COMPLEX]
     */
    val mode: ExecutionModes
    private val encoderProperties: EncoderProperties?
    val type: ChassisTypes

    /**
     * Creates an instance of a Simple Chassis Configuration.
     *
     * @param motors A List of 4 DcMotorBase (not interface, your driver). Order: FL, FR, BL, BR.
     */
    constructor(motors: MutableList<DcMotorBase>, tank: ChassisTypes) {
        mode = ExecutionModes.SIMPLE
        encoderProperties = null
        additionalEncoders = null
        this.motors = motors
        type = tank
    }

    /**
     * Creates an instance of an Using-Encoders Chassis Configuration.
     *
     * @param motors            A List of 4 DcMotorBase (not interface, your driver). Order: LF, RF, LB, RB.
     * @param encoderProperties A EncoderProperties Object
     */
    constructor(
        motors: MutableList<DcMotorBase>,
        encoderProperties: EncoderProperties?,
        tank: ChassisTypes
    ) {
        mode = ExecutionModes.ENCODER
        this.encoderProperties = encoderProperties
        additionalEncoders = null
        this.motors = motors
        type = tank
    }

    /**
     * Creates an instance of an Using-Encoders Chassis Configuration.
     *
     * @param motors             A List of 4 DcMotorBase (not interface, your driver). Order: LF, RF, LB, RB.
     * @param encoderProperties  A EncoderProperties Object
     * @param additionalEncoders A List of Additional Encoders for Odometry.
     */
    constructor(
        motors: MutableList<DcMotorBase>,
        encoderProperties: EncoderProperties?,
        additionalEncoders: List<EncoderBase>?,
        tank: ChassisTypes
    ) {
        mode = ExecutionModes.COMPLEX
        this.encoderProperties = encoderProperties
        this.additionalEncoders = additionalEncoders
        this.motors = motors
        type = tank
    }

    /**
     * Returns a desired DcMotor.
     *
     * @return DcMotorBase
     */
    fun getMotor(id: Byte): DcMotorBase? {
        return motors.find { dcMotorBase: DcMotorBase? -> dcMotorBase!!.id == id }
    }

    /**
     * Returns the current EncoderProperties.
     *
     * @return EncoderProperties.
     */
    fun getEncoderProperties(): EncoderProperties? {
        return if (mode != ExecutionModes.SIMPLE) {
            encoderProperties
        } else null
    }

}