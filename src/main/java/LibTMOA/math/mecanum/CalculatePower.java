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

package LibTMOA.math.mecanum;

import LibTMOA.models.structures.MecanumDirectives;

/**
 * A class with all the mathematics methods to get Mecanum DcMotor Powers.
 */
public class CalculatePower {
    /**
     * Returns power value (for FR,BL).
     * @param directives MecanumDirectives
     * @return Power Value
     */
    public static double calc1(MecanumDirectives directives) {
        double vd = Math.abs(directives.getVd());
        double td = directives.getTd();

        return vd * Math.sin(td + (Math.PI / 4)) + directives.getVt();
    }

    /**
     * Returns power value (for FL,BR).
     * @param directives MecanumDirectives
     * @return Power Value
     */
    public static double calc2(MecanumDirectives directives) {
        double vd = Math.abs(directives.getVd());
        double td = Math.abs(directives.getTd());

        return vd * Math.cos(td + (Math.PI / 4)) + directives.getVt();
    }
}
