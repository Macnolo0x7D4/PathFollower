package org.wint3794.pathfollower.debug

import org.wint3794.pathfollower.controllers.Robot
import org.wint3794.pathfollower.debug.telemetries.UdpServer
import org.wint3794.pathfollower.geometry.Point
import org.wint3794.pathfollower.geometry.Pose2d
import java.net.UnknownHostException
import java.text.DecimalFormat

class ComputerDebugging(ip: String, port: Int) {
    constructor(port: Int) : this("", port) {}

    companion object {
        private var udpServer: UdpServer? = null
        private var messageBuilder = StringBuilder()
        private val df = DecimalFormat("#.00")

        /**
         * Sends the robot location to the debug computer
         */
        fun sendRobotLocation(robot: Pose2d) {
            //si no se usa lacompu
            if (!Robot.Companion.usingComputer) {
                return
            }

            //manda la direcciondel robot
            messageBuilder.append("ROBOT,")
            messageBuilder.append(df.format(robot.x))
            messageBuilder.append(",")
            messageBuilder.append(df.format(robot.y))
            messageBuilder.append(",")
            messageBuilder.append(df.format(robot.angle))
            messageBuilder.append("%")
        }

        /**
         * Sends the location of any point you would like to send
         * @param floatPoint the point you want to send
         */
        fun sendKeyPoint(floatPoint: Point) {
            if (!Robot.Companion.usingComputer) {
                return
            }
            messageBuilder.append("P,")
                .append(df.format(floatPoint.x))
                .append(",")
                .append(df.format(floatPoint.y))
                .append("%")
        }

        /**
         * This is a point you don't want to clear every update
         * @param floatPoint the point you want to send
         */
        fun sendLogPoint(floatPoint: Point) {
            if (!Robot.Companion.usingComputer) {
                return
            }
            messageBuilder.append("LP,")
                .append(df.format(floatPoint.x))
                .append(",")
                .append(df.format(floatPoint.y))
                .append("%")
        }

        /**
         * Used for debugging lines
         * @param point1
         * @param point2
         */
        fun sendLine(
            point1: Point,
            point2: Point
        ) {
            //no compu
            if (!Robot.Companion.usingComputer) {
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

        /**
         * This kills the udpServer background thread
         */
        fun stopAll() {
            if (!Robot.Companion.usingComputer) {
                return
            }
            UdpServer.Companion.isRunning = false
        }

        /**
         * Sends the data accumulated over the update by adding it to the udpServer
         */
        fun markEndOfUpdate() {
            if (!Robot.Companion.usingComputer) {
                return
            }
            messageBuilder.append("CLEAR,%")

//        udpServer.addMessage(messageBuilder.toString());
            udpServer!!.send(messageBuilder.toString())
            messageBuilder = StringBuilder()
        }

        /**
         * Forces a clear log
         */
        fun clearLogPoints() {
            if (!Robot.Companion.usingComputer) {
                return
            }
            udpServer!!.send("CLEARLOG,%")
        }
    }

    /**
     * Initializes udp server and starts it's thread
     */
    init {
        UdpServer.Companion.isRunning = true
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