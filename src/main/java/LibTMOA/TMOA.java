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

package LibTMOA;

import LibTMOA.direction.StandardMovement;
import LibTMOA.math.Calculate;
import LibTMOA.models.ChassisConfiguration;
import LibTMOA.models.DcMotorBase;

import java.util.List;

public class TMOA {

    ChassisConfiguration config;

    /**
     * Creates an instance of the Trigonometric Mecanum Omnidirectional Algorithm.
     *
     * @param config ChassisConfiguration
     */
    public TMOA(ChassisConfiguration config) {
        this.config = config;
        System.out.println("[Main Thread]: The legendary Trigonometric Mecanum Omnidirectional Algorithm is Running!");
    }

    /**
     * Returns the current chassis configuration, including objects and values.
     *
     * @return ChassisConfiguration
     */
    public ChassisConfiguration getChassisInformation() {
        return this.config;
    }

    /**
     * Returns the DcMotor object. Useful if you want to manually send instructions or get its data.
     *
     * @param id The DcMotor Identifier
     * @return DcMotor Object
     */
    public DcMotorBase getDcMotor(byte id) {
        return this.config.getMotors().stream()
                .filter(dcMotor -> dcMotor.getId() == id)
                .findFirst().get();
    }

    /**
     * Indicates to the DcMotor driver the specific power to get the expected movement.
     *
     * @param Vd The multiplicative speed
     * @param Td The directional angle
     * @param Vt The change speed
     */
    public void move(double Vd, double Td, double Vt) {
        setMultiplePowers(StandardMovement.move(Vd, Td, Vt));
    }

    /**
     * Indicates to the DcMotor driver the specific power to get the expected movement.
     * Also, calculates the arc-tangent to get its length and its angle. Useful when using joysticks.
     *
     * @param y Ordinates Position
     * @param x Abscissa Position.
     */
    public void move(double y, double x) {
        setMultiplePowers(StandardMovement.move(y, x));
    }

    private void setMultiplePowers(double[] velocities) {
        List<DcMotorBase> motors = this.config.getMotors();

        for (DcMotorBase dcMotor : motors) {
            dcMotor.setPower(Calculate.roundPower(velocities[motors.indexOf(dcMotor)]));
        }
    }
}
