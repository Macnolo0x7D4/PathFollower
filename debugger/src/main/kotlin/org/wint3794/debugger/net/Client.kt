package org.wint3794.debugger.net

import org.wint3794.debugger.util.CommandProcessor
import org.wint3794.debugger.util.Constants
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException


object Client: Thread("Client") {

    private lateinit var socket: DatagramSocket
    private var buffer: ByteArray = ByteArray(1024)

    init {
        try {
            socket = DatagramSocket(Constants.UDP_CLIENT)
        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }

    override fun run() {
        while (true) {
            val packet = DatagramPacket(buffer, buffer.size)

            try {
                socket.receive(packet)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val commands = String(packet.data, 0, packet.length)
            val point = CommandProcessor.getFrom(commands)
            println(point.toString())
            // println(commands)
        }
    }
}