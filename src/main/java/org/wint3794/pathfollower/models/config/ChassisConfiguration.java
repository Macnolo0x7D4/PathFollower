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

package org.wint3794.pathfollower.models.config;

import org.wint3794.pathfollower.models.structures.EncoderProperties;

import java.util.List;

/**
 * A class that must to be extended to create configurations for your PathFollower instance.
 */
public class ChassisConfiguration {
    private final List<DcMotorBase> motors;
    private final List<EncoderBase> additionalEncoders;
    private final ExecutionModes mode;
    private final EncoderProperties encoderProperties;
    private final ChassisTypes tank;

    /**
     * Creates an instance of a Simple Chassis Configuration.
     *
     * @param motors A List of 4 DcMotorBase (not interface, your driver). Order: FL, FR, BL, BR.
     */
    public ChassisConfiguration(List<DcMotorBase> motors, ChassisTypes tank) {
        this.mode = ExecutionModes.SIMPLE;
        this.encoderProperties = null;
        this.additionalEncoders = null;
        this.motors = motors;
        this.tank = tank;
    }

    /**
     * Creates an instance of an Using-Encoders Chassis Configuration.
     *
     * @param motors            A List of 4 DcMotorBase (not interface, your driver). Order: LF, RF, LB, RB.
     * @param encoderProperties A EncoderProperties Object
     */
    public ChassisConfiguration(List<DcMotorBase> motors, EncoderProperties encoderProperties, ChassisTypes tank) {
        this.mode = ExecutionModes.ENCODER;
        this.encoderProperties = encoderProperties;
        this.additionalEncoders = null;
        this.motors = motors;
        this.tank = tank;
    }

    /**
     * Creates an instance of an Using-Encoders Chassis Configuration.
     *
     * @param motors             A List of 4 DcMotorBase (not interface, your driver). Order: LF, RF, LB, RB.
     * @param encoderProperties  A EncoderProperties Object
     * @param additionalEncoders A List of Additional Encoders for Odometry.
     */
    public ChassisConfiguration(List<DcMotorBase> motors, EncoderProperties encoderProperties, List<EncoderBase> additionalEncoders, ChassisTypes tank) {
        this.mode = ExecutionModes.COMPLEX;
        this.encoderProperties = encoderProperties;
        this.additionalEncoders = additionalEncoders;
        this.motors = motors;
        this.tank = tank;
    }

    /**
     * Returns a List of Chassis DcMotors in current status.
     *
     * @return List of DcMotors
     */
    public List<DcMotorBase> getMotors() {
        return this.motors;
    }

    /**
     * Returns a desired DcMotor.
     *
     * @return DcMotorBase
     */
    public DcMotorBase getMotor(byte id) {
        return this.motors.stream().filter(dcMotorBase -> dcMotorBase.getId() == id).findFirst().orElseThrow();
    }

    /**
     * Returns ExecutionMode (enum) of PathFollower runtime.
     *
     * @return ExecutionMode [SIMPLE, ENCODER, COMPLEX]
     */
    public ExecutionModes getMode() {
        return mode;
    }

    /**
     * Returns the current EncoderProperties.
     *
     * @return EncoderProperties.
     */
    public EncoderProperties getEncoderProperties() {
        if (mode != ExecutionModes.SIMPLE) {
            return encoderProperties;
        }

        return null;
    }

    /**
     * Returns a list with current additional encoders.
     *
     * @return Additional Encoders
     */
    public List<EncoderBase> getAdditionalEncoders() {
        return additionalEncoders;
    }

    public ChassisTypes getType() {
        return tank;
    }
}
