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

package LibTMOA.math;

import LibTMOA.models.structures.JoystickCoordinates;
import LibTMOA.models.structures.MecanumDirectives;
import LibTMOA.utils.Utilities;
import LibTMOA.utils.VelocityChecker;

/**
 * A class with all the mathematics methods to get mecanum directions.
 */
public class Calculate {
    /**
     * Returns power value (for FR,BL).
     * @param directives MecanumDirectives
     * @return Power Value
     */
    public static double calc1(MecanumDirectives directives) {
        return Math.abs(directives.getVd()) * Math.sin(Math.abs(directives.getTd()) + (Math.PI / 4)) + directives.getVt();
    }

    /**
     * Returns power value (for FL,BR).
     * @param directives MecanumDirectives
     * @return Power Value
     */
    public static double calc2(MecanumDirectives directives) {
        return Math.abs(directives.getVd()) * Math.cos(Math.abs(directives.getTd()) + (Math.PI / 4)) + directives.getVt();
    }

    /**
     * Returns angle from coordinates.
     * @param coordinates JoystickCoordinates
     * @return Angle [0 - 2 * Math.PI]
     */
    public static double getAngle(JoystickCoordinates coordinates) {
        double a = Math.atan2(coordinates.getY(), coordinates.getX());
        return Math.abs(a);
    }

    /**
     * Returns multiplicative speed from coordinates.
     * @param coordinates JoystickCoordinates
     * @return Multiplicative Speed [0 - 1] || '0' if not pass integrity check
     */
    public static double getSpeed(JoystickCoordinates coordinates) {
        double y = Math.abs(coordinates.getY());
        double x = Math.abs(coordinates.getX());

        coordinates = new JoystickCoordinates(y, x);

        return VelocityChecker.checkCoordinates(coordinates) ? Math.max(y, x) : 0;
    }

    /**
     * Returns rounded value to established number of places (Utilities.roundPower).
     * @param value Raw value
     * @return Rounded value
     */
    public static double roundPower(double value) {
        long factor = (long) Math.pow(10, Utilities.roundPower);
        value = value * factor;
        return (double) Math.round(value) / factor;
    }
}
