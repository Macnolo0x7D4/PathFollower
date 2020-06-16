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

package LibTMOA.debug;


import LibTMOA.utils.Range;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Semaphore;

public class UdpServer implements Runnable {
    public static boolean kill = false;
    //puerto
    private final int clientPort;
    private final Semaphore sendLock = new Semaphore(1);
    //tiempo de la ultima actualizacion
    private long lastSendMillis = 0;
    private String lastUpdate = "";
    private String currentUpdate = "";


    public UdpServer(int clientPort) {
        this.clientPort = clientPort;
    }

    @Override
    public void run() {
        while (true) {
            if (kill) {
                break;
            }
            try {
                //no mandar valores rapido
                if (System.currentTimeMillis() - lastSendMillis < 50) {
                    continue;
                }

                lastSendMillis = System.currentTimeMillis();


                sendLock.acquire();


                if (currentUpdate.length() > 0) {

                    splitAndSend(currentUpdate);
                    currentUpdate = "";
                } else {

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


            while (message.charAt(endIndex) != '%') {
                endIndex--;//move backwards searching for the separator
            }

            sendUdpRAW(message.substring(startIndex, endIndex + 1));

            startIndex = endIndex + 1;
        } while (endIndex != message.length() - 1);//terminate if we have reached the end
    }

    private void sendUdpRAW(String message) {
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            DatagramPacket datagramPacket = new DatagramPacket(
                    message.getBytes(),
                    message.length(),
                    InetAddress.getByName("127.0.0.1"),//194"),
                    clientPort);

            serverSocket.send(datagramPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(String string) {

        if (!sendLock.tryAcquire()) {

            lastUpdate = string;
        } else {
            currentUpdate = string;
            sendLock.release();
        }
    }
}
