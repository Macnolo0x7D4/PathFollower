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
package org.wint3794.ftc.pathfollower.drivebase.mecanum

import org.wint3794.ftc.pathfollower.models.JoystickCoordinates

/**
 * A class with all the mathematics methods to get Mecanum Velocities.
 */
object CalculateVelocities {
    /**
     * Returns angle from coordinates.
     *
     * @param coordinates JoystickCoordinates
     * @return Angle [0 - 2 * Math.PI] or '0' if not pass integrity check
     */
    fun getAngle(coordinates: JoystickCoordinates): Double {
        return Math.atan2(coordinates.y, coordinates.x)
    }

    /**
     * Returns magnitude from coordinates.
     *
     * @param coordinates JoystickCoordinates
     * @return Magnitude [0 - 1] or '0' if not pass integrity check
     */
    fun getSpeed(coordinates: JoystickCoordinates): Double {
        val y = Math.abs(coordinates.y)
        val x = Math.abs(coordinates.x)
        return Math.max(y, x)
    }
}