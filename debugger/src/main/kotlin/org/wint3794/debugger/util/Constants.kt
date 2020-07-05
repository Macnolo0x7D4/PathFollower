package org.wint3794.debugger.util

import kotlin.math.sqrt

object Constants {
    const val NAME: String = "PathFollower Graphical Debugger"

    const val SCREEN_SIZE: Double = 600.0

    const val FIELD_SIZE: Double = 358.8

    const val UDP_CLIENT: Int = 15645

    const val SCREEN_SIZE_OFFSET: Double = 37.0

    const val ROBOT_SIZE: Double = 18 * 2.54

    val ROBOT_RADIUS: Double = sqrt(2.0) * ROBOT_SIZE / 2.0
}