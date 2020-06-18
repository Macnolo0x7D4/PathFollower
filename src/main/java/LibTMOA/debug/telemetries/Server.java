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

package LibTMOA.debug.telemetries;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import LibTMOA.io.Telemetry;

public class Server implements Telemetry {

    private static final int PORT = 5000;
    private Socket s;
    private ServerSocket ss;
    private DataOutputStream outputStream;

    @Override
    public void init() {
        try {
            this.ss = new ServerSocket(PORT);
            this.s = ss.accept();
            this.outputStream = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void print(String log) {
        try{
            this.outputStream.writeUTF(log);
        }catch (IOException e){
            reset();
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try{
            s.shutdownInput();
            s.shutdownOutput();
            s.close();
            ss.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Debugger Server";
    }

    private void reset(){
        try {
            close();
            this.ss = new ServerSocket(PORT);
            this.s = ss.accept();
            this.outputStream = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
