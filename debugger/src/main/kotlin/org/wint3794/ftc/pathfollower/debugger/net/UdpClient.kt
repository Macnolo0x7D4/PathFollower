package org.wint3794.ftc.pathfollower.debugger.net

import org.wint3794.ftc.pathfollower.debugger.util.Constants
import org.wint3794.ftc.pathfollower.debugger.util.CommandProcessor
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException

class UdpClient : Thread() {

    var processor: CommandProcessor =
        CommandProcessor()
    var isRunning = false

    init {
        isRunning = true
    }

    override fun run() {
        try {
            DatagramSocket(Constants.UDP_PORT).use { clientSocket ->
                while (isRunning) {
                    val buffer = ByteArray(65507)
                    val datagramPacket = DatagramPacket(buffer, 0, buffer.size)
                    clientSocket.receive(datagramPacket)
                    val receivedMessage = String(datagramPacket.data)
                    processor.processMessage(receivedMessage)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun close() {
        isRunning = false
    }

}