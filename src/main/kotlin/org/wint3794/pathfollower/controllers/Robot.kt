package org.wint3794.pathfollower.controllers

import org.wint3794.pathfollower.util.MovementVars
import org.wint3794.pathfollower.util.Range
import org.wint3794.pathfollower.util.SpeedOmeter

class Robot {
    constructor(x: Double, y: Double, angle: Double) {
        xPos = x
        yPos = y
        worldAngle = angle
    }

    constructor(ignored: Follower?) {
        xPos = 0.0
        yPos = 0.0
        worldAngle = 0.0
    }

    //last update time
    private var lastUpdateTime: Long = 0

    /**
     * Calculates the change in position of the robot
     */
    fun update() {
        //tiempo
        val currentTimeMillis = System.currentTimeMillis()
        val elapsedTime = (currentTimeMillis - lastUpdateTime) / 1000.0
        lastUpdateTime = currentTimeMillis
        if (elapsedTime > 1) {
            return
        }


        //incrementa la posicion
        val totalSpeed = Math.hypot(
            xSpeed,
            ySpeed
        )
        val angle = Math.atan2(
            ySpeed,
            xSpeed
        ) - Math.toRadians(90.0)
        val outputAngle = worldAngle + angle
        xPos += totalSpeed * Math.cos(
            outputAngle
        ) * elapsedTime * 1000 * 0.2
        yPos += totalSpeed * Math.sin(
            outputAngle
        ) * elapsedTime * 1000 * 0.2
        worldAngle += MovementVars.movementTurn * elapsedTime * 20 / (2 * Math.PI)
        xSpeed += Range.clip(
            (MovementVars.movementX - xSpeed) / 0.2,
            -1.0,
            1.0
        ) * elapsedTime
        ySpeed += Range.clip(
            (MovementVars.movementY - ySpeed) / 0.2,
            -1.0,
            1.0
        ) * elapsedTime
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
        var usingComputer = true
        var xSpeed = 0.0
        var ySpeed = 0.0
        var turnSpeed = 0.0
        var xPos: Double = 0.0
        var yPos: Double = 0.0
        var worldAngle: Double = 0.0
    }
}