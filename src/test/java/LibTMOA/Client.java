package LibTMOA;

import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Client extends Thread {
    private static Socket s;
    private static BufferedReader input;
    private static final int port = 5000;
    private static final String addr = "127.0.0.1";

    @Override
    public void run() {
        System.out.println("Client initialized successfully!");
        try{
            s = new Socket(addr,port);
            InputStreamReader entradaSocket = new InputStreamReader(s.getInputStream());
            input = new BufferedReader(entradaSocket);
        }catch (IOException e){
            e.printStackTrace();
        }

        while(true){
            try{
                String buffer = input.readLine();
                System.out.println(buffer);
                if(s.isClosed()){
                     break;
                 }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}

