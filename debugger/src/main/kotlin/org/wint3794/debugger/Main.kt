package org.wint3794.debugger

import org.wint3794.debugger.graphics.App

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val gui = App()
        gui.invokeGui(args)
    }

}