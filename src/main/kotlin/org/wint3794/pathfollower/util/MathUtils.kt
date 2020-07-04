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
package org.wint3794.pathfollower.util

import org.wint3794.pathfollower.geometry.Point
import org.wint3794.pathfollower.geometry.Pose2d
import java.util.*

object MathUtils {
    /**
     * Returns rounded value to established number of places (Utilities.roundPower).
     *
     * @param value Raw value
     * @return Rounded value
     */
    fun roundPower(value: Double): Double {
        var value = value
        val factor =
            Math.pow(10.0, Constants.ROUND_POWER.toDouble()).toLong()
        value = value * factor
        return Math.round(value).toDouble() / factor
    }

    /**
     * Returns rounded value to established number of places (Utilities.roundPower).
     *
     * @param value Raw value
     * @param places Number of places to round
     * @return Rounded value
     */
    fun roundPower(value: Double, places: Int): Double {
        var value = value
        val factor = Math.pow(10.0, places.toDouble()).toLong()
        value = value * factor
        return Math.round(value).toDouble() / factor
    }

    /**
     * Returns value clamped between low and high boundaries.
     *
     * @param value Value to clamp.
     * @param low   The lower boundary to which to clamp value.
     * @param high  The higher boundary to which to clamp value.
     */
    fun clamp(value: Int, low: Int, high: Int): Int {
        return Math.max(low, Math.min(value, high))
    }

    /**
     * Returns value clamped between low and high boundaries.
     *
     * @param value Value to clamp.
     * @param low   The lower boundary to which to clamp value.
     * @param high  The higher boundary to which to clamp value.
     */
    fun clamp(value: Double, low: Double, high: Double): Double {
        return Math.max(low, Math.min(value, high))
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
    fun map(
        x: Double,
        in_min: Double,
        in_max: Double,
        out_min: Double,
        out_max: Double
    ): Double {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min
    }

    /**
     * Takes intersection of two lines defined by one point and their slopes.
     * @param point1 First Point
     * @param m1
     * @param point2 Second Point
     * @param m2
     * @return New Point
     */
    fun lineIntersection(point1: Pose2d, m1: Double, point2: Pose2d, m2: Double): Pose2d {
        val xIntercept =
            (-m2 * point2.x + point2.y + m1 * point1.x - point1.y) / (m1 - m2) //solves for the x pos of the intercept
        val yIntercept = m1 * (xIntercept - point1.x) + point1.y
        return Pose2d(xIntercept, yIntercept)
    }

    /**
     * Finds the intersection of a line segment and a circle
     *
     * @param circleX x position of the circle
     * @param circleY y position of the circle
     * @param r       radius of the circle
     * @param lineX1  first x position of the line
     * @param lineY1  first y position of the line
     * @param lineX2  second x position of the line
     * @param lineY2  second y position of the line
     * @return an Array of intersections
     */
    fun lineCircleIntersection(
        circleX: Double, circleY: Double, r: Double,
        lineX1: Double, lineY1: Double,
        lineX2: Double, lineY2: Double
    ): ArrayList<Point> {
        //make sure the points don't exactly line up so the slopes work
        var lineX1 = lineX1
        var lineY1 = lineY1
        if (Math.abs(lineY1 - lineY2) < 0.003) {
            lineY1 = lineY2 + 0.003
        }
        if (Math.abs(lineX1 - lineX2) < 0.003) {
            lineX1 = lineX2 + 0.003
        }

        //calculate the slope of the line
        val m1 = (lineY2 - lineY1) / (lineX2 - lineX1)

        //the first coefficient in the quadratic
        val quadraticA = 1.0 + Math.pow(m1, 2.0)

        //shift one of the line's points so it is relative to the circle
        val x1 = lineX1 - circleX
        val y1 = lineY1 - circleY


        //the second coefficient in the quadratic
        val quadraticB = 2.0 * m1 * y1 - 2.0 * Math.pow(m1, 2.0) * x1

        //the third coefficient in the quadratic
        val quadraticC =
            Math.pow(m1, 2.0) * Math.pow(x1, 2.0) - 2.0 * y1 * m1 * x1 + Math.pow(
                y1,
                2.0
            ) - Math.pow(r, 2.0)
        val allPoints =
            ArrayList<Point>()


        //this may give an error so we use a try catch
        try {
            //now solve the quadratic equation given the coefficients
            var xRoot1 = (-quadraticB + Math.sqrt(
                Math.pow(
                    quadraticB,
                    2.0
                ) - 4.0 * quadraticA * quadraticC
            )) / (2.0 * quadraticA)

            //we know the line equation so plug into that to get root
            var yRoot1 = m1 * (xRoot1 - x1) + y1


            //now we can add back in translations
            xRoot1 += circleX
            yRoot1 += circleY

            //make sure it was within range of the segment
            val minX = Math.min(lineX1, lineX2)
            val maxX = Math.max(lineX1, lineX2)
            if (xRoot1 > minX && xRoot1 < maxX) {
                allPoints.add(Point(xRoot1, yRoot1))
            }

            //do the same for the other root
            var xRoot2 = (-quadraticB - Math.sqrt(
                Math.pow(
                    quadraticB,
                    2.0
                ) - 4.0 * quadraticA * quadraticC
            )) / (2.0 * quadraticA)
            var yRoot2 = m1 * (xRoot2 - x1) + y1
            //now we can add back in translations
            xRoot2 += circleX
            yRoot2 += circleY

            //make sure it was within range of the segment
            if (xRoot2 > minX && xRoot2 < maxX) {
                allPoints.add(Point(xRoot2, yRoot2))
            }
        } catch (e: Exception) {
            //if there are no roots
        }
        return allPoints
    }

    fun AngleWrap(angle: Double): Double {
        var angle = angle
        while (angle < -Math.PI) {
            angle += 2 * Math.PI
        }
        while (angle > Math.PI) {
            angle -= 2 * Math.PI
        }
        return angle
    }

    fun AngleWrap(angle: Float): Float {
        var angle = angle
        while (angle < -Math.PI) {
            angle += 2 * Math.PI.toFloat()
        }
        while (angle > Math.PI) {
            angle -= 2 * Math.PI.toFloat()
        }
        return angle
    }

    fun lineCircleintersection(
        circlecenter: Point,
        radius: Double,
        linePoint1: Point,
        linePoint2: Point
    ): ArrayList<Point> {
        if (Math.abs(linePoint1.y - linePoint2.y) < 0.003) {
            linePoint1.y = linePoint2.y + 0.003
        }
        if (Math.abs(linePoint1.x - linePoint2.x) < 0.003) {
            linePoint1.x = linePoint2.x + 0.003
        }
        val m1 = (linePoint2.y - linePoint1.y) / (linePoint2.x - linePoint1.x)
        val quadraticA = 1.0 + Math.pow(m1, 2.0)
        val x1 = linePoint1.x - circlecenter.x
        val y1 = linePoint1.y - circlecenter.y
        val quadraticB = 2.0 * m1 * y1 - 2.0 * Math.pow(m1, 2.0) * x1
        val quadraticC =
            Math.pow(m1, 2.0) * Math.pow(x1, 2.0) - 2.0 * y1 * m1 * x1 + Math.pow(y1, 2.0) - Math.pow(radius, 2.0)
        val allPoints =
            ArrayList<Point>()
        var xRoot1 =
            (-quadraticB + Math.sqrt(Math.pow(quadraticB, 2.0) - 4 * quadraticA * quadraticC)) / (2.0 * quadraticA)
        var yRoot1 = m1 * (xRoot1 - x1) + y1
        xRoot1 += circlecenter.x
        yRoot1 += circlecenter.y
        val minX = Math.min(linePoint1.x, linePoint2.x)
        val maxX = Math.max(linePoint1.x, linePoint2.x)
        if (xRoot1 > minX && xRoot1 < maxX) {
            allPoints.add(Point(xRoot1, yRoot1))
        }
        var xRoot2 =
            (-quadraticB - Math.sqrt(Math.pow(quadraticB, 2.0) - 4 * quadraticA * quadraticC)) / (2.0 * quadraticA)
        var yRoot2 = m1 * (xRoot2 - x1) + y1
        xRoot2 += circlecenter.x
        yRoot2 += circlecenter.y
        if (xRoot2 > minX && xRoot2 < maxX) {
            allPoints.add(Point(xRoot2, yRoot2))
        }
        return allPoints
    }
}