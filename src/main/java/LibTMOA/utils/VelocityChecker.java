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

package LibTMOA.utils;

public class VelocityChecker {
    public static boolean checkSpeed(double Vd) {
        return Math.abs(Vd) <= 1;
    }

    public static boolean checkAngle(double Td) {
        return Math.abs(Td) <= 2 * Math.PI;
    }

    public static boolean checkCoordinates(double y, double x) {
        return Math.abs((x + y) / 2) <= 1;
    }

}
