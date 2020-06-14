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

import static LibTMOA.robot.VariablesOfMovement.*;


public class DriveTrain {
    public RevMotor Tright;
    public RevMotor Bright;
    public RevMotor Tleft;
    public RevMotor Bleft;
    private final long lastUpdateTime = 0;

    public DriveTrain(RevMotor Tr, RevMotor Br, RevMotor Tl, RevMotor Bl) {
        Tright = Tr;
        Bright = Br;
        Tleft = Tl;
        Bleft = Bl;


    }

    /**
     * converts movement_y, movement_x, movement_turn into motor powers
     */
    public void ApplyMovement() {

        double tl_power_raw = movement_y - movement_turn + movement_x * 1.5;
        double bl_power_raw = movement_y - movement_turn - movement_x * 1.5;
        double br_power_raw = -movement_y - movement_turn - movement_x * 1.5;
        double tr_power_raw = -movement_y - movement_turn + movement_x * 1.5;


        //find the maximum of the powers
        double maxRawPower = Math.abs(tl_power_raw);
        if (Math.abs(bl_power_raw) > maxRawPower) {
            maxRawPower = Math.abs(bl_power_raw);
        }
        if (Math.abs(br_power_raw) > maxRawPower) {
            maxRawPower = Math.abs(br_power_raw);
        }
        if (Math.abs(tr_power_raw) > maxRawPower) {
            maxRawPower = Math.abs(tr_power_raw);
        }

        //if the maximum is greater than 1, scale all the powers down to preserve the shape
        double scaleDownAmount = 1.0;
        if (maxRawPower > 1.0) {
            //when max power is multiplied by this ratio, it will be 1.0, and others less
            scaleDownAmount = 1.0 / maxRawPower;
        }
        tl_power_raw *= scaleDownAmount;
        bl_power_raw *= scaleDownAmount;
        br_power_raw *= scaleDownAmount;
        tr_power_raw *= scaleDownAmount;


        //now we can set the powers ONLY IF THEY HAVE CHANGED TO AVOID SPAMMING USB COMMUNICATIONS
        Tleft.setPower(tl_power_raw);
        Bleft.setPower(bl_power_raw);
        Bright.setPower(br_power_raw);
        Tright.setPower(tr_power_raw);
    }
}


