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

package org.wint3794.pathfollower.robot;

import org.wint3794.pathfollower.PathFollower;
import org.wint3794.pathfollower.drivebase.ChassisConfiguration;
import org.wint3794.pathfollower.robot.MyPosition;
import org.wint3794.pathfollower.util.Range;
import org.wint3794.pathfollower.util.SpeedOmeter;

import static org.wint3794.pathfollower.robot.RuntimeRobotVars.*;

public class Robot {
    private static double xSpeed = 0;
    private static double ySpeed = 0;
    private static double turnSpeed = 0;
    private static double worldXPosition;
    private static double worldYPosition;
    private static double worldAngle;
    private final PathFollower pathFollower;
    private long lastUpdateTime = 0;


    /**
     * Creates a robot simulation
     */
    public Robot(PathFollower pathFollower) {
        this.pathFollower = pathFollower;

        worldXPosition = 100;
        worldYPosition = 140;
        worldAngle = Math.toRadians(-180);
    }

    public static double getXPos() {
        return worldXPosition;
    }

    public static double getYPos() {
        return worldYPosition;
    }

    public static double getWorldAngle() {
        return worldAngle;
    }

    /**
     * Calculates the change in position of the robot
     */
    public void update() {
        long currentTimeMillis = System.currentTimeMillis();

        double elapsedTime = (currentTimeMillis - lastUpdateTime) / 1000.0;

        lastUpdateTime = currentTimeMillis;
        if (elapsedTime > 1) {
            return;
        }


        //incrementa la posicion
        double totalSpeed = Math.hypot(xSpeed, ySpeed);
        double angle = Math.atan2(ySpeed, xSpeed) - Math.toRadians(90);
        double outputAngle = worldAngle + angle;
        worldXPosition += totalSpeed * Math.cos(outputAngle) * elapsedTime * 1000 * 0.2;
        worldYPosition += totalSpeed * Math.sin(outputAngle) * elapsedTime * 1000 * 0.2;

        worldAngle += movementTurn * elapsedTime * 20 / (2 * Math.PI);


        xSpeed += Range.clip((movementX - xSpeed) / 0.2, -1, 1) * elapsedTime;
        ySpeed += Range.clip((movementY - ySpeed) / 0.2, -1, 1) * elapsedTime;
        turnSpeed += Range.clip((movementTurn - turnSpeed) / 0.2, -1, 1) * elapsedTime;


        SpeedOmeter.yDistTraveled += ySpeed * elapsedTime * 1000;
        SpeedOmeter.xDistTraveled += xSpeed * elapsedTime * 1000;

        SpeedOmeter.update();


        xSpeed *= 1.0 - (elapsedTime);
        ySpeed *= 1.0 - (elapsedTime);
        turnSpeed *= 1.0 - (elapsedTime);


    }

    public void CurvoidStartPos() {
        ChassisConfiguration chassis = pathFollower.getChassisConfiguration();

        for (int i = 0; i < 2; i++) {
            MyPosition.initialize(chassis, this);

        }
    }

    public void loop() {
        MyPosition.giveMePositions(pathFollower.getChassisConfiguration());
    }
}
