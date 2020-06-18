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

package org.wint3794.pathfollower.utils;

import org.wint3794.pathfollower.math.mecanum.CalculateVelocities;
import org.wint3794.pathfollower.models.exceptions.InvalidJoystickCoordinatesException;
import org.wint3794.pathfollower.models.exceptions.InvalidMecanumDirectiveException;
import org.wint3794.pathfollower.models.structures.JoystickCoordinates;
import org.wint3794.pathfollower.models.structures.MecanumDirectives;

/**
 * A class with some methods to check integrity of velocities values.
 */
public class VelocityChecker {
    /**
     * Throws InvalidMecanumDirectiveException if magnitude does not pass.
     *
     * @param Vd Magnitude [0 - 1]
     * @throws InvalidMecanumDirectiveException InvalidMecanumDirectiveException
     */
    public static void checkSpeed(double Vd) throws InvalidMecanumDirectiveException {
        if (!(Vd <= 1 && Vd >= 0))
            throw new InvalidMecanumDirectiveException("Check Magnitude.");
    }

    /**
     * Throws InvalidMecanumDirectiveException if angle does not pass.
     *
     * @param Td Angle [-Math.PI - Math.PI]
     * @throws InvalidMecanumDirectiveException InvalidMecanumDirectiveException
     */
    public static void checkAngle(double Td) throws InvalidMecanumDirectiveException {
        if (!(Td <= (2 * Math.PI) && Td >= 0))
            throw new InvalidMecanumDirectiveException("Check Angle.");
    }

    /**
     * Throws InvalidMecanumDirectiveException if directives do not pass.
     *
     * @param directives MecanumDirectives
     * @throws InvalidMecanumDirectiveException InvalidMecanumDirectiveException
     */
    public static void checkVelocity(MecanumDirectives directives) throws InvalidMecanumDirectiveException {
        double vd = directives.getVd();
        double td = directives.getTd();
        double vt = directives.getVt();

        if (!(vd <= 1 && vd >= 0))
            throw new InvalidMecanumDirectiveException("Check Magnitude: " + vd);

        if (!(Math.abs(td) <= Math.PI))
            throw new InvalidMecanumDirectiveException("Check Angle: " + td);

        if (!(vt <= 1 && vt >= -1))
            throw new InvalidMecanumDirectiveException("Check Rotation: " + vt);
    }

    /**
     * Throws InvalidJoystickCoordinatesException or
     * InvalidMecanumDirectiveException if coordinates do not pass.
     *
     * @param coordinate JoystickCoordinate
     * @throws InvalidJoystickCoordinatesException InvalidJoystickCoordinatesException
     * @throws InvalidMecanumDirectiveException    InvalidMecanumDirectiveException
     */
    public static void checkCoordinates(JoystickCoordinates coordinate) throws InvalidJoystickCoordinatesException, InvalidMecanumDirectiveException {
        if (Math.abs(coordinate.getY()) > 1)
            throw new InvalidJoystickCoordinatesException("Check Ordinate Coordinate: " + coordinate.getY());

        if (Math.abs(coordinate.getX()) > 1)
            throw new InvalidJoystickCoordinatesException("Check Abscissa Coordinate: " + coordinate.getX());

        if (!(Math.abs((coordinate.getX() + coordinate.getY()) / 2) <= 1))
            throw new InvalidJoystickCoordinatesException();

        double vd = CalculateVelocities.getSpeed(coordinate);
        double td = CalculateVelocities.getAngle(coordinate);

        checkVelocity(new MecanumDirectives(vd, td, 0));
    }
}
