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

package LibTMOA.direction;

import LibTMOA.math.Calculate;
import LibTMOA.utils.VelocityChecker;

public class StandardMovement {
    public static double[] move(double Vd, double Td, double Vt) {

        if (!VelocityChecker.checkSpeed(Vd)) {
            return null;
        }
        if (!VelocityChecker.checkAngle(Td)) {
            return null;
        }
        if (!VelocityChecker.checkSpeed(Vt)) {
            return null;
        }

        return velocitiesCreator(Vd, Td, Vt);
    }

    public static double[] move(double y, double x) {
        if (!VelocityChecker.checkCoordinates(y, x)) {
            return null;
        }

        double Vd = Calculate.getSpeed(y, x);
        double Td = Calculate.getAngle(y, x);

        if (!VelocityChecker.checkAngle(Td)) {
            return null;
        }

        return velocitiesCreator(Vd, Td, 0);
    }

    private static double[] velocitiesCreator(double Vd, double Td, double Vt) {
        double[] velocities = new double[4];

        velocities[0] = Calculate.calc2(Vd, Td, Vt);
        velocities[1] = Calculate.calc1(Vd, Td, Vt);
        velocities[2] = Calculate.calc1(Vd, Td, Vt);
        velocities[3] = Calculate.calc2(Vd, Td, Vt);

        return velocities;
    }
}