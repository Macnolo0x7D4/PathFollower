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

package org.wint3794.pathfollower.adapters;

import org.wint3794.pathfollower.debug.Log;
import org.wint3794.pathfollower.hardware.DcMotorBase;
import org.wint3794.pathfollower.hardware.EncoderBase;

public class DcMotorAdapter implements DcMotorBase {

    private final byte id;
    private final boolean master;

    private double power;
    private boolean inverted;

    public DcMotorAdapter(byte id, boolean master) {
        this.id = id;
        this.master = master;
    }

    public DcMotorAdapter(byte id) {
        this.id = id;
        this.master = false;
    }

    @Override
    public double getPower() {
        return inverted ? -power : power;
    }

    /*
        public void setPower(double Power) {
            double powerApply = Power;
            if (Math.abs(powerApply - lastPower) > 0.05 || powerApply == 0 && lastPower != 0) {
                myMotor.setPower(powerApply);
                numHardwareUsesThisUpdate++;
                lastPower = powerApply;
            }
        }
     */

    @Override
    public void setPower(double power) {
        Log.println("Now, power is: " + power, getOrigin());
        this.power = power;
    }

    @Override
    public void stop() {
        this.power = 0;
    }

    @Override
    public void setInverted(boolean inverted) {
        this.inverted = inverted;
        if (inverted)
            Log.println("Inverted!", getOrigin());
    }

    @Override
    public EncoderBase getEncoder() {
        return new EncoderAdapter();
    }

    @Override
    public boolean isMaster() {
        return master;
    }

    @Override
    public byte getId() {
        return id;
    }

    private String getOrigin() {
        return "Motor " + getId();
    }
}
