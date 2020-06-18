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

import java.io.*;
import java.net.Socket;

public class DebuggerClient extends Thread {
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

