package org.wint3794.pathfollower.geometry

import java.util.*

class PiecewiseFunction {
    var m_points = ArrayList<CurvePoint>()

    constructor(visualString: String) {
        //go through the string from bottom left to top right (kinda weird) assuming it's 20 by 9 chars
        var iter = visualString.length - 20 //this will be the bottom left
        while (true) {
            if (visualString[iter] == '1') {
                val row =
                    iter / 20 //since lines are in groups of 20 chars, devide i by 20 and truncate decimals (int)
                val col = iter - row * 20
                val c = Point(
                    col.toDouble() / 19.0,
                    1.0 - row.toDouble() / 8.0
                )
                m_points.add(CurvePoint(c))
            }
            if (iter % 20 == 19) {
                iter -= 40
            }
            iter++
            if (iter < 0) {
                break
            }
        }
    }

    //if you want to manually set the points
    constructor(points: ArrayList<CurvePoint>) {
        m_points = points
    }

    //to use this function
    fun getVal(x: Double): Double {
        var x1 = 0.0
        var y1 = 0.0
        var x2 = 0.0
        var y2 = 0.0
        for (i in 0 until m_points.size - 1) {
            if (x >= m_points[i].x && x < m_points[i + 1].x) {
                x1 = m_points[i].x
                y1 = m_points[i].y
                x2 = m_points[i + 1].x
                y2 = m_points[i + 1].y
            }
        }
        //slope is change in y over change in x
        val slope: Double = if (x2 - x1 != 0.0) (y2 - y1) / (x2 - x1) else 100000.0 //avoiding a divide by 0
        /*
        To find y intercept use point-slope:
        y - y1 = m(x - x1)
        y - y1 = slope(0-x1)
        y = slope(-x1) + y1
        */
        val yintercept = slope * -x1 + y1
        return x * slope + yintercept //plug the x value into our equation
    }
}