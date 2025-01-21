package com.example.socketserver

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.ServerSocket

class SocketServer {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun startServer(port: Int) {
        scope.launch {
            val serverSocket = ServerSocket(port)
            serverSocket.reuseAddress = true
            Log.d("SocketServer", "Server started on port $port")

            while (true) {
                val socket = serverSocket.accept()
                Log.d("", "Client connected: ${socket.inetAddress.hostAddress}")

                launch {
                    try {
                        val input = socket.getInputStream()
                        val receivedData = input.bufferedReader().readLine()
                        Log.d("SocketServer", "Received data: $receivedData")
                    } catch (e: Exception) {
                        Log.e("SocketServer", "Error handling client", e)
                    } finally {
                        socket.close()
                    }
                }
            }
        }
    }

    fun stopServer() {
        scope.cancel()
    }
}
