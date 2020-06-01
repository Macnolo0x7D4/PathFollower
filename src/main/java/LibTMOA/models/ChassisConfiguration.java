/*
 * Copyright 2020 WinT 3794 (Manuel Díaz Rojo and Alexis Obed García Hernández)
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

package LibTMOA.models;

import java.util.List;

public class ChassisConfiguration {
    private final List<DcMotorBase> motors;

    private double width;
    private int cpr;
    private int gearRatio;
    private double diameter;
    private double cpi;
    private double conversion;

    private final ExecutionModes mode;

    /**
     * Creates an instance of a Simple Chassis Configuration.
     *
     * @param motors A List of 4 DcMotorBase (not interface, your driver). Order: LF, RF, LB, RB.
     */
    public ChassisConfiguration(List<DcMotorBase> motors) {
        this.mode = ExecutionModes.SIMPLE;
        this.motors = motors;
    }

    /**
     * Creates an instance of an Using-Encoders Chassis Configuration.
     *
     * @param motors    A List of 4 DcMotorBase (not interface, your driver). Order: LF, RF, LB, RB.
     * @param width
     * @param cpr
     * @param gearRatio
     * @param diameter
     */
    public ChassisConfiguration(List<DcMotorBase> motors, double width, int cpr, int gearRatio, double diameter) {
        this.mode = ExecutionModes.ENCODER;
        this.motors = motors;
        this.width = width;
        this.cpr = cpr;
        this.gearRatio = gearRatio;
        this.diameter = diameter;

        this.cpi = (cpr * gearRatio) / (Math.PI * diameter);
    }

    public List<DcMotorBase> getMotors() {
        return this.motors;
    }

    public ExecutionModes getMode() {
        return mode;
    }
}
