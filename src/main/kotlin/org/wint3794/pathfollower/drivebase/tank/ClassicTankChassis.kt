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
package org.wint3794.pathfollower.drivebase.tank

import org.wint3794.pathfollower.drivebase.ChassisConfiguration
import org.wint3794.pathfollower.drivebase.Chassis
import org.wint3794.pathfollower.geometry.Pose2d
import org.wint3794.pathfollower.models.DcMotorVelocities
import org.wint3794.pathfollower.models.chassis.TankDirectives

/**
 * A Kinematic Class that calculates the power of DcMotors from Position values.
 */
open class ClassicTankChassis : Chassis {
    open fun move(directives: TankDirectives): DcMotorVelocities{
        return DcMotorVelocities(doubleArrayOf(- directives.inputA, directives.inputB, - directives.inputA, directives.inputB))
    }
    /**
     * Converts movement values into DcMotor Powers.
     */
    override fun apply(robotPosition: Pose2d): DcMotorVelocities {

        val rawVelocities = listOf(
            robotPosition.y - robotPosition.angle + robotPosition.x * 1.5,
            -robotPosition.y - robotPosition.angle + robotPosition.x * 1.5,
            robotPosition.y- robotPosition.angle - robotPosition.x * 1.5,
            -robotPosition.y - robotPosition.angle - robotPosition.x * 1.5
        )

        val maxPower = rawVelocities.max()

        val scaleDownAmount = if (maxPower!! > 1.0) 1.0 / maxPower else 1.0

        val velocities = rawVelocities.map { power: Double -> power * scaleDownAmount }

        return DcMotorVelocities(velocities)
    }
}