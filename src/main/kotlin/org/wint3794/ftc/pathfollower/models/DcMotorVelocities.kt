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
package org.wint3794.ftc.pathfollower.models

import java.util.*

/**
 * An ADT for DcMotor Velocities
 */
data class DcMotorVelocities (var velocities: DoubleArray){
    /**
     * Returns DcMotorVelocity as a double
     *
     * @param id DcMotor ID
     * @return Velocity [double]
     */
    fun getVelocity(id: Byte): Double? {
        return velocities[id.toInt()]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DcMotorVelocities

        if (!velocities.contentEquals(other.velocities)) return false

        return true
    }

    override fun hashCode(): Int {
        return velocities.contentHashCode()
    }
}