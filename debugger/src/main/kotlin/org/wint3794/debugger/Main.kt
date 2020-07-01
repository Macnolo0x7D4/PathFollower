package org.wint3794.debugger

import org.wint3794.debugger.graphics.App
import org.wint3794.debugger.net.Client

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        Client.start()

        val gui = App()
        gui.invokeGui(args)
    }

}