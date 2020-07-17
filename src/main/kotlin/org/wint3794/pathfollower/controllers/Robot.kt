package org.wint3794.pathfollower.controllers

import org.wint3794.pathfollower.util.MovementVars
import org.wint3794.pathfollower.util.Range
import org.wint3794.pathfollower.util.SpeedOmeter
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class Robot(x: Double, y: Double, angle: Double) {
    private var lastUpdateTime: Long = 0

    init {
        xPos = x
        yPos = y
        worldAngle = angle
    }

    fun update() {
        val currentTimeMillis = System.currentTimeMillis()
        val elapsedTime = (currentTimeMillis - lastUpdateTime) / 1000.0
        lastUpdateTime = currentTimeMillis

        if (elapsedTime > 1) {
            return
        }

        val totalSpeed = hypot(xSpeed, ySpeed)

        val angle = atan2(ySpeed, xSpeed) - Math.toRadians(90.0)

        val outputAngle = worldAngle + angle

        xPos += totalSpeed * cos(outputAngle) * elapsedTime * 1000 * 0.2
        yPos += totalSpeed * sin(outputAngle) * elapsedTime * 1000 * 0.2
        worldAngle += MovementVars.movementTurn * elapsedTime * 20 / (2 * Math.PI)

        xSpeed += Range.clip((MovementVars.movementX - xSpeed) / 0.2, -1.0, 1.0) * elapsedTime
        ySpeed += Range.clip((MovementVars.movementY - ySpeed) / 0.2, -1.0, 1.0) * elapsedTime

        turnSpeed += Range.clip(
            (MovementVars.movementTurn - turnSpeed) / 0.2,
            -1.0,
            1.0
        ) * elapsedTime

        SpeedOmeter.yDistTraveled += ySpeed * elapsedTime * 1000
        SpeedOmeter.xDistTraveled += xSpeed * elapsedTime * 1000
        SpeedOmeter.update()

        xSpeed *= 1.0 - elapsedTime
        ySpeed *= 1.0 - elapsedTime
        turnSpeed *= 1.0 - elapsedTime
    }

    companion object {
        var xSpeed = 0.0
        var ySpeed = 0.0
        var turnSpeed = 0.0
        var xPos: Double = 0.0
        var yPos: Double = 0.0
        var worldAngle: Double = 0.0
    }
}