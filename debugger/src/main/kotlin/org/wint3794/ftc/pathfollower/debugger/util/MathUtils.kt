package org.wint3794.ftc.pathfollower.debugger.util

object MathUtils {
    fun roundAngle(angle: Double): Double {
        var processedAngle = angle
        while (processedAngle < -Math.PI) {
            processedAngle += 2.0 * Math.PI
        }
        while (processedAngle > Math.PI) {
            processedAngle -= 2.0 * Math.PI
        }
        return processedAngle
    }
}