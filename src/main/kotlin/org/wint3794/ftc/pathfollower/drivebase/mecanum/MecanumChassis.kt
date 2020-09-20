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

import org.wint3794.ftc.pathfollower.drivebase.Chassis
import org.wint3794.ftc.pathfollower.exceptions.InvalidJoystickCoordinatesException
import org.wint3794.ftc.pathfollower.exceptions.InvalidChassisDirectiveException
import org.wint3794.ftc.pathfollower.geometry.Pose2d
import org.wint3794.ftc.pathfollower.models.DcMotorVelocities
import org.wint3794.ftc.pathfollower.models.JoystickCoordinates
import org.wint3794.ftc.pathfollower.models.chassis.MecanumDirectives
import org.wint3794.ftc.pathfollower.util.VelocityChecker

/**
 * A class with necessary methods to work with MECANUM chassis
 */
class MecanumChassis: Chassis {

    /**
     * Returns double[] (with DcMotor powers) if IntegrityChecker returns !null.
     *
     * @param directives MecanumDirectives
     * @return Velocities
     */
    @Throws(InvalidChassisDirectiveException::class)
    fun move(directives: MecanumDirectives): DcMotorVelocities {
        VelocityChecker.checkMecanumDirectives(directives)
        return velocitiesCreator(directives)
    }

    /**
     * Returns double[] (with DcMotor powers) if IntegrityChecker returns !null.
     *
     * @param coordinates JoystickCoordinate
     * @return Velocities
     */
    @Throws(InvalidChassisDirectiveException::class, InvalidJoystickCoordinatesException::class)
    fun move(coordinates: JoystickCoordinates): DcMotorVelocities {
        VelocityChecker.checkCoordinates(coordinates)

        val directives = MecanumDirectives(
            CalculateVelocities.getSpeed(coordinates),
            CalculateVelocities.getAngle(coordinates)
        )

        return velocitiesCreator(directives)
    }

    private fun velocitiesCreator(directives: MecanumDirectives): DcMotorVelocities {
        val velocities = DoubleArray(4)
        val vt = directives.vt

        for (i in velocities.indices) {
            if (i == 0 || i == 3) {
                velocities[i] = CalculatePower.calc(
                    directives,
                    true
                )
            } else {
                velocities[i] = CalculatePower.calc(
                    directives,
                    false
                )
            }
            if (i % 2 == 0) {
                velocities[i] = - velocities[i] + vt
            } else {
                velocities[i] = - velocities[i] - vt
            }
        }

        return DcMotorVelocities(velocities)
    }

    override fun apply(robotPosition: Pose2d): DcMotorVelocities {
        TODO("Not yet implemented")
    }
}