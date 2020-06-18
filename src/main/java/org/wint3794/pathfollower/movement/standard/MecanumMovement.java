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

package org.wint3794.pathfollower.movement.standard;

import org.wint3794.pathfollower.math.mecanum.CalculatePower;
import org.wint3794.pathfollower.math.mecanum.CalculateVelocities;
import org.wint3794.pathfollower.models.exceptions.InvalidJoystickCoordinatesException;
import org.wint3794.pathfollower.models.exceptions.InvalidMecanumDirectiveException;
import org.wint3794.pathfollower.models.structures.DcMotorVelocities;
import org.wint3794.pathfollower.models.structures.JoystickCoordinates;
import org.wint3794.pathfollower.models.structures.MecanumDirectives;
import org.wint3794.pathfollower.movement.Movement;
import org.wint3794.pathfollower.utils.VelocityChecker;

/**
 * A class with necessary methods to work with MECANUM chassis
 */
public class MecanumMovement implements Movement {
    /**
     * Returns double[] (with DcMotor powers) if IntegrityChecker returns !null.
     *
     * @param directives MecanumDirectives
     * @return Velocities
     */
    public static DcMotorVelocities move(MecanumDirectives directives) {
        try {
            VelocityChecker.checkVelocity(directives);
        } catch (InvalidMecanumDirectiveException exception) {
            exception.printStackTrace();
            directives = null;
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
        try {
            VelocityChecker.checkCoordinates(coordinates);
        } catch (InvalidMecanumDirectiveException | InvalidJoystickCoordinatesException exception) {
            exception.printStackTrace();
            return velocitiesCreator(null);
        }

        double Vd = CalculateVelocities.getSpeed(coordinates);
        double Td = CalculateVelocities.getAngle(coordinates);

        MecanumDirectives directives = new MecanumDirectives(Vd, Td);

        return velocitiesCreator(directives);
    }

    private static DcMotorVelocities velocitiesCreator(MecanumDirectives directives) {
        if (directives == null) {
            double[] defaultVelocities = {0, 0, 0, 0};
            return new DcMotorVelocities(defaultVelocities);
        }

        double[] velocities = new double[4];

        double vt = directives.getVt();

        for (int i = 0; i < velocities.length; i++) {
            if (i == 0 || i == 3) {
                velocities[i] = CalculatePower.calc(directives, true);
            } else {
                velocities[i] = CalculatePower.calc(directives, false);
            }

            if (i % 2 == 0) {
                velocities[i] = velocities[i] + vt;
            } else {
                velocities[i] = velocities[i] - vt;
            }
        }

        return new DcMotorVelocities(velocities);
    }

    @Override
    public void apply() {

    }
}