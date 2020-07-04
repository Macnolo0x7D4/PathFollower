/*
 * Copyright 2020 WinT 3794 (Manuel Diaz Rojo and Alexis Obed Garcia Hernandez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.wint3794.pathfollower.util

/**
 * A class with some constants.
 */
object Constants {
    //private static final double bias = 0.91;
    //private static final double meccyBias = 0.9;
    /**
     * Indicates the number of places to round.
     */
    const val ROUND_POWER = 8

    /**
     * Indicates the default period for PID Controllers.
     */
    const val DEFAULT_PERIOD = 0.02

    /**
     * Indicates the Field Length. (Only FTC - Skystone)
     */
    const val FIELD_LENGTH = 358.775

    /**
     * Indicates the default follow angle.
     */
    const val DEFAULT_FOLLOW_ANGLE = Math.PI / 2

    /**
     * Indicates the small adjust speed.
     */
    const val SMALL_ADJUST_SPEED = 0.135

    /**
     * Indicates the default client port for UDP connections
     */
    const val DEFAULT_CLIENT_PORT = 15645

    /**
     * Indicates the default port for TCP connections
     */
    const val DEFAULT_TCP_PORT = 5000
}