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

class PIDFCoefficients {
    private val kP: Double
    private val kI: Double
    private val kD: Double
    private val kF: Double
    val pidValues: PIDValues

    constructor(kP: Double, kI: Double, kD: Double, kF: Double) {
        this.kP = kP
        this.kI = kI
        this.kD = kD
        this.kF = kF
        pidValues = PIDValues()
    }

    /**
     * @param kP
     * @param kI
     * @param kD
     * @param kF
     * @param values PIDValues
     */
    constructor(kP: Double, kI: Double, kD: Double, kF: Double, values: PIDValues) {
        this.kP = kP
        this.kI = kI
        this.kD = kD
        this.kF = kF
        pidValues = values
    }

    fun getkP(): Double {
        return kP
    }

    fun getkI(): Double {
        return kI
    }

    fun getkD(): Double {
        return kD
    }

    fun getkF(): Double {
        return kF
    }

}