package org.wint3794.pathfollower.debug;


import org.wint3794.pathfollower.util.Range;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Semaphore;

public class UdpServer implements Runnable{
    //puerto
    private final int clientPort;
    public static boolean kill = false;

    public UdpServer(int clientPort) {
        this.clientPort = clientPort;
    }


    private Semaphore sendLock = new Semaphore(1);



    //tiempo de la ultima actualizacion
    private long lastSendMillis = 0;


    @Override
    public void run() {
        while(true){
            if(kill){break;}
            try {
                //no mandar valores rapido
                if(System.currentTimeMillis()-lastSendMillis < 50) {
                    continue;
                }

                lastSendMillis = System.currentTimeMillis();


                sendLock.acquire();



                if(currentUpdate.length() > 0){

                    splitAndSend(currentUpdate);
                                       currentUpdate = "";
                }else{

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

            sendUdpRAW(message.substring(startIndex,endIndex+1));

            startIndex = endIndex+1;
        } while (endIndex != message.length() - 1);//terminate if we have reached the end
    }


    private void sendUdpRAW(String message){
        try(DatagramSocket serverSocket = new DatagramSocket()){
            DatagramPacket datagramPacket = new DatagramPacket(
                    message.getBytes(),
                    message.length(),
                    InetAddress.getByName("192.168.0.9"),//194"),
                    clientPort);

            serverSocket.send(datagramPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private String lastUpdate = "";
    private String currentUpdate = "";


    public void addMessage(String string){

        if(!sendLock.tryAcquire()){

            lastUpdate = string;
        }else{
            currentUpdate = string;
            sendLock.release();
        }
    }
}
