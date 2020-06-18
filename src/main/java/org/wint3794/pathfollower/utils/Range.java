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

package org.wint3794.pathfollower.utils;

/**
 * Utility class for performing range operations
 */
public class Range {


    // solo metodos de unidad

    private Range() {
    }

    //------------------------------------------------------------------------------------------------
    // Scaling
    //------------------------------------------------------------------------------------------------

    /**
     * Scale a number in the range of x1 to x2, to the range of y1 to y2
     *
     * @param n  number to scale
     * @param x1 lower bound range of n
     * @param x2 upper bound range of n
     * @param y1 lower bound of scale
     * @param y2 upper bound of scale
     * @return a double scaled to a value between y1 and y2, inclusive
     */
    public static double scale(double n, double x1, double x2, double y1, double y2) {
        double a = (y1 - y2) / (x1 - x2);
        double b = y1 - x1 * (y1 - y2) / (x1 - x2);
        return a * n + b;
    }


    /**
     * clip 'number' if 'number' is less than 'min' or greater than 'max'
     *
     * @param number number to test
     * @param min    minimum value allowed
     * @param max    maximum value allowed
     */
    public static double clip(double number, double min, double max) {
        if (number < min) return min;
        if (number > max) return max;
        return number;
    }

    /**
     * clip 'number' if 'number' is less than 'min' or greater than 'max'
     *
     * @param number number to test
     * @param min    minimum value allowed
     * @param max    maximum value allowed
     */
    public static float clip(float number, float min, float max) {
        if (number < min) return min;
        if (number > max) return max;
        return number;
    }

    /**
     * clip 'number' if 'number' is less than 'min' or greater than 'max'
     *
     * @param number number to test
     * @param min    minimum value allowed
     * @param max    maximum value allowed
     */
    public static int clip(int number, int min, int max) {
        if (number < min) return min;
        if (number > max) return max;
        return number;
    }

    /**
     * clip 'number' if 'number' is less than 'min' or greater than 'max'
     *
     * @param number number to test
     * @param min    minimum value allowed
     * @param max    maximum value allowed
     */
    public static short clip(short number, short min, short max) {
        if (number < min) return min;
        if (number > max) return max;
        return number;
    }

    /**
     * clip 'number' if 'number' is less than 'min' or greater than 'max'
     *
     * @param number number to test
     * @param min    minimum value allowed
     * @param max    maximum value allowed
     */
    public static byte clip(byte number, byte min, byte max) {
        if (number < min) return min;
        if (number > max) return max;
        return number;
    }

    //------------------------------------------------------------------------------------------------
    // Validation
    //------------------------------------------------------------------------------------------------

    /**
     * Throw an IllegalArgumentException if 'number' is less than 'min' or greater than 'max'
     *
     * @param number number to test
     * @param min    minimum value allowed
     * @param max    maximum value allowed
     * @throws IllegalArgumentException if number is outside of range
     */
    public static void throwIfRangeIsInvalid(double number, double min, double max) throws IllegalArgumentException {
        if (number < min || number > max) {
            throw new IllegalArgumentException(
                    String.format("number %f is invalid; valid ranges are %f..%f", number, min, max));
        }
    }

    /**
     * Throw an IllegalArgumentException if 'number' is less than 'min' or greater than 'max'
     *
     * @param number number to test
     * @param min    minimum value allowed
     * @param max    maximum value allowed
     * @throws IllegalArgumentException if number is outside of range
     */
    public static void throwIfRangeIsInvalid(int number, int min, int max) throws IllegalArgumentException {
        if (number < min || number > max) {
            throw new IllegalArgumentException(
                    String.format("number %d is invalid; valid ranges are %d..%d", number, min, max));
        }
    }
}
