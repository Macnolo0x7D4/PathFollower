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

package LibTMOA.movement.standard;

import LibTMOA.math.Calculate;
import LibTMOA.models.structures.JoystickCoordinates;
import LibTMOA.models.structures.DcMotorVelocities;
import LibTMOA.utils.VelocityChecker;

/**
 * A class with necessary methods to work with SIMPLE or ENCODER mode.
 */
public class StandardMovement {
    /**
     * Returns double[] (with DcMotor powers) if IntegrityChecker returns !null.
     *
     * @param Vd The multiplicative speed [0 - 1]
     * @param Td The directional angle [0 - 2 * Math.PI]
     * @param Vt The change speed [-1 - 1]
     * @return Velocities
     */
    public static DcMotorVelocities move(double Vd, double Td, double Vt){
        if (!(VelocityChecker.checkSpeed(Vd) && VelocityChecker.checkAngle(Td))) {
            return null;
        }

        return velocitiesCreator(Vd,Td, Vt);
    }

    /**
     * Returns double[] (with DcMotor powers) if IntegrityChecker returns !null.
     *
     * @param coordinates JoystickCoordinate
     * @return Velocities
     */
    public static DcMotorVelocities move(JoystickCoordinates coordinates) {
        if (!VelocityChecker.checkCoordinates(coordinates)) {
            return null;
        }

        double Vd = Calculate.getSpeed(coordinates);
        double Td = Calculate.getAngle(coordinates);

        if (!VelocityChecker.checkAngle(Td)) {
            return null;
        }

        return velocitiesCreator(Vd, Td, 0);
    }

    private static DcMotorVelocities velocitiesCreator(double Vd, double Td, double Vt) {
        double[] velocities = new double[4];

        velocities[0] = Calculate.calc2(Vd, Td, Vt);
        velocities[1] = Calculate.calc1(Vd, Td, Vt);
        velocities[2] = Calculate.calc1(Vd, Td, Vt);
        velocities[3] = Calculate.calc2(Vd, Td, Vt);

        return new DcMotorVelocities(velocities);
    }
}