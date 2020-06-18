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

package org.wint3794.pathfollower.utils;


import org.wint3794.pathfollower.controllers.Robot;

import static org.wint3794.pathfollower.utils.MathUtils.AngleWrap;

public class SpeedOmeter {
    //min time between updates to make sure our speed is accurate
    public static int timeBetweenUpdates = 25;
    public static double yDistTraveled = 0;
    public static double xDistTraveled = 0;
    public static double lastAngle = 0;
    public static double angularVelocity = 0;
    public static double scalePrediction = 1.0;
    //amount robot slips (cm) while going forwards 1 centimeter per second
    public static double ySlipDistanceFor1CMPS = 0.14 * scalePrediction;//0.169;
    public static double xSlipDistanceFor1CMPS = 0.153 * scalePrediction;//0.117;
    //radians the robot slips when going 1 radian per second
    public static double turnSlipAmountFor1RPS = 0.09 * scalePrediction;//0.113;
    private static long lastUpdateStartTime = 0;
    private static double currSpeedY = 0.0;
    private static double currSpeedX = 0.0;

    //calculates our current velocity every update
    public static void update() {
        long currTime = System.currentTimeMillis();

        //return if no change in telemetry
        if (Math.abs(yDistTraveled) < 0.000000001 && Math.abs(xDistTraveled) < 0.000000001 &&
                Math.abs(angularVelocity) < 0.000001) {
            return;
        }


        if (currTime - lastUpdateStartTime > timeBetweenUpdates) {
            //elapsedTime in seconds
            double elapsedTime = (double) (currTime - lastUpdateStartTime) / 1000.0;
            double speedY = yDistTraveled / elapsedTime;
            double speedX = xDistTraveled / elapsedTime;

            if (speedY < 200 && speedX < 200) {//I can assure you our robot can't go 200 m/s
                currSpeedY = speedY;
                currSpeedX = speedX;
            }


            angularVelocity = AngleWrap(Robot.getWorldAngle() - lastAngle) / elapsedTime;
            lastAngle = Robot.getWorldAngle();

            yDistTraveled = 0;
            xDistTraveled = 0;
            lastUpdateStartTime = currTime;
        }
    }

    /**
     * gets relative y speed in cm/s
     */
    public static double getSpeedY() {
        return currSpeedY;
    }

    /**
     * gets relative x speed = cm/s
     */
    public static double getSpeedX() {
        return currSpeedX;
    }

    public static double getDegPerSecond() {
        return Math.toDegrees(angularVelocity);
    }

    public static double getRadPerSecond() {
        return angularVelocity;
    }

    /**
     * Gives the current distance (cm) the robot would slip if power is set to 0
     */
    public static double currSlipDistanceY() {
        return SpeedOmeter.getSpeedY() * ySlipDistanceFor1CMPS;
    }

    public static double currSlipDistanceX() {
        return SpeedOmeter.getSpeedX() * xSlipDistanceFor1CMPS;
    }

    /**
     * Gives the number of radians the robot would turn if power was cut now
     */
    public static double currSlipAngle() {
        return getRadPerSecond() * turnSlipAmountFor1RPS;
    }
}
