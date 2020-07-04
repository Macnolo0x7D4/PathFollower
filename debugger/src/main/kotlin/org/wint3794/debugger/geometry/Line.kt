package org.wint3794.debugger.geometry

class Line(var x1: Double = 0.0, var y1: Double = 0.0, var x2: Double = 0.0, var y2: Double = 0.0) {
    override fun toString(): String {
        return "Line(x1=$x1, y1=$y1, x2=$x2, y2=$y2)"
    }
}