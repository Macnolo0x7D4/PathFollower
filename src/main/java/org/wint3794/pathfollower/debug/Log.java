/*
 * Copyright 2020 WinT 3794 (Manuel Díaz Rojo and Alexis Obed García Hernández)
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

public class Log {
    private static StringBuilder builder = new StringBuilder();
    private static boolean debuggingMode = false;
    private static Telemetry telemetry;

    public static void initializer() {
        telemetry.init();
    }

    public static void println(String log, String origin) {
        builder.append("[").append(origin).append("]: ").append(log).append('\n');
    }

    public static void println(String log) {
        builder.append(log).append('\n');
    }

    public static void update() {
        telemetry.print(builder.toString());
        builder = new StringBuilder();
    }

    public static void close(){
        telemetry.close();
    }

    public static void setTelemetry(Telemetry telemetry) {
        Log.telemetry = telemetry;
    }

    public static void setDebuggingMode(boolean debuggingMode) {
        Log.debuggingMode = debuggingMode;
    }
}
