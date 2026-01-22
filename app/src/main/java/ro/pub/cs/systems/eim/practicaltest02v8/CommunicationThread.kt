package ro.pub.cs.systems.eim.practicaltest02v8
import RequestProcessor
import java.net.Socket
import android.util.Log

class CommunicationThread(
    private val socket: Socket,
    private val processor: RequestProcessor
) : Thread() {

    override fun run() {
        Log.i("EIM", "CommunicationThread started")
        try {
            if (socket == null) {
                Log.e("EIM", "Socket is null")
                return
            }

            val reader = Utilities.getReader(socket)
            val writer = Utilities.getWriter(socket)

            val lines = mutableListOf<String>()
            var line: String?
            while (reader.readLine().also { line = it } != null && line!!.isNotEmpty()) {
                lines.add(line!!)
            }
            Log.i("EIM", "Request received from client: ${lines.joinToString(" | ")}")

            val response = processor.process(
                socket.inetAddress.hostAddress,
                lines
            )

            Log.i("EIM", "Sending response to client")
            response.forEach {
                writer.println(it)
            }
            writer.flush()
            socket.close()
            Log.i("EIM", "Socket closed")
        } catch (e: Exception) {
            Log.e("EIM", "CommunicationThread exception: ${e.message}", e)
        }
    }
}
