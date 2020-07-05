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

import java.util.*

/**
 * An ADT for DcMotor Velocities
 */
class DcMotorVelocities {

    private var velocities: DoubleArray

    constructor(velocities: DoubleArray) {
        this.velocities = velocities
    }

    constructor(velocities: List<Double>){
        this.velocities = velocities.toDoubleArray()
    }

    /**
     * Returns DcMotorVelocity as a double
     *
     * @param id DcMotor ID
     * @return Velocity [double]
     */
    fun getVelocity(id: Byte): Double {
        return velocities[id.toInt()]
    }

    /**
     * Returns DcMotorVelocities as a string
     *
     * @return Velocities [array to string]
     */
    override fun toString(): String {
        return "Velocity{" +
                "velocities=" + velocities.contentToString() +
                '}'
    }

}