package ro.pub.cs.systems.eim.practicaltest02v8
import android.util.Log
import android.widget.TextView
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class ClientThread(
    private val address: String,
    private val port: Int,
    private val request: List<String>,
    private val result: TextView
) : Thread() {

    override fun run() {
        Log.i("EIM", "ClientThread started")
        try {
            Log.i("EIM", "Connecting to server $address:$port")
            val socket = Socket()
            socket.connect(InetSocketAddress(address, port), 2000)

            val reader = Utilities.getReader(socket)
            val writer = Utilities.getWriter(socket)

            Log.i("EIM", "Sending request: ${request.joinToString(" | ")}")
            request.forEach {
                writer.println(it)
            }
            writer.println()
            writer.flush()

            val sb = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
            Log.i("EIM", "Response received")

            result.post {
                result.text = sb.toString()
            }

            socket.close()
            Log.i("EIM", "Client socket closed")
        } catch (e: IOException) {
            Log.e("EIM", "ClientThread error: ${e.message}", e)
        }
    }
}
