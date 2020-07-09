package org.wint3794.pathfollower.debug.telemetries

import org.wint3794.pathfollower.controllers.Robot
import org.wint3794.pathfollower.geometry.Point
import org.wint3794.pathfollower.geometry.Pose2d
import org.wint3794.pathfollower.util.Constants
import java.net.UnknownHostException
import java.text.DecimalFormat

class SimulatorSender(port: Int = Constants.DEFAULT_CLIENT_PORT, ip: String = "") {
    companion object {
        private lateinit var udpServer: UdpServer
        private var messageBuilder = StringBuilder()
        private val df = DecimalFormat("#.00")

        fun sendRobotLocation(robot: Pose2d) {
            if (!Robot.usingComputer) {
                return
            }

            messageBuilder.append("ROBOT,")
            messageBuilder.append(
                df.format(robot.x))
            messageBuilder.append(",")
            messageBuilder.append(
                df.format(robot.y))
            messageBuilder.append(",")
            messageBuilder.append(
                df.format(robot.angle))
            messageBuilder.append("%")
        }

        fun sendKeyPoint(floatPoint: Point) {
            if (!Robot.usingComputer) {
                return
            }
            messageBuilder.append("P,")
                .append(df.format(floatPoint.x))
                .append(",")
                .append(df.format(floatPoint.y))
                .append("%")
        }

        fun sendLogPoint(floatPoint: Point) {
            if (!Robot.usingComputer) {
                return
            }
            messageBuilder.append("LP,")
                .append(df.format(floatPoint.x))
                .append(",")
                .append(df.format(floatPoint.y))
                .append("%")
        }

        fun sendLine(
            point1: Point,
            point2: Point
        ) {
            if (!Robot.usingComputer) {
                return
            }
            messageBuilder.append("LINE,")
                .append(df.format(point1.x))
                .append(",")
                .append(df.format(point1.y))
                .append(",")
                .append(df.format(point2.x))
                .append(",")
                .append(df.format(point2.y))
                .append("%")
        }

        fun markEndOfUpdate() {
            if (!Robot.usingComputer) {
                return
            }
            messageBuilder.append("CLEAR,%")

            udpServer.send(messageBuilder.toString())
            messageBuilder = StringBuilder()
        }

        fun clearLogPoints() {
            if (!Robot.usingComputer) {
                return
            }
            udpServer.send("CLEARLOG,%")
        }
    }

    init {
        UdpServer.isRunning = true

        try {
            udpServer = if (ip == "") {
                UdpServer(port)
            } else {
                UdpServer(ip, port)
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }
        val runner = Thread(udpServer)
        runner.start()
    }
}