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
import org.wint3794.pathfollower.drivebase.Kinematic
import org.wint3794.pathfollower.util.MovementVars
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * A Kinematic Class that calculates the power of DcMotors from Position values.
 */
class TankKinematic
/**
 * Creates an instance of a DriveTrain Chassis.
 * @param config
 */(private val config: ChassisConfiguration) : Kinematic {

    /**
     * Converts movement values into DcMotor Powers.
     */
    override fun apply() {
        /*
        val rawVelocities: MutableList<Double> = ArrayList()
        rawVelocities.add(MovementVars.movementY - MovementVars.movementTurn + MovementVars.movementX * 1.5) // fl
        rawVelocities.add(-MovementVars.movementY - MovementVars.movementTurn + MovementVars.movementX * 1.5) //fr
        rawVelocities.add(MovementVars.movementY - MovementVars.movementTurn - MovementVars.movementX * 1.5) // rl
        rawVelocities.add(-MovementVars.movementY - MovementVars.movementTurn - MovementVars.movementX * 1.5) // rr
        val maxRawPower =
            rawVelocities.stream().max(Comparator.naturalOrder())
        val scaleDownAmount = if (maxRawPower.orElseThrow() > 1.0) 1.0 / maxRawPower.get() else 1.0
        val finalVelocities = rawVelocities.stream()
            .map<Double> { power: Double? -> power!! *= scaleDownAmount }
            .collect(Collectors.toList())
        finalVelocities.forEach(Consumer { power: Double ->
            config.getMotor(
                finalVelocities.indexOf(power).toByte()
            ).power = power
        })*/
    }
}