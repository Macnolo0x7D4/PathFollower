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
package org.wint3794.pathfollower.debug.telemetries

import org.wint3794.pathfollower.debug.Telemetry
import org.wint3794.pathfollower.util.Constants
import java.io.DataOutputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

class TCPServer : Telemetry() {
    private lateinit var socket: Socket
    private lateinit var server: ServerSocket

    private lateinit var outputStream: DataOutputStream

    init {
        try {
            server = ServerSocket(Constants.DEFAULT_TCP_PORT)
            socket = server.accept()
            outputStream = DataOutputStream(socket.getOutputStream())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun print(log: String) {
        try {
            outputStream.writeUTF(log)
        } catch (e: IOException) {
            reset()
            e.printStackTrace()
        }
    }

    override fun close() {
        try {
            socket.shutdownInput()
            socket.shutdownOutput()
            socket.close()
            server.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun toString(): String {
        return "Debugger TCPServer"
    }

    fun reset() {
        try {
            close()
            server = ServerSocket(Constants.DEFAULT_TCP_PORT)
            socket = server.accept()
            outputStream = DataOutputStream(socket.getOutputStream())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}