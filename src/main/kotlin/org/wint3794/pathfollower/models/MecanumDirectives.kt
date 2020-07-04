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

/**
 * ADT of Mecanum Directives [Vd, Td, Vt]
 */
class MecanumDirectives {
    /**
     * Returns Desired Magnitude
     *
     * @return Desired Magnitude [double]
     */
    val vd: Double

    /**
     * Returns Desired Angle
     *
     * @return Desired Angle [double]
     */
    val td: Double

    /**
     * Returns Desired Rotation
     *
     * @return Desired Rotation [double]
     */
    val vt: Double

    /**
     * Creates a MecanumDirectives object from Vd, Td and Vt.
     *
     * @param vd Desired Magnitude [0 - 1]
     * @param td Desired Angle [0 - 2 * Math.PI]
     * @param vt Desired Rotation [-1 - 1]
     */
    constructor(vd: Double, td: Double, vt: Double) {
        this.vd = vd
        this.td = td
        this.vt = vt
    }

    /**
     * Creates a MecanumDirectives object from Vd and Td.
     *
     * @param vd Desired Magnitude [0 - 1]
     * @param td Desired Angle [0 - 2 * Math.PI]
     */
    constructor(vd: Double, td: Double) {
        this.vd = vd
        this.td = td
        vt = 0.0
    }

    /**
     * Returns array with directives
     *
     * @return MecanumDirectives as an array
     */
    val directives: DoubleArray
        get() {
            val directives = DoubleArray(3)
            directives[0] = vd
            directives[1] = td
            directives[2] = vt
            return directives
        }

    /**
     * Returns string with directives
     *
     * @return MecanumDirectives as a string
     */
    override fun toString(): String {
        return "MecanumDirectives{" +
                "Vd=" + vd +
                ", Td=" + td +
                ", Vt=" + vt +
                '}'
    }
}