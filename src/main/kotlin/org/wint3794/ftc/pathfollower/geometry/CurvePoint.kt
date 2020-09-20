package org.wint3794.ftc.pathfollower.geometry

/**
 * A class for mark points in virtual path
 */
data class CurvePoint(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var moveSpeed: Double = 0.0,
    var turnSpeed: Double = 0.0,
    var followDistance: Double = 0.0,
    var slowDownTurnRadians: Double = 0.0,
    var slowDownTurnAmount: Double = 0.0,
    var pointLength: Double = 0.0
) {

    fun toPoint(): Point {
        return Point(x, y)
    }

    fun setPoint(p: Point): CurvePoint {
        x = p.x
        y = p.y
        return this
    }

    fun clone(): CurvePoint {
        return CurvePoint(
            x,
            y,
            moveSpeed,
            turnSpeed,
            followDistance,
            slowDownTurnRadians,
            slowDownTurnAmount,
            pointLength
        )

    }
}