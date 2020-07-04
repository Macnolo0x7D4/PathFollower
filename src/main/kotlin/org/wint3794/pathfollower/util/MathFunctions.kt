package org.wint3794.pathfollower.util

import org.wint3794.pathfollower.geometry.Point
import java.util.*

object MathFunctions {
    fun roundAngle(angle: Double): Double {
        var angle = angle
        while (angle < -Math.PI) {
            angle += 2 * Math.PI
        }
        while (angle > Math.PI) {
            angle -= 2 * Math.PI
        }
        return angle
    }

    fun getIntersection(
        collisionBoxCenter: Point,
        radius: Double,
        linePoint1: Point?,
        linePoint2: Point?
    ): ArrayList<Point> {
        if (Math.abs(linePoint1!!.y - linePoint2!!.y) < 0.003) {
            linePoint1.y = linePoint2.y + 0.003
        }
        if (Math.abs(linePoint1.x - linePoint2.x) < 0.003) {
            linePoint1.x = linePoint2.x + 0.003
        }
        val m1 = (linePoint2.y - linePoint1.y) / (linePoint2.x - linePoint1.x)
        val quadraticA = 1.0 + Math.pow(m1, 2.0)
        val x1 = linePoint1.x - collisionBoxCenter.x
        val y1 = linePoint1.y - collisionBoxCenter.y
        val quadraticB = 2.0 * m1 * y1 - 2.0 * Math.pow(m1, 2.0) * x1
        val quadraticC =
            Math.pow(m1, 2.0) * Math.pow(x1, 2.0) - 2.0 * y1 * m1 * x1 + Math.pow(y1, 2.0) - Math.pow(radius, 2.0)
        val allPoints =
            ArrayList<Point>()
        try {
            val a = Math.pow(quadraticB, 2.0) - 4 * quadraticA * quadraticC
            var xRoot1 = (-quadraticB + Math.sqrt(a)) / (2.0 * quadraticA)
            var yRoot1 = m1 * (xRoot1 - x1) + y1
            xRoot1 += collisionBoxCenter.x
            yRoot1 += collisionBoxCenter.y
            val minX = Math.min(linePoint1.x, linePoint2.x)
            val maxX = Math.max(linePoint1.x, linePoint2.x)
            if (xRoot1 > minX && xRoot1 < maxX) {
                allPoints.add(Point(xRoot1, yRoot1))
            }
            var xRoot2 = (-quadraticB - Math.sqrt(a)) / (2.0 * quadraticA)
            var yRoot2 = m1 * (xRoot2 - x1) + y1
            xRoot2 += collisionBoxCenter.x
            yRoot2 += collisionBoxCenter.y
            if (xRoot2 > minX && xRoot2 < maxX) {
                allPoints.add(Point(xRoot2, yRoot2))
            }
        } catch (e: Exception) {
        }
        return allPoints
    }
}