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

package org.wint3794.pathfollower.debug;

import org.wint3794.pathfollower.io.Telemetry;
import org.wint3794.pathfollower.models.structures.Pose2D;

/**
 * A class with some static functions to log.
 */
public class Log {
    private static StringBuilder builder = new StringBuilder();
    private static Telemetry telemetry;
    private static boolean debug = true;

    /**
     * Initialize the Log class.
     */
    public static void init() {
        telemetry.init();
    }

    /**
     * Add log to StringBuilder.
     * @param log String to append.
     * @param origin The origin of the log.
     */
    public static void println(String log, String origin) {
        builder.append("[").append(origin).append("]: ").append(log).append('\n');
    }

    /**
     * Checkpoint in log by Pose2d
     * @param pose Robot Position
     */
    public static void check(Pose2D pose){
        telemetry.print("%" + pose.getX() + "," + pose.getY() + "%");
    }

    /**
     * Updates log exporting StringBuilder as string to telemetry and clears buffer.
     */
    public static void update() {
        if (debug) {
            telemetry.print(builder.toString());
        }
        builder = new StringBuilder();
    }

    /**
     * Closes telemetry.
     */
    public static void close() {
        telemetry.close();
    }

    /**
     * Sets a Telemetry Driver. It needs to implements {@link org.wint3794.pathfollower.io.Telemetry}
     * @param telemetry Telemetry Driver. You can create one by implementing {@link org.wint3794.pathfollower.io.Telemetry}
     *                  interface or you can use some of our drivers, like {@link org.wint3794.pathfollower.debug.telemetries.ConsolePrinter}.
     */
    public static void setTelemetry(Telemetry telemetry) {
        Log.telemetry = telemetry;
    }

    public static void setDebuggingMode(boolean debug){
        Log.debug = debug;
    }
}
