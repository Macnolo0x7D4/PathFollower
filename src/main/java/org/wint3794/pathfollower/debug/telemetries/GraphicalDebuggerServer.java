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

package org.wint3794.pathfollower.debug.telemetries;

import org.wint3794.pathfollower.debug.Telemetry;
import org.wint3794.pathfollower.geometry.Pose2d;
import org.wint3794.pathfollower.util.Range;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Semaphore;

public class GraphicalDebuggerServer extends Telemetry implements Runnable {
  public static boolean kill = false;
  private final int clientPort = 5000;
  private Semaphore sendLock = new Semaphore(1);

  private long lastSendMillis = 0;
  private String lastUpdate = "";
  private String currentUpdate = "";

  @Override
  public void run() {
    while (true) {
      if (kill) break;

      try {
        if (System.currentTimeMillis() - lastSendMillis < 50) {
          continue;
        }

        lastSendMillis = System.currentTimeMillis();
        sendLock.acquire();

        if (currentUpdate.length() > 0) {
          splitAndSend(currentUpdate);
          currentUpdate = "";
        }

        sendLock.release();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void splitAndSend(String message) {

    int startIndex = 0;
    int endIndex;

    do {
      endIndex = Range.clip(startIndex + 600, 0, message.length() - 1);

      sendUdpRAW(message.substring(startIndex, endIndex + 1));
      startIndex = endIndex + 1;
    } while (endIndex != message.length() - 1);
  }

  private void sendUdpRAW(String message) {
    try (DatagramSocket serverSocket = new DatagramSocket()) {
      DatagramPacket datagramPacket =
          new DatagramPacket(
              message.getBytes(),
              message.length(),
              InetAddress.getByName("127.0.0.1"), clientPort);

      serverSocket.send(datagramPacket);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return "Graphical Debugger Server (TCP)";
  }

  public void sendPosition(Pose2d pose2d) {
    System.out.println("%" + pose2d.getX() + "," + pose2d.getY() + "%");
    splitAndSend("%" + pose2d.getX() + "," + pose2d.getY() + "%");
    // super.outputStream.writeUTF("%" + pose2d.getX() + "," + pose2d.getY() + "%\n");tackTrace();
  }

    @Override
    public void init() {

    }

    @Override
    public void print(String log) {

    }

    @Override
    public void close() {

    }
}
