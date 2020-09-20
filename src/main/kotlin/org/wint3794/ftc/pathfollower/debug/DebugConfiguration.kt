package org.wint3794.ftc.pathfollower.debug

import org.wint3794.ftc.pathfollower.debug.telemetries.ConsolePrinter
import org.wint3794.ftc.pathfollower.util.Constants

/**
 * The configuration for PathFollower Debugger
 */
data class DebugConfiguration(
    val debug: Boolean = false,
    val telemetry: Telemetry = ConsolePrinter(),
    val port: Int = Constants.DEFAULT_CLIENT_PORT,
    val ip: String = ""
)