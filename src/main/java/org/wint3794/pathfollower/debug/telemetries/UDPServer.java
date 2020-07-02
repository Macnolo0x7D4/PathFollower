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

import org.wint3794.pathfollower.debug.Log;
import org.wint3794.pathfollower.debug.Telemetry;
import org.wint3794.pathfollower.geometry.Pose2d;
import org.wint3794.pathfollower.util.Constants;
import org.wint3794.pathfollower.util.MathUtils;
import org.wint3794.pathfollower.util.Range;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.concurrent.Semaphore;

public class UDPServer extends Telemetry implements Runnable {
  private final Semaphore sendLock = new Semaphore(1);
  private static boolean running = false;
  private final int clientPort;
  private final InetAddress clientIp;
  private long lastSendMillis = 0;
  private String currentUpdate = "";

  public UDPServer(int clientPort) throws UnknownHostException {
    this.clientPort = clientPort;
    clientIp = InetAddress.getByName("127.0.0.1");
  }

  public UDPServer(int clientPort, InetAddress clientIp) {
    this.clientPort = clientPort;
    this.clientIp = clientIp;
  }

  @Override
  public void init() {
    send("CLEARLOG,%");
    setRunning(true);
  }

  @Override
  public void print(String log) {
    System.out.println(log);
  }

  @Override
  public void close() {
    setRunning(false);
  }

  public void sendPosition(Pose2d pose2d) {
    final double x = MathUtils.roundPower(pose2d.getX(), 2);
    final double y = MathUtils.roundPower(pose2d.getY(), 2);
    final double angle = MathUtils.roundPower(pose2d.getAngle(), 2);

    send("ROBOT," + x + "," + y + "," + angle + "%");
    // send("POS," + x + "," + y + "," + angle + "%");
    // super.outputStream.writeUTF("%" + pose2d.getX() + "," + pose2d.getY() + "%\n");tackTrace();
  }

  public void sendKeyPoint(Pose2d floatPoint) {
    send("P," + floatPoint.getX() + "," + floatPoint.getX() + "%");
  }

  public void sendLogPoint(Pose2d floatPoint) {
    send("LP," + floatPoint.getX() + "," + floatPoint.getX() + "%");
  }

  public void sendLine(Pose2d floatPoint1, Pose2d floatPoint2) {
    send("LINE," + floatPoint1.getX() + "," + floatPoint1.getX() + "," + floatPoint2.getX() + "," + floatPoint2.getX() + "%");
  }

  @Override
  public void run() {
    while (isRunning()) {
      try {
        if (System.currentTimeMillis() - lastSendMillis < 50) {
          continue;
        }

        lastSendMillis = System.currentTimeMillis();
        sendLock.acquire();

        if (currentUpdate.length() > 0) {
          send(currentUpdate);
          currentUpdate = "";
        }

        sendLock.release();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void send(String message) {

    message += "CLEAR,%";

    int startIndex = 0;
    int endIndex;

    do {
      endIndex = Range.clip(startIndex + 600, 0, message.length() - 1);
      message = message.substring(startIndex, endIndex + 1);

      try (DatagramSocket serverSocket = new DatagramSocket()) {
        DatagramPacket datagramPacket =
                new DatagramPacket(
                        message.getBytes(),
                        message.length(),
                        clientIp,
                        clientPort);

        serverSocket.send(datagramPacket);

      } catch (IOException e) {
        e.printStackTrace();
      }

      startIndex = endIndex + 1;
    } while (endIndex != message.length() - 1);
  }

  @Override
  public String toString() {
    return "Graphical Debugger Server (UDP)";
  }

  public static boolean isRunning() {
    return running;
  }

  public static void setRunning(boolean running) {
    UDPServer.running = running;
  }
}
