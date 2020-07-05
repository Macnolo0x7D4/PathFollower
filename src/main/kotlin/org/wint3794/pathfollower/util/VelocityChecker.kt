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
import org.wint3794.pathfollower.exceptions.InvalidChassisDirectiveException
import org.wint3794.pathfollower.models.JoystickCoordinates
import org.wint3794.pathfollower.models.chassis.MecanumDirectives
import kotlin.math.PI
import kotlin.math.abs

/**
 * A class with some methods to check integrity of velocities values.
 */
object VelocityChecker {
    /**
     * Throws InvalidMecanumDirectiveException if magnitude does not pass.
     *
     * @param Vd Magnitude [0 - 1]
     * @throws InvalidChassisDirectiveException InvalidMecanumDirectiveException
     */
    @Throws(InvalidChassisDirectiveException::class)
    fun checkMagnitude(vd: Double) {
        if (vd !in 0.0..1.0) throw InvalidChassisDirectiveException("Check Magnitude.")
    }

    @Throws(InvalidChassisDirectiveException::class)
    fun checkSpeed(vt: Double) {
        if (vt !in -1.0..1.0) throw InvalidChassisDirectiveException("Check Speed.")
    }

    /**
     * Throws InvalidMecanumDirectiveException if angle does not pass.
     *
     * @param Td Angle [-Math.PI - Math.PI]
     * @throws InvalidChassisDirectiveException InvalidMecanumDirectiveException
     */
    @Throws(InvalidChassisDirectiveException::class)
    fun checkAngle(td: Double) {
        if (!(td <= 2 * PI && td >= 0)) throw InvalidChassisDirectiveException("Check Angle.")
    }

    /**
     * Throws InvalidMecanumDirectiveException if directives do not pass.
     *
     * @param directives MecanumDirectives
     * @throws InvalidChassisDirectiveException InvalidMecanumDirectiveException
     */
    @Throws(InvalidChassisDirectiveException::class)
    fun checkMecanumDirectives(directives: MecanumDirectives) {
        val vd = directives.vd
        val td = directives.td
        val vt = directives.vt

        if (vd !in 0.0..1.0) throw InvalidChassisDirectiveException("Check Magnitude: $vd")
        if (td > Math.PI) throw InvalidChassisDirectiveException("Check Angle: $td")
        if (vt !in -1.0..1.0) throw InvalidChassisDirectiveException("Check Rotation: $vt")
    }

    /**
     * Throws InvalidJoystickCoordinatesException or
     * InvalidMecanumDirectiveException if coordinates do not pass.
     *
     * @param coordinate JoystickCoordinate
     * @throws InvalidJoystickCoordinatesException InvalidJoystickCoordinatesException
     * @throws InvalidChassisDirectiveException    InvalidMecanumDirectiveException
     */
    @Throws(InvalidJoystickCoordinatesException::class, InvalidChassisDirectiveException::class)
    fun checkCoordinates(coordinate: JoystickCoordinates) {
        if (abs(coordinate.y) > 1) throw InvalidJoystickCoordinatesException("Check Ordinate Coordinate: " + coordinate.y)
        if (abs(coordinate.x) > 1) throw InvalidJoystickCoordinatesException("Check Abscissa Coordinate: " + coordinate.x)
        if (abs((coordinate.x + coordinate.y) / 2) > 1) throw InvalidJoystickCoordinatesException()

        val vd = CalculateVelocities.getSpeed(coordinate)
        val td = CalculateVelocities.getAngle(coordinate)
        checkMecanumDirectives(MecanumDirectives(vd, td, 0.0))
    }
}