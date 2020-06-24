/*
 * Copyright 2020 WinT-3794
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.wint3794.debugger.driver;

import org.wint3794.debugger.graphics.Robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

public class Client extends Thread {
    private static final int port = 5000;
    private static final String addr = "127.0.0.1";
    private static boolean running = false;
    private static Socket s;
    private static BufferedReader input;

    @Override
    public void run() {
        System.out.println("Client initialized successfully!");
        do {
            try {
                s = new Socket(addr, port);
                InputStreamReader inputStreamReader = new InputStreamReader(s.getInputStream());
                input = new BufferedReader(inputStreamReader);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while(isRunning());

        while (isRunning()) {
            try {
                String buffer = input.readLine();

                double[] pos = transformBufferIntoDouble(buffer);

                if(pos != null)
                    Robot.move(pos);

                if (s.isClosed()) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private double[] transformBufferIntoDouble(String buffer){

        double[] pose = new double[2];

        StringBuilder builder = new StringBuilder();

        int start = buffer.indexOf("%");

        int separator = buffer.indexOf(",");

        for (int i = start + 1; i < separator; i++) {
            builder.append((char) buffer.getBytes()[i]);
        }

        try {
            pose[0] = Double.parseDouble(builder.toString());
        } catch (NumberFormatException ignored) {
            return null;
        }

        builder = new StringBuilder();

        for (int i = separator + 1; buffer.charAt(i) != '%'; i++) {
            builder.append((char) buffer.getBytes()[i]);
        }

        try {
            pose[1] = Double.parseDouble(builder.toString());
        } catch (NumberFormatException ignored) {
            return null;
        }

        return pose;
    }

    public synchronized static boolean isRunning() {
        return running;
    }

    public synchronized static void setRunning(boolean running) {
        Client.running = running;
    }
}

