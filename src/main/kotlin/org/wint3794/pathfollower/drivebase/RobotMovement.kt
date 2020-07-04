package org.wint3794.pathfollower.drivebase

import org.wint3794.pathfollower.controllers.Robot
import org.wint3794.pathfollower.debug.ComputerDebugging
import org.wint3794.pathfollower.geometry.CurvePoint
import org.wint3794.pathfollower.geometry.Point
import org.wint3794.pathfollower.util.MathFunctions
import org.wint3794.pathfollower.util.MovementVars
import org.wint3794.pathfollower.util.Range
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.hypot

object RobotMovement {
    fun followCurve(allPoints: List<CurvePoint>, followAngle: Double) {
        for (i in 0 until allPoints.size - 1) {
            ComputerDebugging.Companion.sendLine(
                Point(
                    allPoints[i].x,
                    allPoints[i].y
                ), Point(allPoints[i + 1].x, allPoints[i + 1].y)
            )
        }
        val followMe = getFollowPointPath(
            allPoints,
            Point(
                Robot.xPos,
                Robot.yPos
            ),
            allPoints[0].followDistance
        )
        ComputerDebugging.sendKeyPoint(Point(followMe.x, followMe.y))
        val end = allPoints[allPoints.size - 1]
        if (abs(end.x - Robot.xPos) < 30 && abs(
                end.y - Robot.yPos
            ) < 30
        ) {
            MovementVars.movementX = 0.0
            MovementVars.movementY = 0.0
        } else {
            moveToPosition(followMe.x, followMe.y, followMe.moveSpeed, followAngle, followMe.turnSpeed)
        }
    }

    fun getFollowPointPath(
        pathPoints: List<CurvePoint>,
        robotLocation: Point,
        followRadius: Double
    ): CurvePoint {
        val followMe = CurvePoint(pathPoints[0])
        for (i in 0 until pathPoints.size - 1) {
            val startLine = pathPoints[i]
            val endline = pathPoints[i + 1]
            val intersections =
                MathFunctions.getIntersection(robotLocation, followRadius, startLine.toPoint(), endline.toPoint())
            var closestAngle = 100000000.0
            for (thisIntersection in intersections) {
                val angle = atan2(
                    thisIntersection.y - Robot.yPos,
                    thisIntersection.x - Robot.yPos
                )
                val deltaAngle =
                    Math.abs(MathFunctions.roundAngle(angle - Robot.worldAngle))
                if (deltaAngle < closestAngle) {
                    closestAngle = deltaAngle
                    followMe.setPoint(thisIntersection)
                }
            }
        }
        return followMe
    }

    fun moveToPosition(
        x: Double,
        y: Double,
        Speed: Double,
        preferredTurnAngle: Double,
        turnSpeed: Double
    ) {
        val absX: Double = x - Robot.xPos
        val absY: Double = y - Robot.yPos
        val hypotenuse = hypot(absX, absY)
        val relativeAngle = atan2(absY, absX)
        val relativeAngleToTarget = MathFunctions.roundAngle(
            relativeAngle - (Robot.Companion.worldAngle - Math.toRadians(
                90.0
            ))
        )
        val relativeX = Math.cos(relativeAngleToTarget) * hypotenuse
        val relativeY = Math.sin(relativeAngleToTarget) * hypotenuse
        val relativeTurnAngle =
            relativeAngleToTarget - Math.toRadians(180.0) + preferredTurnAngle
        val relativePositionSum = Math.abs(relativeX) + Math.abs(relativeY)
        MovementVars.movementX = relativeX / relativePositionSum * Speed
        MovementVars.movementY = relativeY / relativePositionSum * Speed
        MovementVars.movementTurn = if (hypotenuse > 15) Range.clip(
            relativeTurnAngle / Math.toRadians(30.0), -1.0, 1.0
        ) * turnSpeed else 0.0
    }
}