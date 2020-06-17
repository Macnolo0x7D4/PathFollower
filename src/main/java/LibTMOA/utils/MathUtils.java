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

package LibTMOA.utils;

import LibTMOA.models.structures.Pose2D;

import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MathUtils {
    /**
     * Returns rounded value to established number of places (Utilities.roundPower).
     *
     * @param value Raw value
     * @return Rounded value
     */
    public static double roundPower(double value) {
        long factor = (long) Math.pow(10, Constants.ROUND_POWER);
        value = value * factor;
        return (double) Math.round(value) / factor;
    }

    /**
     * Returns value clamped between low and high boundaries.
     *
     * @param value Value to clamp.
     * @param low   The lower boundary to which to clamp value.
     * @param high  The higher boundary to which to clamp value.
     */
    public static int clamp(int value, int low, int high) {
        return Math.max(low, Math.min(value, high));
    }

    /**
     * Returns value clamped between low and high boundaries.
     *
     * @param value Value to clamp.
     * @param low   The lower boundary to which to clamp value.
     * @param high  The higher boundary to which to clamp value.
     */
    public static double clamp(double value, double low, double high) {
        return Math.max(low, Math.min(value, high));
    }

    /**
     * Returns a Value mapped.
     *
     * @param x       Value to map.
     * @param in_min  The min value of entry map.
     * @param in_max  The max value of entry map.
     * @param out_min The min value of output map.
     * @param out_max The max value of output map.
     * @return Value mapped.
     */
    public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }


    /**
     * Takes intersection of two lines defined by one point and their slopes
     */
    public static Pose2D lineIntersecion(Pose2D point1, double m1, Pose2D point2, double m2) {
        double xIntercept = ((-m2 * point2.getX()) + point2.getY() + (m1 * point1.getX()) - point1.getY()) / (m1 - m2);//solves for the x pos of the intercept
        double yIntercept = m1 * (xIntercept - point1.getX()) + point1.getY();//plug into any equation to get y

        return new Pose2D(xIntercept, yIntercept);
    }

    /**
     * Finds the intersection of a line segment and a circle
     *
     * @param circleX x position of the circle
     * @param circleY y position of the circle
     * @param r:      radius of the circle
     * @param lineX1  first x position of the line
     * @param lineY1  first y position of the line
     * @param lineX2  second x position of the line
     * @param lineY2  second y position of the line
     * @return an Array of intersections
     */
    public static ArrayList<Point> lineCircleIntersection(double circleX, double circleY, double r,
                                                          double lineX1, double lineY1,
                                                          double lineX2, double lineY2) {
        //make sure the points don't exactly line up so the slopes work
        if (Math.abs(lineY1 - lineY2) < 0.003) {
            lineY1 = lineY2 + 0.003;
        }
        if (Math.abs(lineX1 - lineX2) < 0.003) {
            lineX1 = lineX2 + 0.003;
        }

        //calculate the slope of the line
        double m1 = (lineY2 - lineY1) / (lineX2 - lineX1);

        //the first coefficient in the quadratic
        double quadraticA = 1.0 + Math.pow(m1, 2);

        //shift one of the line's points so it is relative to the circle
        double x1 = lineX1 - circleX;
        double y1 = lineY1 - circleY;


        //the second coefficient in the quadratic
        double quadraticB = (2.0 * m1 * y1) - (2.0 * Math.pow(m1, 2) * x1);

        //the third coefficient in the quadratic
        double quadraticC = ((Math.pow(m1, 2) * Math.pow(x1, 2)) - (2.0 * y1 * m1 * x1) + Math.pow(y1, 2) - Math.pow(r, 2));


        ArrayList<Point> allPoints = new ArrayList<>();


        //this may give an error so we use a try catch
        try {
            //now solve the quadratic equation given the coefficients
            double xRoot1 = (-quadraticB + Math.sqrt(Math.pow(quadraticB, 2) - (4.0 * quadraticA * quadraticC))) / (2.0 * quadraticA);

            //we know the line equation so plug into that to get root
            double yRoot1 = m1 * (xRoot1 - x1) + y1;


            //now we can add back in translations
            xRoot1 += circleX;
            yRoot1 += circleY;

            //make sure it was within range of the segment
            double minX = Math.min(lineX1, lineX2);
            double maxX = Math.max(lineX1, lineX2);

            if (xRoot1 > minX && xRoot1 < maxX) {
                allPoints.add(new Point(xRoot1, yRoot1));
            }

            //do the same for the other root
            double xRoot2 = (-quadraticB - Math.sqrt(Math.pow(quadraticB, 2) - (4.0 * quadraticA * quadraticC))) / (2.0 * quadraticA);

            double yRoot2 = m1 * (xRoot2 - x1) + y1;
            //now we can add back in translations
            xRoot2 += circleX;
            yRoot2 += circleY;

            //make sure it was within range of the segment
            if (xRoot2 > minX && xRoot2 < maxX) {
                allPoints.add(new Point(xRoot2, yRoot2));
            }
        } catch (Exception e) {
            //if there are no roots
        }
        return allPoints;
    }

    public static double AngleWrap(double angle) {
        while (angle < -Math.PI) {
            angle += 2 * Math.PI;
        }
        while (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    public static float AngleWrap(float angle) {
        while (angle < -Math.PI) {
            angle += 2 * Math.PI;
        }
        while (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    public static ArrayList<Point> lineCircleintersection(Point circlecenter, double radius, Point linePoint1, Point linePoint2) {
        if (Math.abs(linePoint1.y - linePoint2.y) < 0.003) {
            linePoint1.y = linePoint2.y + 0.003;

        }
        if (Math.abs(linePoint1.x - linePoint2.x) < 0.003) {
            linePoint1.x = linePoint2.x + 0.003;

        }
        double m1 = (linePoint2.y - linePoint1.y) / (linePoint2.x - linePoint1.x);
        double quadraticA = 1.0 + pow(m1, 2);

        double x1 = linePoint1.x - circlecenter.x;
        double y1 = linePoint1.y - circlecenter.y;


        double quadraticB = (2.0 * m1 * y1) - (2.0 * pow(m1, 2) * x1);

        double quadraticC = (pow(m1, 2) * pow(x1, 2)) - (2.0 * y1 * m1 * x1) + pow(y1, 2) - pow(radius, 2);

        ArrayList<Point> allPoints = new ArrayList<>();

        double xRoot1 = (-quadraticB + sqrt(pow(quadraticB, 2) - (4 * quadraticA * quadraticC))) / (2.0 * quadraticA);
        double yRoot1 = m1 * (xRoot1 - x1) + y1;

        xRoot1 += circlecenter.x;
        yRoot1 += circlecenter.y;

        double minX = Math.min(linePoint1.x, linePoint2.x);
        double maxX = Math.max(linePoint1.x, linePoint2.x);

        if (xRoot1 > minX && xRoot1 < maxX) {
            allPoints.add(new Point(xRoot1, yRoot1));
        }

        double xRoot2 = (-quadraticB - sqrt(pow(quadraticB, 2) - (4 * quadraticA * quadraticC))) / (2.0 * quadraticA);
        double yRoot2 = m1 * (xRoot2 - x1) + y1;

        xRoot2 += circlecenter.x;
        yRoot2 += circlecenter.y;

        if (xRoot2 > minX && xRoot2 < maxX) {
            allPoints.add(new Point(xRoot2, yRoot2));
        }

        return allPoints;

    }
}
