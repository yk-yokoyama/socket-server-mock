package com.example.socketserver

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val startButton by lazy { findViewById<Button>(R.id.btn_start) }
    private val stopButton by lazy { findViewById<Button>(R.id.btn_stop) }

    private val socketServer = SocketServer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startButton.setOnClickListener {
            socketServer.startServer(8080)
        }
        stopButton.setOnClickListener {
            Log.d("MainActivity", "Stop button clicked")
            socketServer.stopServer()
        }
    }
}