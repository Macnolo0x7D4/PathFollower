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

import org.wint3794.pathfollower.controllers.Robot;
import org.wint3794.pathfollower.models.config.ChassisConfiguration;
import org.wint3794.pathfollower.utils.MathUtils;
import org.wint3794.pathfollower.utils.SpeedOmeter;

public class MyPosition {

    public static Robot myRobot;
    public static double moveScalingFactor = 12.56064392;
    public static double turnScalingFactor = 35.694;
    public static double auxScalingFactor = 12.48;//12.6148;
    public static double auxPredictionScalingFactor = 0.92;


    public static double wheelLeftLast = 0.0;
    public static double wheelRightLast = 0.0;
    public static double wheelAuxLast = 0.0;

    public static double worldXPosition = 0.0;
    public static double worldYPosition = 0.0;
    public static double worldAngle_rad = 0.0;

    public static double worldXPositionOld = 0.0;
    public static double worldYPositionOld = 0.0;

    public static double currPos_l = 0;
    public static double currPos_r = 0;
    public static double currPos_a = 0;

    public static double wheelLeftInitialReading = 0.0;
    public static double wheelRightInitialReading = 0.0;
    public static double lastResetAngle = 0.0;

    //calc cuanto hemos avanzado
    public static double currentTravelYDistance = 0.0;


    public static void initialize(ChassisConfiguration config, Robot myRobot) {
        MyPosition.myRobot = myRobot;

        /*
        currPos_l = l;
        currPos_r = r;
        currPos_a = a;
         */

        currPos_l = config.getMotor((byte) 0).getEncoder().getCurrentPosition();
        currPos_r = config.getMotor((byte) 1).getEncoder().getCurrentPosition();
        currPos_a = config.getMotor((byte) 2).getEncoder().getCurrentPosition();

        update();
    }

    public static void giveMePositions(ChassisConfiguration config) {
        currPos_l = config.getMotor((byte) 0).getEncoder().getCurrentPosition();
        currPos_r = config.getMotor((byte) 1).getEncoder().getCurrentPosition();
        currPos_a = config.getMotor((byte) 2).getEncoder().getCurrentPosition();

        update();
    }

    private static void update() {
        PositioningCalculations();
    }

    /**
     * Updates our position on the field using the change from the encoders
     */
    public static void PositioningCalculations() {
        double wheelLeftCurrent = -currPos_l;
        double wheelRightCurrent = currPos_r;
        double wheelAuxCurrent = currPos_a;

        //compute how much the wheel data has changed
        double wheelLeftDelta = wheelLeftCurrent - wheelLeftLast;
        double wheelRightDelta = wheelRightCurrent - wheelRightLast;
        double wheelAuxDelta = wheelAuxCurrent - wheelAuxLast;


        //get the real distance traveled using the movement scaling factors
        double wheelLeftDeltaScale = wheelLeftDelta * moveScalingFactor / 1000.0;
        double wheelRightDeltaScale = wheelRightDelta * moveScalingFactor / 1000.0;
        double wheelAuxDeltaScale = wheelAuxDelta * auxScalingFactor / 1000.00;

        //get how much our angle has changed
        double angleIncrement = (wheelLeftDelta - wheelRightDelta) * turnScalingFactor / 100000.0;


        //but use absolute for our actual angle
        double wheelRightTotal = currPos_r - wheelRightInitialReading;
        double wheelLeftTotal = -(currPos_l - wheelLeftInitialReading);

        double worldAngleLast = worldAngle_rad;
        worldAngle_rad = MathUtils.AngleWrap(((wheelLeftTotal - wheelRightTotal) * turnScalingFactor / 100000.0) + lastResetAngle);

        //get the predicted amount the straif will go
        double tracker_a_prediction = Math.toDegrees(angleIncrement) * (auxPredictionScalingFactor / 10.0);
        //now subtract that from the actual
        double r_xDistance = wheelAuxDeltaScale - tracker_a_prediction;


        //relativeY will by defa
        double relativeY = (wheelLeftDeltaScale + wheelRightDeltaScale) / 2.0;
        double relativeX = r_xDistance;


        //if angleIncrement is > 0 we can use steven's dumb stupid and stupid well you know the point
        //equations because he is dumb
        if (Math.abs(angleIncrement) > 0) {
            //gets the radius of the turn we are in
            double radiusOfMovement = (wheelRightDeltaScale + wheelLeftDeltaScale) / (2 * angleIncrement);
            //get the radius of our straifing circle
            double radiusOfStraif = r_xDistance / angleIncrement;


            relativeY = (radiusOfMovement * Math.sin(angleIncrement)) - (radiusOfStraif * (1 - Math.cos(angleIncrement)));

            relativeX = radiusOfMovement * (1 - Math.cos(angleIncrement)) + (radiusOfStraif * Math.sin(angleIncrement));

        }


        worldXPosition += (Math.cos(worldAngleLast) * relativeY) + (Math.sin(worldAngleLast) *
                relativeX);
        worldYPosition += (Math.sin(worldAngleLast) * relativeY) - (Math.cos(worldAngleLast) *
                relativeX);


        SpeedOmeter.yDistTraveled += relativeY;
        SpeedOmeter.xDistTraveled += r_xDistance;


        //save the last positions for later
        wheelLeftLast = wheelLeftCurrent;
        wheelRightLast = wheelRightCurrent;
        wheelAuxLast = wheelAuxCurrent;


        //save how far we traveled in the y dimension this update for anyone that needs it
        //currently the absolute control of the collector radius uses it to compensate for
        //robot movement
        currentTravelYDistance = relativeY;
    }

    /**
     * Updates our position on the field using the change from the encoders
     */
    public static void PositioningCalculationsOld() {
        double wheelLeftCurrent = currPos_l;
        double wheelRightCurrent = -currPos_r;
        double wheelAuxCurrent = currPos_a;

        //compute how much the wheel data has changed
        double wheelLeftDelta = wheelLeftCurrent - wheelLeftLast;
        double wheelRightDelta = wheelRightCurrent - wheelRightLast;
        double wheelAuxDelta = wheelAuxCurrent - wheelAuxLast;


        //get the real distance traveled using the movement scaling factors
        double wheelLeftDeltaScale = wheelLeftDelta * moveScalingFactor / 1000.0;
        double wheelRightDeltaScale = wheelRightDelta * moveScalingFactor / 1000.0;
        double wheelAuxDeltaScale = wheelAuxDelta * auxScalingFactor / 1000.00;


        //get how much our angle has changed
        double angleIncrement = (wheelLeftDelta - wheelRightDelta) * turnScalingFactor / 100000.0;


        //but use absolute for our actual angle
        double wheelRightTotal = currPos_r - wheelRightInitialReading;
        double wheelLeftTotal = -(currPos_l - wheelLeftInitialReading);
        worldAngle_rad = MathUtils.AngleWrap(((wheelLeftTotal - wheelRightTotal) * turnScalingFactor / 100000.0) + lastResetAngle);


        //relative y translation
        double r_yDistance = (wheelRightDeltaScale + wheelLeftDeltaScale) / 2;


        double tracker_a_prediction = Math.toDegrees(angleIncrement) * (auxPredictionScalingFactor / 10.0);
        double r_xDistance = wheelAuxDeltaScale - tracker_a_prediction;


        worldXPosition += (Math.cos(worldAngle_rad) * r_yDistance) + (Math.sin(worldAngle_rad) *
                r_xDistance);
        worldYPosition += (Math.sin(worldAngle_rad) * r_yDistance) - (Math.cos(worldAngle_rad) *
                r_xDistance);


        SpeedOmeter.yDistTraveled += r_yDistance;
        SpeedOmeter.xDistTraveled += r_xDistance;


        //save the last positions for later
        wheelLeftLast = wheelLeftCurrent;
        wheelRightLast = wheelRightCurrent;
        wheelAuxLast = wheelAuxCurrent;


        //save how far we traveled in the y dimension this update for anyone that needs it
        //currently the absolute control of the collector radius uses it to compensate for
        //robot movement
        currentTravelYDistance = r_yDistance;

    }

    public static double subtractAngles(double angle1, double angle2) {
        return MathUtils.AngleWrap(angle1 - angle2);
    }


    /**
     * USE THIS TO SET OUR POSITION
     **/
    public static void setPosition(double x, double y, double angle) {
        worldXPosition = x;
        worldYPosition = y;
        worldAngle_rad = angle;

        worldXPositionOld = x;
        worldYPositionOld = y;

        //remember where we were at the time of the reset
        wheelLeftInitialReading = currPos_l;
        wheelRightInitialReading = currPos_r;
        lastResetAngle = angle;
    }
}

