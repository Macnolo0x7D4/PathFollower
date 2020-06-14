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
     * Returns power value (for FR,RL).
     *
     * @param directives MecanumDirectives
     * @return Power Value
     */
    @Deprecated
    public static double calc1(MecanumDirectives directives) {
        double vd = Math.abs(directives.getVd());
        double td = directives.getTd();
        double vt = directives.getVt();

        return vd * Math.sin(td + (Math.PI / 4)) + vt;
    }

    /**
     * Returns power value (for FL,RR).
     *
     * @param directives MecanumDirectives
     * @return Power Value
     */
    @Deprecated
    public static double calc2(MecanumDirectives directives) {
        double vd = Math.abs(directives.getVd());
        double td = directives.getTd();
        double vt = directives.getVt();

        return vd * Math.cos(td + (Math.PI / 4)) + vt;
    }

    /**
     * Returns Power value
     *
     * @param diagonal   Selects diagonal. If 'diagonal' is true, selects FL and RR, else FR and RL.
     * @param directives MecanumDirectives
     * @return Power Value
     */
    public static double calc(MecanumDirectives directives, boolean diagonal) {
        double vd = Math.abs(directives.getVd());
        double td = directives.getTd();

        if (!diagonal)
            return vd * Math.sin(td - (Math.PI / 4));
        else
            return vd * Math.cos(td - (Math.PI / 4));
    }
}