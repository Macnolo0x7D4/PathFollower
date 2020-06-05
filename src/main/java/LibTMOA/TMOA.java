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

import LibTMOA.models.structures.DcMotorVelocities;
import LibTMOA.models.structures.JoystickCoordinates;
import LibTMOA.movement.standard.StandardMovement;
import LibTMOA.math.Calculate;
import LibTMOA.models.ChassisConfiguration;
import LibTMOA.models.DcMotorBase;

import java.util.List;

/**
 * The main class of the library.
 */
public class TMOA {

    private ChassisConfiguration config;

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
     * Returns a DcMotor object. Useful if you want to manually send instructions or get its data.
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
     * Indicates to DcMotor driver the specific power to get the expected movement.
     *
     * @param Vd The multiplicative speed [0 - 1]
     * @param Td The directional angle [0 - 2 * Math.PI]
     * @param Vt The change speed [-1 - 1]
     */
    public void move(double Vd, double Td, double Vt) {
        setMultiplePowers(StandardMovement.move(Vd, Td, Vt));
    }

    /**
     * Indicates to DcMotor driver the specific power to get the expected movement.
     * Also, calculates the arc-tangent to get its length and its angle. Useful when using joysticks.
     *
     * @param coordinates JoystickCoordinate
     */
    public void move(JoystickCoordinates coordinates) {
        setMultiplePowers(StandardMovement.move(coordinates));
    }

    private void setMultiplePowers(DcMotorVelocities velocities) {
        List<DcMotorBase> motors = this.config.getMotors();

        for (DcMotorBase dcMotor : motors) {
            dcMotor.setPower(Calculate.roundPower(
                    velocities.getVelocity((byte)motors.indexOf(dcMotor))
            ));
        }
    }
}
