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
package org.wint3794.pathfollower.drivebase.mecanum

import org.wint3794.pathfollower.drivebase.Kinematic
import org.wint3794.pathfollower.exceptions.InvalidJoystickCoordinatesException
import org.wint3794.pathfollower.exceptions.InvalidMecanumDirectiveException
import org.wint3794.pathfollower.models.DcMotorVelocities
import org.wint3794.pathfollower.models.JoystickCoordinates
import org.wint3794.pathfollower.models.MecanumDirectives
import org.wint3794.pathfollower.util.VelocityChecker

/**
 * A class with necessary methods to work with MECANUM chassis
 */
class MecanumKinematic : Kinematic {
    override fun apply() {}

    companion object {
        /**
         * Returns double[] (with DcMotor powers) if IntegrityChecker returns !null.
         *
         * @param directives MecanumDirectives
         * @return Velocities
         */
        fun move(directives: MecanumDirectives?): DcMotorVelocities {
            var directives = directives

            try {
                VelocityChecker.checkVelocity(directives)
            } catch (exception: InvalidMecanumDirectiveException) {
                exception.printStackTrace()
                directives = null
            }

            return velocitiesCreator(directives)
        }

        /**
         * Returns double[] (with DcMotor powers) if IntegrityChecker returns !null.
         *
         * @param coordinates JoystickCoordinate
         * @return Velocities
         */
        fun move(coordinates: JoystickCoordinates): DcMotorVelocities {
            try {
                VelocityChecker.checkCoordinates(coordinates)
            } catch (exception: InvalidMecanumDirectiveException) {
                exception.printStackTrace()
                return velocitiesCreator(null)
            } catch (exception: InvalidJoystickCoordinatesException) {
                exception.printStackTrace()
                return velocitiesCreator(null)
            }
            val Vd = CalculateVelocities.getSpeed(coordinates)
            val Td = CalculateVelocities.getAngle(coordinates)
            val directives = MecanumDirectives(Vd, Td)
            return velocitiesCreator(directives)
        }

        private fun velocitiesCreator(directives: MecanumDirectives?): DcMotorVelocities {
            if (directives == null) {
                val defaultVelocities = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
                return DcMotorVelocities(defaultVelocities)
            }
            val velocities = DoubleArray(4)
            val vt = directives.vt
            for (i in velocities.indices) {
                if (i == 0 || i == 3) {
                    velocities[i] = CalculatePower.calc(directives, true)
                } else {
                    velocities[i] = CalculatePower.calc(directives, false)
                }
                if (i % 2 == 0) {
                    velocities[i] = velocities[i] + vt
                } else {
                    velocities[i] = velocities[i] - vt
                }
            }
            return DcMotorVelocities(velocities)
        }
    }
}