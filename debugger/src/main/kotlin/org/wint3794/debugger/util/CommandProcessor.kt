package org.wint3794.debugger.util

import org.wint3794.debugger.geometry.Pose2d

object CommandProcessor {
    fun getFrom(message: String): Pose2d {
        val pose2d: Pose2d = Pose2d()

        if (message.isBlank()) {
            return pose2d;
        }

        var commands = message.split("%")
        commands = commands.filter { command -> command.isNotBlank() }

        val commandStructure = commands.last().split(",")

        if (commandStructure[0] == "ROBOT") {
            pose2d.x = commandStructure[1].toDouble()
            pose2d.y = commandStructure[2].toDouble()
            pose2d.angle = commandStructure[3].toDouble()
        }

        return pose2d
    }
}