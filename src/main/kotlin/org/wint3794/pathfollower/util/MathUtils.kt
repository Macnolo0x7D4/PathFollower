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
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

object MathUtils {
    /**
     * Returns rounded value to established number of places (Utilities.roundPower).
     *
     * @param value Raw value
     * @return Rounded value
     */
    fun roundPower(value: Double): Double {
        return roundPower(value, Constants.ROUND_POWER)
    }

    /**
     * Returns rounded value to established number of places (Utilities.roundPower).
     *
     * @param value Raw value
     * @param places Number of places to round
     * @return Rounded value
     */
    fun roundPower(value: Double, places: Int): Double {
        val factor = 10.0.pow(places.toDouble()).toLong()
        return (value * factor).roundToInt().toDouble() / factor
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

    fun roundAngle(angle: Double): Double {
        var processedAngle = angle

        while (processedAngle < -Math.PI) {
            processedAngle += 2 * Math.PI
        }
        while (processedAngle > Math.PI) {
            processedAngle -= 2 * Math.PI
        }
        return processedAngle
    }

    fun getIntersection(
        collisionBoxCenter: Point,
        radius: Double,
        linePoint1: Point,
        linePoint2: Point
    ): ArrayList<Point> {
        if (abs(linePoint1.y - linePoint2.y) < 0.003) {
            linePoint1.y = linePoint2.y + 0.003
        }

        if (abs(linePoint1.x - linePoint2.x) < 0.003) {
            linePoint1.x = linePoint2.x + 0.003
        }

        val m1 = (linePoint2.y - linePoint1.y) / (linePoint2.x - linePoint1.x)

        val quadraticA = 1.0 + m1.pow(2.0)

        val x1 = linePoint1.x - collisionBoxCenter.x
        val y1 = linePoint1.y - collisionBoxCenter.y

        val quadraticB = 2.0 * m1 * y1 - 2.0 * m1.pow(2.0) * x1

        val quadraticC =
            m1.pow(2.0) * x1.pow(2.0) - 2.0 * y1 * m1 * x1 + y1.pow(2.0) - radius.pow(2.0)

        val allPoints = ArrayList<Point>()

        val a = quadraticB.pow(2.0) - 4 * quadraticA * quadraticC

        var xRoot1 = (-quadraticB + sqrt(a)) / (2.0 * quadraticA)
        var yRoot1 = m1 * (xRoot1 - x1) + y1

        xRoot1 += collisionBoxCenter.x
        yRoot1 += collisionBoxCenter.y

        val minX = linePoint1.x.coerceAtMost(linePoint2.x)
        val maxX = linePoint1.x.coerceAtLeast(linePoint2.x)

        // if (xRoot1 > minX && xRoot1 < maxX) {
        if (xRoot1 in minX..maxX) {
            allPoints.add(Point(xRoot1, yRoot1))
        }

        var xRoot2 = (-quadraticB - sqrt(a)) / (2.0 * quadraticA)
        var yRoot2 = m1 * (xRoot2 - x1) + y1

        xRoot2 += collisionBoxCenter.x
        yRoot2 += collisionBoxCenter.y

        // if (xRoot2 > minX && xRoot2 < maxX) {
        if (xRoot2 in minX..maxX) {
            allPoints.add(Point(xRoot2, yRoot2))
        }

        return allPoints
    }
}