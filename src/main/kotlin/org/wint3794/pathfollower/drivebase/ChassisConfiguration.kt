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
data class ChassisConfiguration(
    /**
     * Returns a List of Chassis DcMotors in current status.
     *
     * @return List of DcMotors
     */
    val motors: MutableList<DcMotorBase>,

    /**
     * Returns the current EncoderProperties.
     *
     * @return EncoderProperties.
     */
    val encoderProperties: EncoderProperties?,

    /**
     * Returns a list with current additional encoders.
     *
     * @return Additional Encoders
     */
    val additionalEncoders: List<EncoderBase>?,

    /**
     * Returns the chassis type.
     *
     * @return ChassisType.
     */
    val chassisType: ChassisTypes) {

    /**
     * Returns ExecutionMode (enum) of Follower runtime.
     *
     * @return ExecutionMode [SIMPLE, ENCODER, COMPLEX]
     */
    val mode: ExecutionModes = if (encoderProperties != null) {
        if (additionalEncoders != null) {
            ExecutionModes.COMPLEX
        } else {
            ExecutionModes.ENCODER
        }
    } else {
        ExecutionModes.SIMPLE
    }

    /**
     * Returns a desired DcMotor.
     *
     * @return DcMotorBase
     */
    fun getMotor(id: Byte): DcMotorBase? {
        return motors.find { dcMotorBase: DcMotorBase? -> dcMotorBase!!.id == id }
    }
}