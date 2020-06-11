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

public class MathUtils{
    /**
     * Returns rounded value to established number of places (Utilities.roundPower).
     * @param value Raw value
     * @return Rounded value
     */
    public static double roundPower(double value) {
        long factor = (long) Math.pow(10, Constants.ROUND_POWER);
        value = value * factor;
        return (double) Math.round(value) / factor;
    }

    /**
     * Returns value clamped between low and high boundaries.
     *
     * @param value Value to clamp.
     * @param low   The lower boundary to which to clamp value.
     * @param high  The higher boundary to which to clamp value.
     */
    public static int clamp(int value, int low, int high) {
        return Math.max(low, Math.min(value, high));
    }

    /**
     * Returns value clamped between low and high boundaries.
     *
     * @param value Value to clamp.
     * @param low   The lower boundary to which to clamp value.
     * @param high  The higher boundary to which to clamp value.
     */
    public static double clamp(double value, double low, double high) {
        return Math.max(low, Math.min(value, high));
    }

    /**
     * Returns a Value mapped.
     *
     * @param x Value to map.
     * @param in_min The min value of entry map.
     * @param in_max The max value of entry map.
     * @param out_min The min value of output map.
     * @param out_max The max value of output map.
     * @return Value mapped.
     */
    public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
