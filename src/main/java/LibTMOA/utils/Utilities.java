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
 * A class with some constants.
 */
public class Utilities {
    //private static final double bias = 0.91;
    //private static final double meccyBias = 0.9;

    /**
     * Indicates the number of places to round in powers.
     */
    public static final int roundPower = 8;

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
