package org.wint3794.pathfollower.debug.telemetries

import org.wint3794.pathfollower.debug.Log
import org.wint3794.pathfollower.debug.Telemetry
import org.wint3794.pathfollower.util.Range
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.concurrent.Semaphore

class UdpServer : Telemetry, Runnable {
    private val clientPort: Int
    private var ip: InetAddress? = null
    private val sendLock = Semaphore(1)
    private var lastSendMillis: Long = 0
    private var currentUpdate = ""

    constructor(clientPort: Int) {
        this.clientPort = clientPort
        ip = InetAddress.getLocalHost()
    }

    constructor(ip: String?, clientPort: Int) {
        this.clientPort = clientPort
        this.ip = InetAddress.getByName(ip)
    }

    override fun run() {
        while (isRunning) {
            try {
                if (System.currentTimeMillis() - lastSendMillis < 50) {
                    continue
                }
                lastSendMillis = System.currentTimeMillis()
                sendLock.acquire()
                if (currentUpdate.length > 0) {
                    send(currentUpdate)
                    currentUpdate = ""
                }
                sendLock.release()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun send(message: String) {
        var startIndex = 0
        var endIndex: Int
        do {
            endIndex = Range.clip(startIndex + 600, 0, message.length - 1)
            while (message[endIndex] != '%') {
                endIndex--
            }
            val packet = message.substring(startIndex, endIndex + 1)
            try {
                DatagramSocket().use { serverSocket ->
                    val datagramPacket = DatagramPacket(
                        packet.toByteArray(),
                        packet.length,
                        ip,
                        clientPort
                    )
                    serverSocket.send(datagramPacket)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.error("Oh no! ${e.message}. Graphical Debugger will be disabled.", "UDP Server")
                isRunning = false
                break
            }
            startIndex = endIndex + 1
        } while (endIndex != message.length - 1)
    }

    override fun print(log: String) {
        println(log)
    }

    override fun close() {}

    companion object {
        @get:Synchronized
        @set:Synchronized
        var isRunning = false

    }
}