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

package org.wint3794.pathfollower.models.config;

/**
 * The Interface where you need to configure your DcMotor Driver.
 */
public interface DcMotorBase {

    boolean isMaster();

    /**
     * Returns the ID of your motor instance.
     *
     * @return ID [byte]
     */
    byte getId();

    /**
     * Returns current power of the motor.
     *
     * @return Power [double]
     */
    double getPower();

    /**
     * Sets power for your motor instance.
     *
     * @param power Power [double]
     */
    void setPower(double power);

    /**
     * Sets power 0 for motor instance.
     */
    void stop();

    /**
     * Sets inverted if inverted is true
     *
     * @param inverted Inverted [true, false]
     */
    void setInverted(boolean inverted);

    EncoderBase getEncoder();
}
