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

package LibTMOA.math;

import LibTMOA.utils.Utilities;

public class Calculate {
    public static double calc1(double Vd, double Td, double Vt) {
        return Vd * Math.sin(Td + (Math.PI / 4)) + Vt;
    }

    public static double calc2(double Vd, double Td, double Vt) {
        return Vd * Math.cos(Td + (Math.PI / 4)) + Vt;
    }

    public static double calc1(double Vd, double Td) {
        return Vd * Math.sin(Td + (Math.PI / 4));
    }

    public static double calc2(double Vd, double Td) {
        return Vd * Math.cos(Td + (Math.PI / 4));
    }

    public static double getAngle(double y, double x) {
        return Math.atan2(y, x);
    }

    public static double getSpeed(double y, double x) {
        if (Math.abs(y) >= Math.abs(x)) {
            return y;
        }

        return x;
    }

    public static double roundPower(double value) {
        long factor = (long) Math.pow(10, Utilities.roundPower);
        value = value * factor;
        return (double) Math.round(value) / factor;
    }
}
