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

package LibTMOA.math.mecanum;

import LibTMOA.models.structures.JoystickCoordinates;

/**
 * A class with all the mathematics methods to get Mecanum Velocities.
 */
public class CalculateVelocities {
    /**
     * Returns angle from coordinates.
     * @param coordinates JoystickCoordinates
     * @return Angle [0 - 2 * Math.PI]
     */
    public static double getAngle(JoystickCoordinates coordinates) {
        return Math.atan2(coordinates.getY(), coordinates.getX()) + Math.PI;
    }

    /**
     * Returns magnitude from coordinates.
     * @param coordinates JoystickCoordinates
     * @return Magnitude [0 - 1] || '0' if not pass integrity check
     */
    public static double getSpeed(JoystickCoordinates coordinates) {
        double y = Math.abs(coordinates.getY());
        double x = Math.abs(coordinates.getX());

        return Math.max(y, x);
    }
}
