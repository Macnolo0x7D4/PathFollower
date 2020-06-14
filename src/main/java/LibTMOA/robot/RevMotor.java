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

package LibTMOA.robot;

import LibTMOA.models.config.DcMotorBase;


public class RevMotor {
    public static int numHardwareUsesThisUpdate = 0;
    public DcMotorBase myMotor;
    public boolean isMaster;
    private int currPosition = 0; //medida en ticks
    private double lastPower = -1;

    public RevMotor(DcMotorBase motor, boolean master) {
        myMotor = motor;
        isMaster = master;
    }

    public static void endUpdate() {
        numHardwareUsesThisUpdate = 0;
    }

    public void setPower(double Power) {
        double powerApply = Power;
        if (Math.abs(powerApply - lastPower) > 0.05 || powerApply == 0 && lastPower != 0) {
            myMotor.setPower(powerApply);
            numHardwareUsesThisUpdate++;
            lastPower = powerApply;
        }
    }

    public void encoderReading(int position) {
        currPosition = position;
    }

    public int getCurrPosition() {
        return currPosition;
    }

}
