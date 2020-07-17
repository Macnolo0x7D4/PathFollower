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

import org.wint3794.pathfollower.models.chassis.MecanumDirectives

/**
 * A class with all the mathematics methods to get Mecanum DcMotor Powers.
 */
object CalculatePower {
    /**
     * Returns power value (for FR,RL).
     *
     * @param directives MecanumDirectives
     * @return Power Value
     */
    @Deprecated("")
    fun calc1(directives: MecanumDirectives): Double {
        val vd = Math.abs(directives.vd)
        val td = directives.td
        val vt = directives.vt
        return vd * Math.sin(td + Math.PI / 4) + vt
    }

    /**
     * Returns power value (for FL,RR).
     *
     * @param directives MecanumDirectives
     * @return Power Value
     */
    @Deprecated("")
    fun calc2(directives: MecanumDirectives): Double {
        val vd = Math.abs(directives.vd)
        val td = directives.td
        val vt = directives.vt
        return vd * Math.cos(td + Math.PI / 4) + vt
    }

    /**
     * Returns Power value
     *
     * @param diagonal   Selects diagonal. If 'diagonal' is true, selects FL and RR, else FR and RL.
     * @param directives MecanumDirectives
     * @return Power Value
     */
    fun calc(directives: MecanumDirectives, diagonal: Boolean): Double {
        val vd = Math.abs(directives.vd)
        val td = directives.td
        return if (!diagonal) vd * Math.sin(td - Math.PI / 4) else vd * Math.cos(
            td - Math.PI / 4
        )
    }
}