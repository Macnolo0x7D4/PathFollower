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

package LibTMOA.movement.road;

import LibTMOA.math.road.FloatPoint;
import LibTMOA.server.ComputerDebugging;
import LibTMOA.utils.CurvePoint;
import LibTMOA.utils.Point;
import LibTMOA.utils.Range;

import java.util.ArrayList;
import java.util.List;

import static LibTMOA.controllers.Robot.*;
import static LibTMOA.robot.VariablesOfMovement.*;
import static LibTMOA.utils.MathUtils.AngleWrap;
import static LibTMOA.utils.MathUtils.lineCircleintersection;

public class RobotMovement {

    public static void followCurve(List<CurvePoint> allPoints, double followAngle) {
        for (int i = 0; i < allPoints.size() - 1; i++) {
            ComputerDebugging.sendLine(new FloatPoint(allPoints.get(i).x, allPoints.get(i).y), new FloatPoint(allPoints.get(i + 1).x, allPoints.get(i + 1).y));
        }

        CurvePoint followMe = getFollowPointPath(allPoints, new Point(worldXPosition, worldYPosition), allPoints.get(0).followDistance);

        ComputerDebugging.sendKeyPoint(new FloatPoint(followMe.x, followMe.y));

        MoveToPosition(followMe.x, followMe.y, followMe.moveSpeed, followAngle, followMe.turnSpeed);
    }

    public static CurvePoint getFollowPointPath(List<CurvePoint> pathPoints, Point robotLocation, double followRadius) {
        CurvePoint followMe = new CurvePoint(pathPoints.get(0));


        for (int i = 0; i < pathPoints.size() - 1; i++) {
            CurvePoint startLine = pathPoints.get(i);
            CurvePoint endline = pathPoints.get(i + 1);

            ArrayList<Point> intersections = lineCircleintersection(robotLocation, followRadius, startLine.toPoint(), endline.toPoint());

            double closestAngle = 100000000;

            for (Point thisIntersection : intersections) {
                double angle = Math.atan2(thisIntersection.y - worldYPosition, thisIntersection.x - worldYPosition);
                double deltaAngle = Math.abs(AngleWrap(angle - worldAngle_rad));

                if (deltaAngle < closestAngle) {
                    closestAngle = deltaAngle;
                    followMe.setPoint(thisIntersection);
                }
            }


        }
        return followMe;
    }


    public static void MoveToPosition(double x, double y, double Speed, double preferredTurnAngle, double turnSpeed) {
        double distanceToTarget = Math.hypot(x - worldXPosition, y - worldYPosition);
        double AbsoluteAngleToTarget = Math.atan2(y - worldYPosition, x - worldXPosition);
        double RelativeAngleToTarget = AngleWrap(AbsoluteAngleToTarget - (worldAngle_rad - Math.toRadians(90)));


        double relativeX = Math.cos(RelativeAngleToTarget) * distanceToTarget;
        double relativeY = Math.sin(RelativeAngleToTarget) * distanceToTarget;

        double MovementXPower = relativeX / (Math.abs(relativeX) + Math.abs(relativeY));
        double MovementYPower = relativeY / (Math.abs(relativeX) + Math.abs(relativeY));

        movement_x = MovementXPower * Speed;
        movement_y = MovementYPower * Speed;

        double relativeTurnAngle = RelativeAngleToTarget - Math.toRadians(180) + preferredTurnAngle;
        movement_turn = Range.clip(relativeTurnAngle / Math.toRadians(30), -1, 1) * turnSpeed;

        if (distanceToTarget < 10) {
            movement_turn = 0;
        }

    }
}
