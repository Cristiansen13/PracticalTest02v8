import java.net.ServerSocket
import android.util.Log
import ro.pub.cs.systems.eim.practicaltest02v8.CommunicationThread

class ServerThread(private val port: Int) : Thread() {

    private var serverSocket: ServerSocket? = null
    val processor: RequestProcessor = Processor()

    override fun run() {
        Log.i("EIM", "Server starting on port $port")
        try {
            serverSocket = ServerSocket(port)
            while (!isInterrupted) {
                Log.i("EIM", "Waiting for client... calling accept()")
                val socket = serverSocket!!.accept()
                Log.i("EIM", "Client accepted from ${socket.inetAddress.hostAddress}")
                CommunicationThread(socket, processor).start()
            }
        } catch (e: Exception) {
            Log.e("EIM", "Server encountered an exception: ${e.message}", e)
        } finally {
            Log.i("EIM", "Server stopping")
        }
    }

    fun stopThread() {
        interrupt()
        try {
            serverSocket?.close()
            Log.i("EIM", "Server socket closed")
        } catch (e: Exception) {
            Log.e("EIM", "Error while closing server socket: ${e.message}", e)
        }
    }
}
