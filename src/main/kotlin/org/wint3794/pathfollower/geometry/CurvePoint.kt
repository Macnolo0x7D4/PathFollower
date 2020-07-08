package org.wint3794.pathfollower.geometry

class CurvePoint : Point {
    var moveSpeed = 0.0
    var turnSpeed = 0.0
    var followDistance = 0.0
    var slowDownTurnRadians = 0.0
    var slowDownTurnAmount = 0.0
    var pointLength = 0.0

    constructor(
        x: Double, y: Double, moveSpeed: Double, turnSpeed: Double,
        followDistance: Double, slowDownTurnRadians: Double, slowDownTurnAmount: Double
    ) {
        this.x = x
        this.y = y
        this.moveSpeed = moveSpeed
        this.turnSpeed = turnSpeed
        this.followDistance = followDistance
        pointLength = followDistance
        this.slowDownTurnRadians = slowDownTurnRadians
        this.slowDownTurnAmount = slowDownTurnAmount
    }

    constructor(point: Point) {
        x = point.x
        y = point.y
    }

    constructor(
        x: Double,
        y: Double,
        moveSpeed: Double,
        turnSpeed: Double,
        followDistance: Double,
        pointLength: Double,
        slowDownTurnRadians: Double,
        slowDownTurnAmount: Double
    ) : this(x, y, moveSpeed, turnSpeed, followDistance, slowDownTurnRadians, slowDownTurnAmount) {
        this.pointLength = pointLength
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

    fun setPoint(p: Point?) {
        x = p!!.x
        y = p.y
    }
}