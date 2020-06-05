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

import LibTMOA.math.mecanum.CalculatePower;
import LibTMOA.math.mecanum.CalculateVelocities;
import LibTMOA.models.structures.JoystickCoordinates;
import LibTMOA.models.structures.DcMotorVelocities;
import LibTMOA.models.structures.MecanumDirectives;
import LibTMOA.utils.VelocityChecker;

/**
 * A class with necessary methods to work with SIMPLE or ENCODER mode.
 */
public class StandardMovement {
    /**
     * Returns double[] (with DcMotor powers) if IntegrityChecker returns !null.
     * @param directives MecanumDirectives
     * @return Velocities
     */
    public static DcMotorVelocities move(MecanumDirectives directives){
        if (!(VelocityChecker.checkSpeed(directives.getVd()) && VelocityChecker.checkAngle(directives.getTd()))) {
            return null;
        }

        return velocitiesCreator(directives);
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

        double Vd = CalculateVelocities.getSpeed(coordinates);
        double Td = CalculateVelocities.getAngle(coordinates);

        /*if (!VelocityChecker.checkAngle(Td)) {
            return null;
        }*/

        MecanumDirectives directives = new MecanumDirectives(Vd, Td);

        return velocitiesCreator(directives);
    }

    private static DcMotorVelocities velocitiesCreator(MecanumDirectives directives) {
        double[] velocities = new double[4];

        double motorA = CalculatePower.calc1(directives);
        double motorB = CalculatePower.calc2(directives);

        velocities[0] = motorB;
        velocities[1] = motorA;
        velocities[2] = motorA;
        velocities[3] = motorB;

        return new DcMotorVelocities(velocities);
    }
}