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

package LibTMOA;

import LibTMOA.models.config.DcMotorBase;
import LibTMOA.movement.encoder.Encoders;
import LibTMOA.movement.encoder.ZeroPowerBehavior;

class DcMotorTestDriver implements DcMotorBase {

    private final byte id;
    private Encoders encoder;
    private ZeroPowerBehavior zeroPowerBehavior;
    private double power;
    private boolean inverted;

    public DcMotorTestDriver(byte id) {
        this.id = id;
    }

    @Override
    public double getPower() {
        return inverted ? -power : power;
    }

    @Override
    public void setPower(double power) {
        System.out.println("[Motor " + getId() + "]: Now, power is: " + power);
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
            System.out.println("[Motor " + getId() + "]: Inverted!");
    }

    @Override
    public void setMode(Encoders encoder) {

    }

    @Override
    public void setBrake(boolean brake) {
        this.zeroPowerBehavior = brake ? ZeroPowerBehavior.BRAKE : ZeroPowerBehavior.FLOAT;
    }

    @Override
    public byte getId() {
        return id;
    }
}
