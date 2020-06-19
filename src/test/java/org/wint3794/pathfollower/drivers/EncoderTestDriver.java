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

package org.wint3794.pathfollower.drivers;

import org.wint3794.pathfollower.hardware.EncoderBase;
import org.wint3794.pathfollower.hardware.encoder.Encoders;
import org.wint3794.pathfollower.hardware.encoder.ZeroPowerBehavior;

public class EncoderTestDriver implements EncoderBase {

    private Encoders encoder;
    private ZeroPowerBehavior zeroPowerBehavior;
    private int numHardwareUsesThisUpdate = 0;
    private long currentPosition = 0;

    @Override
    public void setMode(Encoders encoder) {
        this.encoder = encoder;
    }

    @Override
    public void setBrake(boolean brake) {
        this.zeroPowerBehavior = brake ? ZeroPowerBehavior.BRAKE : ZeroPowerBehavior.FLOAT;
    }

    @Override
    public long getCurrentPosition() {
        return this.currentPosition;
    }

    @Override
    public void setCurrentPosition(long currentPosition) {
        this.currentPosition = currentPosition;
    }

    @Override
    public void endUpdate() {
        numHardwareUsesThisUpdate = 0;
    }

    @Override
    public Encoders getEncoderMode() {
        return this.encoder;
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return this.zeroPowerBehavior;
    }
}
