package org.wint3794.debugger.graphics

import org.wint3794.debugger.geometry.Pose2d
import org.wint3794.debugger.util.Constants

object Screen {

    var centerPoint: Array<Double> = arrayOf(0.0, 0.0)
    var dimensions: Array<Double> = arrayOf(0.0, 0.0)

    val pixel
        get() = Constants.FIELD_SIZE / Math.max(dimensions[0], (dimensions[1]))

    val scale
        get() = Constants.FIELD_SIZE / pixel

    fun toScreen(point: Pose2d) : Pose2d {
        val windowScale = Pose2d(dimensions[0] * pixel, dimensions[1] * pixel)

        val relTopLeftX = point.x - (centerPoint[0] - windowScale.x / 2.0)
        val relTopLeftY = (centerPoint[1] + windowScale.y / 2.0) - point.y

        val percentX = relTopLeftX / windowScale.x
        val percentY = relTopLeftY / windowScale.y

        return Pose2d(percentX * dimensions[0], percentY * dimensions[1])
    }
}