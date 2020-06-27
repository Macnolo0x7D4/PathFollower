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
import org.wint3794.debugger.utils.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Client extends Thread {
  private static boolean running = false;
  private DatagramSocket socket;

  private byte[] buf = new byte[1024];

  public Client() {
    try {
      socket = new DatagramSocket(Constants.UDP_PORT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public double[] get() {
    DatagramPacket packet = new DatagramPacket(buf, buf.length);

    try {
      socket.receive(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }

    String received = new String(packet.getData(), 0, packet.getLength());

    return transformBufferIntoDouble(received);
  }

  public void close() {
    socket.close();
  }

  private double[] transformBufferIntoDouble(String buffer) {

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

  @Override
  public void run() {
    while (true) {
      Robot.move(get());
    }
  }
}
