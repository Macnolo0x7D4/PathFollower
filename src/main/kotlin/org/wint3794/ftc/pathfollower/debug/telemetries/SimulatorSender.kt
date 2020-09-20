package org.wint3794.ftc.pathfollower.debug.telemetries

import org.wint3794.ftc.pathfollower.controllers.Robot
import org.wint3794.ftc.pathfollower.debug.DebugConfiguration
import org.wint3794.ftc.pathfollower.geometry.Point
import org.wint3794.ftc.pathfollower.geometry.Pose2d
import org.wint3794.ftc.pathfollower.util.Constants
import java.net.UnknownHostException
import java.text.DecimalFormat

class SimulatorSender(config: DebugConfiguration) {

    companion object {
        private lateinit var udpServer: UdpServer
        private var messageBuilder = StringBuilder()
        private var debug = false
        private val df = DecimalFormat("#.00")

        fun sendRobotLocation(robot: Pose2d) {
            if (!debug) {
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
            if (!debug) {
                return
            }

            messageBuilder.append("P,")
                .append(df.format(floatPoint.x))
                .append(",")
                .append(df.format(floatPoint.y))
                .append("%")
        }

        fun sendLogPoint(floatPoint: Point) {
            if (!debug) {
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
            if (!debug) {
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
            if (!debug) {
                return
            }
            messageBuilder.append("CLEAR,%")

            udpServer.send(
                messageBuilder.toString())
            messageBuilder = StringBuilder()
        }

        fun clearLogPoints() {
            if (!debug) {
                return
            }
            udpServer.send("CLEARLOG,%")
        }
    }

    init {
        UdpServer.isRunning = config.debug

        debug = config.debug

        try {
            udpServer = if (config.ip == "") {
                UdpServer(config.port)
            } else {
                UdpServer(config.ip, config.port)
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }

        val runner = Thread(udpServer)

        if (config.debug) {
            runner.start()
        }
    }
}