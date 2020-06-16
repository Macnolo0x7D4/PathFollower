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

package LibTMOA.models.structures;

public class EncoderProperties {
    private final double width;
    private final double cpr;
    private final double gearRatio;
    private final double diameter;
    private final double cpi;

    public EncoderProperties(double width, double cpr, double gearRatio, double diameter) {
        this.width = width;
        this.cpr = cpr;
        this.gearRatio = gearRatio;
        this.diameter = diameter;
        this.cpi = (cpr * gearRatio) / (Math.PI * diameter);
    }

    public double getWidth() {
        return width;
    }

    public double getCpr() {
        return cpr;
    }

    public double getGearRatio() {
        return gearRatio;
    }

    public double getDiameter() {
        return diameter;
    }

    public double getCpi() {
        return cpi;
    }
}
