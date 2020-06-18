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

package org.wint3794.pathfollower.models.structures;

import org.wint3794.pathfollower.utils.Constants;

public class PIDValues {
    private double sp;
    private double pv;
    private double period;

    /**
     * Creates PID Values with DEFAULT Configuration [0, 0, Constants.DEFAULT_PERIOD]
     */
    public PIDValues() {
        this.sp = 0;
        this.pv = 0;
        this.period = Constants.DEFAULT_PERIOD;
    }

    /**
     * Creates PID Values with desired values.
     *
     * @param sp     The setpoint of the calculation.
     * @param pv     The previous value of the calculation.
     * @param period
     */
    public PIDValues(double sp, double pv, double period) {
        this.sp = sp;
        this.pv = pv;
        this.period = period;
    }

    /**
     * Creates PID Values with desired values.
     *
     * @param sp The setpoint of the calculation.
     * @param pv The previous value of the calculation.
     */
    public PIDValues(double sp, double pv) {
        this.sp = sp;
        this.pv = pv;
    }

    /**
     * Returns the setpoint of the calculation.
     *
     * @return SetPoint
     */
    public double getSp() {
        return sp;
    }

    /**
     * Sets the setpoint of the calculation.
     *
     * @param sp SetPoint
     */
    public void setSp(double sp) {
        this.sp = sp;
    }

    /**
     * Returns the previous value of the calculation.
     *
     * @return Previous Value
     */
    public double getPv() {
        return pv;
    }

    /**
     * Sets the previous value of the calculation.
     *
     * @param pv Previous Value
     */
    public void setPv(double pv) {
        this.pv = pv;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }
}
