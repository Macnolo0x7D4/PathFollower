package org.wint3794.pathfollower.debugger

import org.wint3794.pathfollower.debugger.graphics.App
import org.wint3794.pathfollower.debugger.net.UdpClient
import kotlin.system.exitProcess

object Main {

    private lateinit var udp: UdpClient;

    @JvmStatic
    fun main(args: Array<String>) {
        udp = UdpClient()
        udp.start()

        val app = App()
        app.invokeGui(args)
    }

    @JvmStatic
    fun close() {
        udp.close()
        exitProcess(0)
    }
}