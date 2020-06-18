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

package org.wint3794.pathfollower.movement.road;

import org.wint3794.pathfollower.debug.ComputerDebugging;
import org.wint3794.pathfollower.models.structures.Pose2D;
import org.wint3794.pathfollower.movement.road.structures.IndexedPoint;
import org.wint3794.pathfollower.movement.road.structures.LinePoint;
import org.wint3794.pathfollower.movement.road.structures.MovementResult;
import org.wint3794.pathfollower.movement.road.structures.ProfileStates;
import org.wint3794.pathfollower.utils.*;

import java.util.ArrayList;

import static org.wint3794.pathfollower.movement.road.structures.ProfileStates.gunningIt;
import static org.wint3794.pathfollower.robot.MyPosition.*;
import static org.wint3794.pathfollower.robot.RuntimeRobotVars.*;
import static org.wint3794.pathfollower.utils.MathUtils.AngleWrap;


public class WorldMapMovement {
    public static final double smallAdjustSpeed = 0.135;
    public static ProfileStates stateMovementYProf = gunningIt;
    public static ProfileStates stateMovementXProf = gunningIt;
    public static ProfileStates stateTurningProf = gunningIt;
    public static double movementYMin = 0.091;
    public static double movementXMin = 0.11;
    public static double movementTurnMin = 0.10;

    public static String turnCurveVisual =
            "                   1" +
                    "                    " +
                    "                    " +
                    "                    " +
                    "                    " +
                    "1                   " +
                    "                    " +
                    "                    " +
                    "                    ";
    public static PiecewiseFunction turnCurve = new PiecewiseFunction(turnCurveVisual);
    public static int followCurveIndex = 0;

    //inits our mini state machines for motion profiling
    public static void initForMove() {
        stateMovementYProf = gunningIt;
        stateMovementXProf = gunningIt;
        stateTurningProf = gunningIt;
    }

    public static void goToPosition(double targetX, double targetY, double point_angle, double movement_speed, double point_speed) {
        //calc la distancia de nosotros a el punto
        double distanceToPoint = Math.sqrt(Math.pow(targetX - worldXPosition, 2) + Math.pow(targetY - worldYPosition, 2));

        double angleToPoint = Math.atan2(targetY - worldYPosition, targetX - worldXPosition);
        double deltaAngleToPoint = AngleWrap(angleToPoint - (worldAngle_rad - Math.toRadians(90)));
        //componentes de x Y y
        double relative_x_to_point = Math.cos(deltaAngleToPoint) * distanceToPoint;
        double relative_y_to_point = Math.sin(deltaAngleToPoint) * distanceToPoint;

        double relative_abs_x = Math.abs(relative_x_to_point);
        double relative_abs_y = Math.abs(relative_y_to_point);

        /*
        Log.d("testTag", "relative_x: " + relative_x_to_point);
        Log.d("testTag", "relative_y: " + relative_y_to_point);
         */

        double movement_x_power = (relative_x_to_point / (relative_abs_y + relative_abs_x)) * movement_speed;
        double movement_y_power = (relative_y_to_point / (relative_abs_y + relative_abs_x)) * movement_speed;

        //el movimientose compone de dos partes la rapida y la desaceleración
        if (stateMovementYProf == ProfileStates.gunningIt) {
            if (relative_abs_y < Math.abs(SpeedOmeter.currSlipDistanceY() * 2) || relative_abs_y < 3) {
                stateMovementYProf = stateMovementYProf.next();
            }
        }
        if (stateMovementYProf == ProfileStates.slipping) {
            movement_y_power = 0;
            if (Math.abs(SpeedOmeter.getSpeedY()) < 0.03) {
                stateMovementYProf = stateMovementYProf.next();
            }
        }
        if (stateMovementYProf == ProfileStates.fineAdjustment) {
            movement_y_power = Range.clip(((relative_y_to_point / 8.0) * 0.15), -0.15, 0.15);
        }

        if (stateMovementXProf == ProfileStates.gunningIt) {
            if (relative_abs_x < Math.abs(SpeedOmeter.currSlipDistanceY() * 1.2) || relative_abs_x < 3) {
                stateMovementXProf = stateMovementXProf.next();
            }
        }
        if (stateMovementXProf == ProfileStates.slipping) {
            movement_x_power = 0;
            if (Math.abs(SpeedOmeter.getSpeedY()) < 0.03) {
                stateMovementXProf = stateMovementXProf.next();
            }
        }
        if (stateMovementXProf == ProfileStates.fineAdjustment) {
            movement_x_power = Range.clip(((relative_x_to_point / 2.5) * smallAdjustSpeed), -smallAdjustSpeed, smallAdjustSpeed);
        }

        double rad_to_target = AngleWrap(point_angle - worldAngle_rad);
        double turnPower = 0;


        if (stateTurningProf == ProfileStates.gunningIt) {
            turnPower = rad_to_target > 0 ? point_speed : -point_speed;
            if (Math.abs(rad_to_target) < Math.abs(SpeedOmeter.currSlipAngle() * 1.2) || Math.abs(rad_to_target) < Math.toRadians(3.0)) {
                stateTurningProf = stateTurningProf.next();
            }

        }
        if (stateTurningProf == ProfileStates.slipping) {
            if (Math.abs(SpeedOmeter.getDegPerSecond()) < 60) {
                stateTurningProf = stateTurningProf.next();
            }

        }

        if (stateTurningProf == ProfileStates.fineAdjustment) {
            turnPower = (rad_to_target / Math.toRadians(10)) * smallAdjustSpeed;
            turnPower = Range.clip(turnPower, -smallAdjustSpeed, smallAdjustSpeed);
        }

        movementTurn = turnPower;
        movementX = movement_x_power;
        movementY = movement_y_power;

        allComponentsMinPower();
    }

    //angulo relativo hacia noventa grados (adelante)
    public static MovementResult gunToPosition(double targetX, double targetY, double point_angle,
                                               double movement_speed, double point_speed,
                                               double slowDownTurnRadians, double slowDownMovementFromTurnError,
                                               boolean stop) {


        double currSlipY = (SpeedOmeter.currSlipDistanceY() * Math.sin(worldAngle_rad)) +
                (SpeedOmeter.currSlipDistanceX() * Math.cos(worldAngle_rad));
        double currSlipX = (SpeedOmeter.currSlipDistanceY() * Math.cos(worldAngle_rad)) +
                (SpeedOmeter.currSlipDistanceX() * Math.sin(worldAngle_rad));

        double targetXAdjusted = targetX - currSlipX;
        double targetYAdjusted = targetY - currSlipY;

        //distancia sobre el punto ajustado
        double distanceToPoint = Math.sqrt(Math.pow(targetXAdjusted - worldXPosition, 2)
                + Math.pow(targetYAdjusted - worldYPosition, 2));


        double angleToPointAdjusted =
                Math.atan2(targetYAdjusted - worldYPosition, targetXAdjusted - worldXPosition);

        //solo nos importa el angulo relativo por lo que restamos 90 si estamos en cero y usamos el movimiento en y para ir hacia adelante
        double deltaAngleToPointAdjusted = AngleWrap(angleToPointAdjusted - (worldAngle_rad - Math.toRadians(90)));


        double relative_x_to_point = Math.cos(deltaAngleToPointAdjusted) * distanceToPoint;
        double relative_y_to_point = Math.sin(deltaAngleToPointAdjusted) * distanceToPoint;


        double relative_abs_x = Math.abs(relative_x_to_point);
        double relative_abs_y = Math.abs(relative_y_to_point);


        // empezamos a calcular el poder de cada motor
        //empezamos con el power para los motores sin importar la distancia
        double movement_x_power = (relative_x_to_point / (relative_abs_y + relative_abs_x));
        double movement_y_power = (relative_y_to_point / (relative_abs_y + relative_abs_x));


        if (stop) {
            movement_x_power *= relative_abs_x / 30.0;
            movement_y_power *= relative_abs_y / 30.0;
        }


        movementX = Range.clip(movement_x_power, -movement_speed, movement_speed);
        movementY = Range.clip(movement_y_power, -movement_speed, movement_speed);


        double actualRelativePointAngle = (point_angle - Math.toRadians(90));

        //angulo absoluto al punto sobre el campo
        double angleToPointRaw = Math.atan2(targetY - worldYPosition, targetX - worldXPosition);

        double absolutePointAngle = angleToPointRaw + actualRelativePointAngle;


        double relativePointAngle = AngleWrap(absolutePointAngle - worldAngle_rad);


        double velocityAdjustedRelativePointAngle = AngleWrap(relativePointAngle -
                SpeedOmeter.currSlipAngle());

        //cambia la desaceleración dependiendo de que tan rapido vamos
        double decelerationDistance = Math.toRadians(40);


        double turnSpeed = (velocityAdjustedRelativePointAngle / decelerationDistance) * point_speed;


        movementTurn = Range.clip(turnSpeed, -point_speed, point_speed);

        if (distanceToPoint < 10) {
            movementTurn = 0;
        }

        allComponentsMinPower();


        //dar efecto dedeslize
        movementX *= Range.clip((relative_abs_x / 6.0), 0, 1);
        movementY *= Range.clip((relative_abs_y / 6.0), 0, 1);

        movementTurn *= Range.clip(Math.abs(relativePointAngle) / Math.toRadians(2), 0, 1);


        double errorTurnSoScaleDownMovement = Range.clip(1.0 - Math.abs(relativePointAngle / slowDownTurnRadians), 1.0 - slowDownMovementFromTurnError, 1);
        //no desacelerar si no giramos mientras la distancia es menor a 0
        if (Math.abs(movementTurn) < 0.00001) {
            errorTurnSoScaleDownMovement = 1;
        }
        movementX *= errorTurnSoScaleDownMovement;
        movementY *= errorTurnSoScaleDownMovement;

        return new MovementResult(relativePointAngle);
    }


    // va lo mas rapido posible manteniendose lo mas posible hacia adelante para mantener tiempo y ser masefectivo
    //baja la velocidad y para

    public static MovementResult pointAngle(double point_angle, double point_speed, double decelerationRadians) {

        double relativePointAngle = AngleWrap(point_angle - worldAngle_rad);
        double velocityAdjustedRelativePointAngle = AngleWrap(relativePointAngle - SpeedOmeter.currSlipAngle());

        double turnSpeed = (velocityAdjustedRelativePointAngle / decelerationRadians) * point_speed;

        movementTurn = Range.clip(turnSpeed, -point_speed, point_speed);

        allComponentsMinPower();

        //deslize
        movementTurn *= Range.clip(Math.abs(relativePointAngle) / Math.toRadians(3), 0, 1);

        return new MovementResult(relativePointAngle);
    }

    private static void allComponentsMinPower() {
        if (Math.abs(movementX) > Math.abs(movementY)) {
            if (Math.abs(movementX) > Math.abs(movementTurn)) {
                movementX = minPower(movementX, movementXMin);
            } else {
                movementTurn = minPower(movementTurn, movementTurnMin);
            }
        } else {
            if (Math.abs(movementY) > Math.abs(movementTurn)) {
                movementY = minPower(movementY, movementYMin);
            } else {
                movementTurn = minPower(movementTurn, movementTurnMin);
            }
        }
    }

    public static double minPower(double val, double min) {
        if (val >= 0 && val <= min) {
            return min;
        }
        if (val < 0 && val > -min) {
            return -min;
        }
        return val;
    }

    //Inicia
    public static void initCurve() {
        followCurveIndex = 0;
    }

    /**
     * follows a set of points, while maintaining a following distance
     */
    public static boolean followCurve(ArrayList<CurvePoint> allPoints, double followAngle, boolean allowSkipping) {

        for (int i = 0; i < allPoints.size() - 1; i++) {
            ComputerDebugging.sendLine(new Pose2D(allPoints.get(i).x, allPoints.get(i).y),
                    new Pose2D(allPoints.get(i + 1).x, allPoints.get(i + 1).y));
        }


        //extender el punto final
        ArrayList<CurvePoint> pathExtended = (ArrayList<CurvePoint>) allPoints.clone();

        //en que posicion estamos
        IndexedPoint clippedToPath = clipToPath(allPoints, worldXPosition, worldYPosition);
        int currFollowIndex = clippedToPath.getIndex() + 1;

        //calc el punto a seguir
        CurvePoint followMe = getFollowPointPath(pathExtended, worldXPosition, worldYPosition,
                allPoints.get(currFollowIndex).followDistance);


        //esto cambia el punto a ser extendido
        pathExtended.set(pathExtended.size() - 1,
                extendLine(allPoints.get(allPoints.size() - 2), allPoints.get(allPoints.size() - 1),
                        allPoints.get(allPoints.size() - 1).pointLength * 1.5));


        CurvePoint pointToMe = getFollowPointPath(pathExtended, worldXPosition, worldYPosition,
                allPoints.get(currFollowIndex).pointLength);


        double clipedDistToFinalEnd = Math.hypot(
                clippedToPath.getX() - allPoints.get(allPoints.size() - 1).x,
                clippedToPath.getY() - allPoints.get(allPoints.size() - 1).y);


        if (clipedDistToFinalEnd <= followMe.followDistance + 15 ||
                Math.hypot(worldXPosition - allPoints.get(allPoints.size() - 1).x,
                        worldYPosition - allPoints.get(allPoints.size() - 1).y) < followMe.followDistance + 15) {

            followMe.setPoint(allPoints.get(allPoints.size() - 1).toPoint());
        }


        ComputerDebugging.sendKeyPoint(new Pose2D(pointToMe.x, pointToMe.y));
        ComputerDebugging.sendKeyPoint(new Pose2D(followMe.x, followMe.y));


        gunToPosition(followMe.x, followMe.y, followAngle,
                followMe.moveSpeed, followMe.turnSpeed,
                followMe.slowDownTurnRadians, 0, true);

        double currFollowAngle = Math.atan2(pointToMe.y - worldYPosition, pointToMe.x - worldXPosition);

        currFollowAngle += subtractAngles(followAngle, Math.toRadians(90));

        MovementResult result = pointAngle(currFollowAngle, allPoints.get(currFollowIndex).turnSpeed, Math.toRadians(45));
        movementX *= 1 - Range.clip(Math.abs(result.turnDelta_rad) / followMe.slowDownTurnRadians, 0, followMe.slowDownTurnAmount);
        movementY *= 1 - Range.clip(Math.abs(result.turnDelta_rad) / followMe.slowDownTurnRadians, 0, followMe.slowDownTurnAmount);


        return clipedDistToFinalEnd < 10;//if we are less than 10 cm to the target, return true
    }

    private static CurvePoint extendLine(CurvePoint firstPoint, CurvePoint secondPoint, double distance) {


        //angulo
        double lineAngle = Math.atan2(secondPoint.y - firstPoint.y, secondPoint.x - firstPoint.x);
        //largo
        double lineLength = Math.hypot(secondPoint.x - firstPoint.x, secondPoint.y - firstPoint.y);
        //extender por1.5
        double extendedLineLength = lineLength + distance;

        CurvePoint extended = new CurvePoint(secondPoint);
        extended.x = Math.cos(lineAngle) * extendedLineLength + firstPoint.x;
        extended.y = Math.sin(lineAngle) * extendedLineLength + firstPoint.y;
        return extended;
    }

    public static IndexedPoint clipToPath(ArrayList<CurvePoint> pathPoints, double xPos, double yPos) {
        double closestClippedDistance = 10000000;//start this off rediculously high

        int closestClippedIndex = 0;


        Point clippedToLine = new Point();


        for (int i = 0; i < pathPoints.size() - 1; i++) {

            CurvePoint firstPoint = pathPoints.get(i);
            CurvePoint secondPoint = pathPoints.get(i + 1);

            Point currClippedToLine = clipToLine(firstPoint.x, firstPoint.y,
                    secondPoint.x, secondPoint.y, xPos, yPos);

            double distanceToClip = Math.hypot(xPos - currClippedToLine.x, yPos - currClippedToLine.y);


            if (distanceToClip < closestClippedDistance) {
                closestClippedDistance = distanceToClip;
                closestClippedIndex = i;
                clippedToLine = currClippedToLine;
            }
        }

        return new IndexedPoint(clippedToLine.x, clippedToLine.y, closestClippedIndex);
    }

    public static CurvePoint getFollowPointPath(ArrayList<CurvePoint> pathPoints,
                                                double xPos,
                                                double yPos,
                                                double followRadius) {


        IndexedPoint clippedToLine = clipToPath(pathPoints, xPos, yPos);
        int currFollowIndex = clippedToLine.getIndex();//this is the index of the first point on the path we are following


        CurvePoint followMe = new CurvePoint(pathPoints.get(currFollowIndex + 1));
        followMe.setPoint(new Point(clippedToLine.getX(), clippedToLine.getY()));

        //ir por todos los puntos de la interseccion
        for (int i = 0; i < pathPoints.size() - 1; i++) {
            CurvePoint startLine = pathPoints.get(i);
            CurvePoint endLine = pathPoints.get(i + 1);

            //interseccion con la linea
            ArrayList<Point> intersections =
                    MathUtils.lineCircleIntersection(xPos, yPos, followRadius,
                            startLine.x, startLine.y, endLine.x, endLine.y);

            double closestDistance = 1000000;
            for (Point thisIntersection : intersections) {

                double dist = Math.hypot(thisIntersection.x - pathPoints.get(pathPoints.size() - 1).x,
                        thisIntersection.y - pathPoints.get(pathPoints.size() - 1).y);


                if (dist < closestDistance) {
                    closestDistance = dist;
                    followMe.setPoint(thisIntersection);
                }
            }
        }


        return followMe;
    }

    public static Point clipToLine(double lineX1, double lineY1, double lineX2, double lineY2,
                                   double robotX, double robotY) {
        if (lineX1 == lineX2) {
            lineX1 = lineX2 + 0.01;//nah
        }
        if (lineY1 == lineY2) {
            lineY1 = lineY2 + 0.01;//nah
        }

        //slope de la linea
        double m1 = (lineY2 - lineY1) / (lineX2 - lineX1);
        //slope de la linea perpendicular
        double m2 = (lineX1 - lineX2) / (lineY2 - lineY1);

        double xClipedToLine = ((-m2 * robotX) + robotY + (m1 * lineX1) - lineY1) / (m1 - m2);
        double yClipedToLine = (m1 * (xClipedToLine - lineX1)) + lineY1;
        return new Point(xClipedToLine, yClipedToLine);
    }

    public static Point extendPointOnLine(double lineX1, double lineY1, double lineX2, double lineY2,
                                          double pointX, double pointY, double extendDist) {
        Point cliped = clipToLine(lineX1, lineY1, lineX2, lineY2, pointX, pointY);


        if (lineX1 == lineX2) {
            lineX1 += 0.0001;
        }
        if (lineY1 == lineY2) {
            lineY1 += 0.0001;
        }
        double angleLine = Math.atan2(lineY2 - lineY1, lineX2 - lineX1);


        //extiende la linea
        double xTarget = (Math.cos(angleLine) * extendDist) + cliped.x;
        double yTarget = (Math.sin(angleLine) * extendDist) + cliped.y;


        return new Point(xTarget, yTarget);
    }

    public static LinePoint pointAlongLine(double lineX1, double lineY1, double lineX2, double lineY2,
                                           double robotX, double robotY,
                                           double followDistance) {
        Point clipedToLine = clipToLine(lineX1, lineY1, lineX2, lineY2, robotX, robotY);


        double angleLine = Math.atan2(lineY2 - lineY1, lineX2 - lineX1);


        //Calcula el punto a seguir
        double xTarget = (Math.cos(angleLine) * followDistance) + clipedToLine.x;
        double yTarget = (Math.sin(angleLine) * followDistance) + clipedToLine.y;


        boolean pointIsOnLine = false;

        if ((xTarget > lineX2 && lineX2 < lineX1) || (xTarget < lineX2 && lineX2 > lineX1)) {
            pointIsOnLine = true;
        }

        return new LinePoint(xTarget, yTarget, pointIsOnLine);
    }
}
