package org.wint3794.pathfollower.geometry

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

    constructor(point: Point) {
        x = point.x
        y = point.y
    }

    constructor(nextPoint: CurvePoint) {
        x = nextPoint.x
        y = nextPoint.y
        moveSpeed = nextPoint.moveSpeed
        turnSpeed = nextPoint.turnSpeed
        followDistance = nextPoint.followDistance
        slowDownTurnRadians = nextPoint.slowDownTurnRadians
        slowDownTurnAmount = nextPoint.slowDownTurnAmount
        pointLength = nextPoint.pointLength
    }

    fun toPoint(): Point {
        return Point(x, y)
    }

    fun setPoint(p: Point) {
        x = p.x
        y = p.y
    }
}