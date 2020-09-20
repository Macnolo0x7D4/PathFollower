package org.wint3794.ftc.pathfollower.debugger.util

import org.wint3794.ftc.pathfollower.debugger.geometry.Line
import org.wint3794.ftc.pathfollower.debugger.geometry.Point
import org.wint3794.ftc.pathfollower.debugger.graphics.App
import java.util.*
import kotlin.math.hypot

class CommandProcessor {
    var debugPoints = ArrayList<Point>()
    var debugLines = ArrayList<Line>()

    fun processMessage(receivedMessage: String) {
        val commands = receivedMessage.split("%".toRegex()).toTypedArray()

        for (command in commands) {
            val splitString = command.split(",".toRegex()).toTypedArray()

            when(splitString[0]) {
                "ROBOT" -> processRobotLocation(splitString)
                "P" -> processPoint(splitString)
                "LINE" -> processLine(splitString)
                "LP" -> addPoint(splitString)
                "CLEARLOG" -> pointLog.clear()
                else -> {
                    if (splitString[0].length >= 5) {
                        if (splitString[0].substring(0, 5) == "CLEAR") {
                            clear()
                        }
                    }
                }

            }
        }
    }

    private fun processRobotLocation(splitString: Array<String>) {
        if (splitString.size != 4) {
            return
        }
        lastRobotX =
            robotX
        lastRobotY =
            robotY
        lastRobotAngle =
            robotAngle
        robotX = splitString[1].toDouble()
        robotY = splitString[2].toDouble()
        robotAngle = splitString[3].toDouble()
        lastElapsedTime = System.currentTimeMillis() - lastTimeUpdate
        lastTimeUpdate = System.currentTimeMillis()
    }

    private fun processPoint(splitString: Array<String>) {
        if (splitString.size != 3) {
            return
        }

        debugPoints.add(
            Point(
                splitString[1].toDouble(),
                splitString[2].toDouble()
            )
        )
    }

    private fun addPoint(splitString: Array<String>) {
        if (splitString.size != 3) {
            return
        }
        val point = Point(
            splitString[1].toDouble(),
            splitString[2].toDouble()
        )

        var alreadyExists = false

        for (p in pointLog) {
            if (hypot(p.x - point.x, p.y - point.y) < 1.5) {
                alreadyExists = true
            }
        }

        if (!alreadyExists) {
            pointLog.add(point)
        }
    }

    private fun processLine(splitString: Array<String>) {
        if (splitString.size != 5) {
            return
        }
        debugLines.add(
            Line(
                splitString[1].toDouble(),
                splitString[2].toDouble(),
                splitString[3].toDouble(),
                splitString[4].toDouble()
            )
        )
    }

    private fun clear() {
        val currTime = System.currentTimeMillis()

        elapsedMillisThisUpdate = currTime - lastClearTime.toDouble()
        lastClearTime = currTime

        try {
            App.drawSemaphore.acquire()
            App.displayPoints.clear()
            App.displayLines.clear()
            App.displayPoints.addAll(debugPoints)
            App.displayLines.addAll(debugLines)
            debugPoints.clear()
            debugLines.clear()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        App.drawSemaphore.release()
    }

    companion object {
        var robotX = 0.0
            private set
        var robotY = 0.0
            private set
        var robotAngle = 0.0
            private set

        val interpolatedRobotX: Double
            get() {
                val currTime = System.currentTimeMillis()
                val elapsedSinceLast = currTime - lastClearTime
                val distanceToCover = robotX - lastRobotX
                return lastRobotX + distanceToCover * (elapsedSinceLast.toDouble() / lastElapsedTime)
            }

        val interpolatedRobotY: Double
            get() {
                val currTime = System.currentTimeMillis()
                val elapsedSinceLast = currTime - lastClearTime
                val distanceToCover = robotY - lastRobotY
                return lastRobotY + distanceToCover * (elapsedSinceLast.toDouble() / 50)
            }

        val interpolatedRobotAngle: Double
            get() {
                val currTime = System.currentTimeMillis()
                val elapsedSinceLast = currTime - lastClearTime
                val distanceToCover =
                    MathUtils.roundAngle(
                        robotAngle - lastRobotAngle
                    )
                return lastRobotAngle + distanceToCover * (elapsedSinceLast.toDouble() / 50)
            }

        private var lastRobotX = 0.0
        private var lastRobotY = 0.0
        private var lastRobotAngle = 0.0
        private var lastTimeUpdate: Long = 0
        private var lastElapsedTime: Long = 0

        var pointLog = ArrayList<Point>()

        private var lastClearTime: Long = 0

        var elapsedMillisThisUpdate = 0.0
            private set
    }
}