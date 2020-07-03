package org.wint3794.pathfollower.util;

import org.wint3794.pathfollower.geometry.Point;
import java.util.ArrayList;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MathFunctions {
    public static double roundAngle(double angle ){
        while (angle < -Math.PI){
            angle += 2*Math.PI;
        }
        while (angle > Math.PI){
            angle-=  2*Math.PI;
        }
        return angle;
    }

    public static ArrayList<Point> getIntersection(Point collisionBoxCenter, double radius, Point linePoint1, Point linePoint2){
        if (Math.abs(linePoint1.y- linePoint2.y )< 0.003){
            linePoint1.y= linePoint2.y + 0.003;

        }
        if (Math.abs(linePoint1.x - linePoint2.x)<0.003){
            linePoint1.x = linePoint2.x +0.003;

        }
        double m1= (linePoint2.y- linePoint1.y) / (linePoint2.x - linePoint1.x);
        double quadraticA = 1.0 + pow(m1,2);

        double x1= linePoint1.x -collisionBoxCenter.x;
        double y1 = linePoint1.y - collisionBoxCenter.y;


        double quadraticB = (2.0 *m1*y1)-  (2.0 * pow(m1,2)*x1);

        double quadraticC = (pow(m1,2) * pow(x1,2 ))-(2.0*y1*m1*x1)+ pow(y1,2) - pow(radius,2);

        ArrayList <Point> allPoints = new ArrayList<>();

        try {
            final double a = pow(quadraticB, 2) - (4 * quadraticA * quadraticC);
            double xRoot1 = (-quadraticB + sqrt(a)) / (2.0*quadraticA);
            double yRoot1 = m1* (xRoot1-x1)+y1 ;

            xRoot1 += collisionBoxCenter.x;
            yRoot1 += collisionBoxCenter.y;

            double minX = Math.min(linePoint1.x, linePoint2.x);
            double maxX = Math.max(linePoint1.x, linePoint2.x);

            if(xRoot1 > minX && xRoot1< maxX){
                allPoints.add(new Point (xRoot1,yRoot1));
            }

            double xRoot2 = (-quadraticB - sqrt(a)) / (2.0*quadraticA);
            double yRoot2 = m1 * (xRoot2-x1)+y1;

            xRoot2 += collisionBoxCenter.x;
            yRoot2 += collisionBoxCenter.y;

            if(xRoot2 > minX && xRoot2< maxX){
                allPoints.add(new Point (xRoot2,yRoot2));
            }
        } catch (Exception e){

        }
        return allPoints;

    }
}
