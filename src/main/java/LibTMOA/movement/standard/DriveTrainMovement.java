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

package LibTMOA.movement.standard;

import LibTMOA.models.config.ChassisConfiguration;

import java.util.*;
import java.util.stream.Collectors;

import static LibTMOA.robot.VariablesOfMovement.*;


public class DriveTrainMovement {
    private final ChassisConfiguration config;
    private final long lastUpdateTime = 0;

    public DriveTrainMovement(ChassisConfiguration config) {
        this.config = config;
    }

    /**
     * converts movement_y, movement_x, movement_turn into motor powers
     */
    public void ApplyMovement() {
        List<Double> rawVelocities = new ArrayList<>();

        rawVelocities.add(movement_y - movement_turn + movement_x * 1.5); // fl
        rawVelocities.add(-movement_y - movement_turn + movement_x * 1.5); //fr
        rawVelocities.add(movement_y - movement_turn - movement_x * 1.5); // rl
        rawVelocities.add(-movement_y - movement_turn - movement_x * 1.5); // rr

        Optional<Double> maxRawPower = rawVelocities.stream().max(Comparator.naturalOrder());

        final double scaleDownAmount = maxRawPower.get() > 1.0 ? 1.0 / maxRawPower.get() : 1.0;

        final List<Double> finalVelocities = rawVelocities.stream().map( power -> power *= scaleDownAmount).collect(Collectors.toList());

        finalVelocities.forEach( power -> config.getMotor((byte)finalVelocities.indexOf(power)).setPower(power));
    }
}


