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

package LibTMOA.controllers;

import LibTMOA.TMOA;
import LibTMOA.models.config.ChassisConfiguration;
import LibTMOA.robot.MyPosition;
import LibTMOA.utils.Range;
import LibTMOA.utils.SpeedOmeter;

import static LibTMOA.robot.VariablesOfMovement.*;

public class Robot {
    private final TMOA tmoa;
    private static boolean usingComputer = false;
    private static double xSpeed = 0;
    private static double ySpeed = 0;
    private static double turnSpeed = 0;
    private static double worldXPosition;
    private static double worldYPosition;
    private static double worldAngle;

    private long lastUpdateTime = 0;


    /**
     * Creates a robot simulation
     */
    public Robot(TMOA tmoa) {
        this.tmoa = tmoa;

        worldXPosition = 100;
        worldYPosition = 140;
        worldAngle = Math.toRadians(-180);
    }

    public double getXPos() {
        return worldXPosition;
    }

    public double getYPos() {
        return worldYPosition;
    }

    public double getWorldAngle_rad() {
        return worldAngle;
    }

    /**
     * Calculates the change in position of the robot
     */
    public void update() {
        //tiempo
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

        worldAngle += movement_turn * elapsedTime * 20 / (2 * Math.PI);


        xSpeed += Range.clip((movement_x - xSpeed) / 0.2, -1, 1) * elapsedTime;
        ySpeed += Range.clip((movement_y - ySpeed) / 0.2, -1, 1) * elapsedTime;
        turnSpeed += Range.clip((movement_turn - turnSpeed) / 0.2, -1, 1) * elapsedTime;


        SpeedOmeter.yDistTraveled += ySpeed * elapsedTime * 1000;
        SpeedOmeter.xDistTraveled += xSpeed * elapsedTime * 1000;

        SpeedOmeter.update();


        xSpeed *= 1.0 - (elapsedTime);
        ySpeed *= 1.0 - (elapsedTime);
        turnSpeed *= 1.0 - (elapsedTime);


    }

    public void CurvoidStartPos() {
        ChassisConfiguration chassis = tmoa.getChassisInformation();

        for (int i = 0; i < 2; i++) {
            MyPosition.initialize(chassis, this);

        }
    }

    public void loop() {
        MyPosition.giveMePositions(tmoa.getChassisInformation());
    }

    public static double getWorldXPosition() {
        return worldXPosition;
    }

    public static double getWorldYPosition() {
        return worldYPosition;
    }

    public static double getWorldAngle() {
        return worldAngle;
    }

    public static boolean isUsingComputer() {
        return usingComputer;
    }
}
