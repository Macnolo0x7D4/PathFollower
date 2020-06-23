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

package org.wint3794.pathfollower.util;

/**
 * A class with some constants.
 */
public class Constants {
    //private static final double bias = 0.91;
    //private static final double meccyBias = 0.9;

    /**
     * Indicates the number of places to round.
     */
    public static final int ROUND_POWER = 8;

    /**
     * Indicates the default period for PID Controllers.
     */
    public static final double DEFAULT_PERIOD = 0.02;

    /**
     * Indicates the Field Length. (Only FTC - Skystone)
     */
    public static final double FIELD_LENGTH = 358.775;

    /**
     * Indicates the default follow angle.
     */
    public static final double DEFAULT_FOLLOW_ANGLE = Math.PI/2;

    /**
     * Indicates the small adjust speed.
     */
    public static final double SMALL_ADJUST_SPEED = 0.135;
}