package org.wint3794.pathfollower.util

import org.wint3794.pathfollower.controllers.Robot

object SpeedOmeter {
    private var lastUpdateStartTime: Long = 0
    /**gets relative y speed in cm/s */
    var speedY = 0.0
        private set
    /**gets relative x speed = cm/s */
    var speedX = 0.0
        private set

    //min time between updates to make sure our speed is accurate
    var timeBetweenUpdates = 25
    var yDistTraveled = 0.0
    var xDistTraveled = 0.0
    var lastAngle = 0.0
    var radPerSecond = 0.0

    //calculates our current velocity every update
    fun update() {
        val currTime = System.currentTimeMillis()

        //return if no change in telemetry
        if (Math.abs(yDistTraveled) < 0.000000001 && Math.abs(xDistTraveled) < 0.000000001 && Math.abs(radPerSecond) < 0.000001
        ) {
            return
        }
        if (currTime - lastUpdateStartTime > timeBetweenUpdates) {
            //elapsedTime in seconds
            val elapsedTime = (currTime - lastUpdateStartTime).toDouble() / 1000.0
            val speedY = yDistTraveled / elapsedTime
            val speedX = xDistTraveled / elapsedTime
            if (speedY < 200 && speedX < 200) { //I can assure you our robot can't go 200 m/s
                this.speedY = speedY
                this.speedX = speedX
            }
            radPerSecond =
                MathFunctions.roundAngle(Robot.Companion.worldAngle - lastAngle) / elapsedTime
            lastAngle = Robot.Companion.worldAngle
            yDistTraveled = 0.0
            xDistTraveled = 0.0
            lastUpdateStartTime = currTime
        }
    }

    val degPerSecond: Double
        get() = Math.toDegrees(radPerSecond)

    var scalePrediction = 1.0

    //amount robot slips (cm) while going forwards 1 centimeter per second
    var ySlipDistanceFor1CMPS = 0.14 * scalePrediction //0.169;
    var xSlipDistanceFor1CMPS = 0.153 * scalePrediction //0.117;

    //radians the robot slips when going 1 radian per second
    var turnSlipAmountFor1RPS = 0.09 * scalePrediction //0.113;

    /** Gives the current distance (cm) the robot would slip if power is set to 0  */
    fun currSlipDistanceY(): Double {
        return speedY * ySlipDistanceFor1CMPS
    }

    fun currSlipDistanceX(): Double {
        return speedX * xSlipDistanceFor1CMPS
    }

    /** Gives the number of radians the robot would turn if power was cut now */
    fun currSlipAngle(): Double {
        return radPerSecond * turnSlipAmountFor1RPS
    }
}