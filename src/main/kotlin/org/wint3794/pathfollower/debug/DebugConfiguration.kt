package org.wint3794.pathfollower.debug

import org.wint3794.pathfollower.debug.telemetries.ConsolePrinter
import org.wint3794.pathfollower.util.Constants

/**
 * The configuration for PathFollower Debugger
 */
data class DebugConfiguration(
    val debug: Boolean = true,
    val telemetry: Telemetry = ConsolePrinter(),
    val port: Int = Constants.DEFAULT_CLIENT_PORT,
    val ip: String = ""
)