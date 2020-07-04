package org.wint3794.debugger.util

import org.wint3794.debugger.geometry.Point

object CommandProcessor {
    fun getFrom(message: String): Point {
        val point: Point = Point()

        if (message.isBlank()) {
            return point
        }

        var commands = message.split("%")
        commands = commands.filter { command -> command.isNotBlank() }

        val commandStructure = commands.last().split(",")

        when (commandStructure[0] ) {
            "ROBOT" -> {
                point.x = commandStructure[1].toDouble()
                point.y = commandStructure[2].toDouble()
                point.angle = commandStructure[3].toDouble()
            }
        }

        return point
    }
}