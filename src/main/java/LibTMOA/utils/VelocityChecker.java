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

package LibTMOA.utils;

/**
 * A class with some methods to check integrity of velocities values.
 */
public class VelocityChecker {
    /**
     * Returns true if the integrity of speed its ok.
     * @param Vd Multiplicative Speed [0 - 1]
     * @return Ok? [boolean]
     */
    public static boolean checkSpeed(double Vd) {
        return Vd <= 1 && Vd >= 0;
    }

    /**
     * Returns true if the integrity of angle its ok.
     * @param Td Angle [0 - 2 * Math.PI]
     * @return Ok? [boolean]
     */
    public static boolean checkAngle(double Td) {
        return Td <= (2 * Math.PI) && Td >= 0;
    }

    /**
     * Returns true if the integrity of coordinates its ok.
     * @param y Ordinates Position [-1 - 1]
     * @param x Abscissa Position [-1 - 1]
     * @return Ok? [boolean]
     */
    public static boolean checkCoordinates(double y, double x) {
        return Math.abs((x + y) / 2) <= 1;
    }

}
