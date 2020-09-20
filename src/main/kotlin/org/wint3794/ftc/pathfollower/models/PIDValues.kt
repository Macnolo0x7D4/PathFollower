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

import org.wint3794.ftc.pathfollower.util.Constants

class PIDValues {
    /**
     * Returns the setpoint of the calculation.
     *
     * @return SetPoint
     */
    /**
     * Sets the setpoint of the calculation.
     *
     * @param sp SetPoint
     */
    var sp: Double
    /**
     * Returns the previous value of the calculation.
     *
     * @return Previous Value
     */
    /**
     * Sets the previous value of the calculation.
     *
     * @param pv Previous Value
     */
    var pv: Double
    var period = 0.0

    /**
     * Creates PID Values with DEFAULT Configuration [0, 0, Constants.DEFAULT_PERIOD]
     */
    constructor() {
        sp = 0.0
        pv = 0.0
        period = Constants.DEFAULT_PERIOD
    }

    /**
     * Creates PID Values with desired values.
     *
     * @param sp     The setpoint of the calculation.
     * @param pv     The previous value of the calculation.
     * @param period
     */
    constructor(sp: Double, pv: Double, period: Double) {
        this.sp = sp
        this.pv = pv
        this.period = period
    }

    /**
     * Creates PID Values with desired values.
     *
     * @param sp The setpoint of the calculation.
     * @param pv The previous value of the calculation.
     */
    constructor(sp: Double, pv: Double) {
        this.sp = sp
        this.pv = pv
    }

}