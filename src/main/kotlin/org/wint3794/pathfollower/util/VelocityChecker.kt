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
package org.wint3794.pathfollower.util

import org.wint3794.pathfollower.drivebase.mecanum.CalculateVelocities
import org.wint3794.pathfollower.exceptions.InvalidJoystickCoordinatesException
import org.wint3794.pathfollower.exceptions.InvalidMecanumDirectiveException
import org.wint3794.pathfollower.models.JoystickCoordinates
import org.wint3794.pathfollower.models.MecanumDirectives
import kotlin.math.abs

/**
 * A class with some methods to check integrity of velocities values.
 */
object VelocityChecker {
    /**
     * Throws InvalidMecanumDirectiveException if magnitude does not pass.
     *
     * @param Vd Magnitude [0 - 1]
     * @throws InvalidMecanumDirectiveException InvalidMecanumDirectiveException
     */
    @Throws(InvalidMecanumDirectiveException::class)
    fun checkSpeed(Vd: Double) {
        if (!(Vd <= 1 && Vd >= 0)) throw InvalidMecanumDirectiveException("Check Magnitude.")
    }

    /**
     * Throws InvalidMecanumDirectiveException if angle does not pass.
     *
     * @param Td Angle [-Math.PI - Math.PI]
     * @throws InvalidMecanumDirectiveException InvalidMecanumDirectiveException
     */
    @Throws(InvalidMecanumDirectiveException::class)
    fun checkAngle(Td: Double) {
        if (!(Td <= 2 * Math.PI && Td >= 0)) throw InvalidMecanumDirectiveException("Check Angle.")
    }

    /**
     * Throws InvalidMecanumDirectiveException if directives do not pass.
     *
     * @param directives MecanumDirectives
     * @throws InvalidMecanumDirectiveException InvalidMecanumDirectiveException
     */
    @Throws(InvalidMecanumDirectiveException::class)
    fun checkVelocity(directives: MecanumDirectives?) {
        val vd = directives?.vd
        val td = directives?.td
        val vt = directives?.vt
        if (vd != null && td != null && vt != null) {
            if (vd !in 0.0..1.0) throw InvalidMecanumDirectiveException("Check Magnitude: $vd")
            if (td > Math.PI) throw InvalidMecanumDirectiveException("Check Angle: $td")
            if (vt !in -1.0..1.0) throw InvalidMecanumDirectiveException("Check Rotation: $vt")
        }
    }

    /**
     * Throws InvalidJoystickCoordinatesException or
     * InvalidMecanumDirectiveException if coordinates do not pass.
     *
     * @param coordinate JoystickCoordinate
     * @throws InvalidJoystickCoordinatesException InvalidJoystickCoordinatesException
     * @throws InvalidMecanumDirectiveException    InvalidMecanumDirectiveException
     */
    @Throws(InvalidJoystickCoordinatesException::class, InvalidMecanumDirectiveException::class)
    fun checkCoordinates(coordinate: JoystickCoordinates) {
        if (Math.abs(coordinate.y) > 1) throw InvalidJoystickCoordinatesException("Check Ordinate Coordinate: " + coordinate.y)
        if (Math.abs(coordinate.x) > 1) throw InvalidJoystickCoordinatesException("Check Abscissa Coordinate: " + coordinate.x)
        if (Math.abs((coordinate.x + coordinate.y) / 2) > 1) throw InvalidJoystickCoordinatesException()
        val vd = CalculateVelocities.getSpeed(coordinate)
        val td = CalculateVelocities.getAngle(coordinate)
        checkVelocity(MecanumDirectives(vd, td, 0.0))
    }
}