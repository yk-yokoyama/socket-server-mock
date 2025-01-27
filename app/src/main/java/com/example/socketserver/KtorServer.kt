package com.example.socketserver

import android.util.Log
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.ServerSocket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.readUTF8Line
import io.ktor.utils.io.writeStringUtf8
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class KtorServer {

    private var serverSocket: ServerSocket? = null
    private var receiveChannel: ByteReadChannel? = null
    private var sendChannel: ByteWriteChannel? = null

    fun open(host: String = "192.168.151.56", port: Int = 8080) {
        runBlocking {
            val selectorManager = SelectorManager(Dispatchers.IO)
            serverSocket = aSocket(selectorManager).tcp().bind(host, port)
            Log.d("KtorServer", "Server is listening at ${serverSocket!!.localAddress}")
            while (true) {
                val socket = serverSocket!!.accept()
                Log.d("", "Accepted $socket")

                CoroutineScope(Dispatchers.IO).launch {
                    receiveChannel = socket.openReadChannel()
                    sendChannel = socket.openWriteChannel(autoFlush = true)

                    try {
                        while (true) {
                            val receivedMessage = receiveChannel!!.readUTF8Line()
                            if (receivedMessage != null) {
                                // TODO: ここで直接クライアントにメッセージを送っても切断されるまで送られないので、コールバックを用意する
                                Log.d("", "received Message: $receivedMessage")
                            } else {
                                Log.d("", "client disconnected")
                                break
                            }
                        }
                    } catch (e: Throwable) {
                        Log.d("KtorServer", "Error: $e")
                        socket.close()
                    }
                }
            }
        }
    }

    fun close() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                serverSocket?.close()
            } catch (e: Exception) {
                Log.e("KtorServer", "Error closing server socket", e)
            }
        }
    }

    fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            sendChannel!!.writeStringUtf8(message)
        }
    }
}