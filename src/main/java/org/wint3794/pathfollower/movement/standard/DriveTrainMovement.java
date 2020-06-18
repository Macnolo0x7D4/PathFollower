/*
 * Copyright 2020 WinT 3794 (Manuel Diaz Rojo and Alexis Obed Garcia Hernandez)
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

package org.wint3794.pathfollower.movement.standard;

import org.wint3794.pathfollower.models.config.ChassisConfiguration;
import org.wint3794.pathfollower.movement.Movement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.wint3794.pathfollower.robot.RuntimeRobotVars.*;

/**
 * A Movement Class that calculates the power of DcMotors from Position values.
 */
public class DriveTrainMovement implements Movement {
    private final ChassisConfiguration config;

    /**
     * Creates an instance of a DriveTrain Chassis.
     * @param config
     */
    public DriveTrainMovement(ChassisConfiguration config) {
        this.config = config;
    }

    /**
     * Converts movement values into DcMotor Powers.
     */
    @Override
    public void apply() {
        List<Double> rawVelocities = new ArrayList<>();

        rawVelocities.add(movementY - movementTurn + movementX * 1.5); // fl
        rawVelocities.add(-movementY - movementTurn + movementX * 1.5); //fr
        rawVelocities.add(movementY - movementTurn - movementX * 1.5); // rl
        rawVelocities.add(-movementY - movementTurn - movementX * 1.5); // rr

        Optional<Double> maxRawPower = rawVelocities.stream().max(Comparator.naturalOrder());

        final double scaleDownAmount = maxRawPower.orElseThrow() > 1.0 ? 1.0 / maxRawPower.get() : 1.0;

        final List<Double> finalVelocities = rawVelocities.stream().map(power -> power *= scaleDownAmount).collect(Collectors.toList());

        finalVelocities.forEach(power -> config.getMotor((byte) finalVelocities.indexOf(power)).setPower(power));
    }
}


