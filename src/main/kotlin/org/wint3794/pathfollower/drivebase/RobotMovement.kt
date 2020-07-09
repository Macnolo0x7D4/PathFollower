package org.wint3794.pathfollower.drivebase

import org.wint3794.pathfollower.controllers.Robot
import org.wint3794.pathfollower.debug.telemetries.SimulatorSender
import org.wint3794.pathfollower.geometry.CurvePoint
import org.wint3794.pathfollower.geometry.Point
import org.wint3794.pathfollower.util.MathUtils
import org.wint3794.pathfollower.util.MovementVars
import org.wint3794.pathfollower.util.Range
import kotlin.math.*

object RobotMovement {
    fun followCurve(allPoints: List<CurvePoint>, followAngle: Double) {
        for (i in 0 until allPoints.size - 1) {
            SimulatorSender.sendLine(
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

        SimulatorSender.sendKeyPoint(Point(followMe.x, followMe.y))

        val start = allPoints.first()
        val end = allPoints[allPoints.size - 1]

        if (abs(end.x - Robot.xPos) < start.followDistance && abs(
                end.y - Robot.yPos
            ) < start.followDistance
        ) {
            MovementVars.movementX = 0.0
            MovementVars.movementY = 0.0
            MovementVars.movementTurn = 0.0
        } else {
            moveToPosition(followMe.x, followMe.y, followMe.moveSpeed, followAngle, followMe.turnSpeed)
        }
    }

    private fun getFollowPointPath(
        pathPoints: List<CurvePoint>,
        robotLocation: Point,
        followRadius: Double
    ): CurvePoint {
        val followMe = pathPoints[0].clone()

        for (i in 0 until pathPoints.size - 1) {
            val startLine = pathPoints[i]
            val endLine = pathPoints[i + 1]
            val intersections =
                MathUtils.getIntersection(robotLocation, followRadius, startLine.toPoint(), endLine.toPoint())
            var closestAngle = 100000000.0
            for (thisIntersection in intersections) {
                val angle = atan2(
                    thisIntersection.y - Robot.yPos,
                    thisIntersection.x - Robot.yPos
                )
                val deltaAngle =
                    abs(MathUtils.roundAngle(angle - Robot.worldAngle))
                if (deltaAngle < closestAngle) {
                    closestAngle = deltaAngle
                    followMe.setPoint(thisIntersection)
                }
            }
        }
        return followMe
    }

    private fun moveToPosition(
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
        val relativeAngleToTarget = MathUtils.roundAngle(
            relativeAngle - (Robot.worldAngle - Math.toRadians(
                90.0
            ))
        )
        val relativeX = cos(relativeAngleToTarget) * hypotenuse
        val relativeY = sin(relativeAngleToTarget) * hypotenuse
        val relativeTurnAngle =
            relativeAngleToTarget - Math.toRadians(180.0) + preferredTurnAngle
        val relativePositionSum = abs(relativeX) + abs(relativeY)

        MovementVars.movementX = relativeX / relativePositionSum * Speed
        MovementVars.movementY = relativeY / relativePositionSum * Speed
        MovementVars.movementTurn = if (hypotenuse > 15) Range.clip(
            relativeTurnAngle / Math.toRadians(30.0), -1.0, 1.0
        ) * turnSpeed else 0.0
    }
}