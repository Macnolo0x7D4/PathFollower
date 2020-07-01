package org.wint3794.debugger.geometry

class Pose2d(var x: Double = 0.0, var y: Double = 0.0, var angle: Double = 0.0) {

    override fun toString(): String {
        return "Pose2d(x=$x, y=$y, angle=$angle)"
    }

}