package org.wint3794.pathfollower.drivebase;

import org.wint3794.pathfollower.debug.ComputerDebugging;
import org.wint3794.pathfollower.geometry.CurvePoint;
import org.wint3794.pathfollower.util.MathFunctions;
import org.wint3794.pathfollower.util.Range;
import org.wint3794.pathfollower.geometry.Point;
import org.wint3794.pathfollower.util.MovementVars;

import java.util.ArrayList;
import java.util.List;

import static org.wint3794.pathfollower.controllers.Robot.*;

public class RobotMovement {

    public static void followCurve(List<CurvePoint> allPoints, double followAngle){
        for (int i=0; i< allPoints.size()-1; i++){
            ComputerDebugging.sendLine(new Point(allPoints.get(i).x,allPoints.get(i).y ), new Point(allPoints.get(i+1).x, allPoints.get(i+1).y));
        }

        CurvePoint followMe = getFollowPointPath(allPoints, new Point(worldXPosition, worldYPosition),allPoints.get(0).followDistance);

        ComputerDebugging.sendKeyPoint(new Point(followMe.x, followMe.y));

        final CurvePoint end = allPoints.get(allPoints.size() - 1);

        if (Math.abs(end.x - worldXPosition) < 30 && Math.abs(end.y - worldYPosition) < 30) {
            MovementVars.movementX = 0;
            MovementVars.movementY = 0;
        } else {
            moveToPosition(followMe.x, followMe.y, followMe.moveSpeed, followAngle, followMe.turnSpeed);
        }
    }

    public static CurvePoint getFollowPointPath(List<CurvePoint> pathPoints,Point robotLocation, double followRadius){
        CurvePoint followMe = new CurvePoint(pathPoints.get(0));


        for (int i=0; i< pathPoints.size() -1; i ++){
            CurvePoint startLine = pathPoints.get(i);
            CurvePoint endline = pathPoints.get(i+1);

            ArrayList<Point> intersections = MathFunctions.getIntersection(robotLocation, followRadius, startLine.toPoint(), endline.toPoint() );

            double closestAngle = 100000000;

            for(Point thisIntersection : intersections){
                double angle = Math.atan2(thisIntersection.y - worldYPosition, thisIntersection.x- worldYPosition);
                double deltaAngle = Math.abs(MathFunctions.roundAngle(angle- worldAngle));

                if (deltaAngle < closestAngle){
                    closestAngle= deltaAngle;
                    followMe.setPoint(thisIntersection);
                }
            }



        }
        return followMe;
    }


    public static  void moveToPosition(double x, double y, double Speed, double preferredTurnAngle, double turnSpeed){
        final double absX = x - worldXPosition;
        final double absY = y - worldYPosition;

        final double hypotenuse = Math.hypot(absX, absY);
        final double relativeAngle = Math.atan2(absY, absX);
        final double relativeAngleToTarget = MathFunctions.roundAngle(relativeAngle - (worldAngle - Math.toRadians(90)));


        final double relativeX = Math.cos(relativeAngleToTarget) * hypotenuse;
        final double relativeY = Math.sin(relativeAngleToTarget) * hypotenuse;
        final double relativeTurnAngle  = relativeAngleToTarget - Math.toRadians(180) + preferredTurnAngle;

        final double relativePositionSum = Math.abs(relativeX) + Math.abs(relativeY);


        MovementVars.movementX = relativeX / relativePositionSum * Speed;
        MovementVars.movementY = relativeY / relativePositionSum * Speed;
        MovementVars.movementTurn = hypotenuse > 15 ? Range.clip(relativeTurnAngle / Math.toRadians(30), -1, 1) * turnSpeed : 0;
    }
}
