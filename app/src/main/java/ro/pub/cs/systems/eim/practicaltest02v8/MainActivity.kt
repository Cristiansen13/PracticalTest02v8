package ro.pub.cs.systems.eim.practicaltest02v8


import ServerThread
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private var serverThread: ServerThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serverPort = findViewById<EditText>(R.id.server_port)
        val startServer = findViewById<Button>(R.id.start_server)

        val address = findViewById<EditText>(R.id.client_address)
        val port = findViewById<EditText>(R.id.client_port)
        val line1 = findViewById<EditText>(R.id.request_line1)
        val send = findViewById<Button>(R.id.send_request)
        val result = findViewById<TextView>(R.id.result)

        startServer.setOnClickListener {
            val p = serverPort.text.toString()
            if (p.isEmpty()) {
                Toast.makeText(applicationContext, "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            serverThread = ServerThread(p.toInt())
            serverThread?.start()
        }

        send.setOnClickListener {
            val addr = address.text.toString()
            val portText = port.text.toString()
            if (addr.isEmpty() || portText.isEmpty()) {
                Toast.makeText(applicationContext, "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (serverThread == null || !serverThread!!.isAlive) {
                Toast.makeText(applicationContext, "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val line1Text = line1.text.toString()
            if (line1Text.isEmpty()) {
                Toast.makeText(applicationContext, "[MAIN ACTIVITY] Parameter from client (URL) should be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            result.text = ""

            ClientThread(
                addr,
                portText.toInt(),
                listOf(line1Text),
                result
            ).start()
        }
    }

    override fun onDestroy() {
        serverThread?.stopThread()
        super.onDestroy()
    }
}
