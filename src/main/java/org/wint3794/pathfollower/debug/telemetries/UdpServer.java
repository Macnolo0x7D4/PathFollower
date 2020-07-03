package org.wint3794.pathfollower.debug.telemetries;


import org.wint3794.pathfollower.debug.Telemetry;
import org.wint3794.pathfollower.util.Range;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

public class UdpServer extends Telemetry implements Runnable{
    private final int clientPort;
    private InetAddress ip = null;
    private final Semaphore sendLock = new Semaphore(1);
    private long lastSendMillis = 0;
    private String currentUpdate = "";
    private static boolean running = false;

    public UdpServer(int clientPort) throws UnknownHostException {
        this.clientPort = clientPort;
        this.ip = InetAddress.getLocalHost();
    }

    public UdpServer(String ip, int clientPort) throws UnknownHostException {
        this.clientPort = clientPort;
        this.ip = InetAddress.getByName(ip);
    }

    @Override
    public void run() {
        while(isRunning()){
            try {
                if(System.currentTimeMillis()-lastSendMillis < 50) {
                    continue;
                }

                lastSendMillis = System.currentTimeMillis();

                sendLock.acquire();

                if(currentUpdate.length() > 0){
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

        int startIndex = 0;
        int endIndex;

        do {
            endIndex = Range.clip(startIndex + 600, 0, message.length() - 1);

            while (message.charAt(endIndex) != '%') {
                endIndex--;
            }

            final String packet = message.substring(startIndex,endIndex+1);

            try(DatagramSocket serverSocket = new DatagramSocket()){
                DatagramPacket datagramPacket = new DatagramPacket(
                        packet.getBytes(),
                        packet.length(),
                        ip,
                        clientPort);

                serverSocket.send(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            startIndex = endIndex+1;
        } while (endIndex != message.length() - 1);
    }

    @Override
    public void init() {

    }

    @Override
    public void print(String log) {
        System.out.println(log);
    }

    @Override
    public void close() {

    }

    public synchronized static boolean isRunning() {
        return running;
    }

    public synchronized static void setRunning(boolean running) {
        UdpServer.running = running;
    }
}
