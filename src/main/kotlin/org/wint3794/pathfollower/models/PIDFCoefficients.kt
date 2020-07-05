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

data class PIDFCoefficients(var kP: Double, var kI: Double, var kD: Double, var kF: Double, var values: PIDValues){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PIDFCoefficients

        if (kP != other.kP) return false
        if (kI != other.kI) return false
        if (kD != other.kD) return false
        if (kF != other.kF) return false
        if (values != other.values) return false

        return true
    }

    override fun hashCode(): Int {
        var result = kP.hashCode()
        result = 31 * result + kI.hashCode()
        result = 31 * result + kD.hashCode()
        result = 31 * result + kF.hashCode()
        result = 31 * result + values.hashCode()
        return result
    }
}