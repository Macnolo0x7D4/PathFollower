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
package org.wint3794.pathfollower.models

/**
 * A Model for coordinates
 */
data class JoystickCoordinates
/**
 * Creates JoystickCoordinates from [y, x]
 *
 * @param y Ordinate [double]
 * @param x Abscissa [double]
 */(
    /**
     * Returns Ordinate value
     *
     * @return Ordinate [double]
     */
    val y: Double,
    /**
     * Returns Abscissa value
     *
     * @return Abscissa [double]
     */
    val x: Double
) {

    /**
     * Returns JoystickCoordinates as an array of doubles
     *
     * @return Pose2d [double[]]
     */
    @get:Deprecated("")
    val position: DoubleArray
        get() {
            val pos = DoubleArray(2)
            pos[0] = y
            pos[1] = x
            return pos
        }

    /**
     * Return JoystickCoordinates as a string
     *
     * @return JoystickCoordinates [string]
     */
    override fun toString(): String {
        return "JoystickCoordinate{" +
                "y=" + y +
                ", x=" + x +
                '}'
    }

}