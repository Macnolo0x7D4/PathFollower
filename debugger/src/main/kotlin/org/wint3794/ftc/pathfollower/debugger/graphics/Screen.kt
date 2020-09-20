package org.wint3794.ftc.pathfollower.debugger.graphics

import org.wint3794.ftc.pathfollower.debugger.geometry.Point
import org.wint3794.ftc.pathfollower.debugger.util.Constants

object Screen {
    private var centerXReal = 0.0
    private var centerYReal = 0.0

    var widthScreen = 1000.0
    var heightScreen = 1000.0

    fun setCenterPoint(centerX: Double, centerY: Double) {
        centerXReal = centerX
        centerYReal = centerY
    }

    fun setDimensionsPixels(width: Double, height: Double) {
        widthScreen = width
        heightScreen = height
    }

    fun convertToScreen(p: Point): Point {
        val topLeft =
            topLeftScreenRealPosition
        val relativeFromTopLeft =
            Point(p.x - topLeft.x, topLeft.y - p.y)
        val percentX = relativeFromTopLeft.x / windowSizeInRealScale.x
        val percentY = relativeFromTopLeft.y / windowSizeInRealScale.y
        return Point(
            percentX * widthScreen,
            percentY * heightScreen
        )
    }

    private val topLeftScreenRealPosition: Point
        get() {
            val windowSizeReal =
                windowSizeInRealScale
            return Point(
                centerXReal - windowSizeReal.x / 2.0,
                centerYReal + windowSizeReal.y / 2.0
            )
        }

    private val windowSizeInRealScale: Point
        get() = Point(
            widthScreen * centimetersPerPixel,
            heightScreen * centimetersPerPixel
        )

    val centimetersPerPixel: Double
        get() = Constants.FIELD_SIZE / fieldSizePixels

    val fieldSizePixels: Double
        get() {
            val biggestWindowDimensionPixels = if (heightScreen > widthScreen) heightScreen else widthScreen
            return biggestWindowDimensionPixels / Constants.ZOOM
        }
}