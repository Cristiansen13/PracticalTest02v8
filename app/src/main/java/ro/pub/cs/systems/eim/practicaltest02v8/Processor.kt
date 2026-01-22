import java.net.HttpURLConnection
import java.net.URL
import android.util.Log

class Processor : RequestProcessor {

    override fun process(clientId: String, request: List<String>): List<String> {
        Log.i("EIM", "process() entered")
        Log.i("EIM", "Client: $clientId, Request: ${request.joinToString(" | ")}")
        if (request.isEmpty()) {
            return listOf("Invalid request. Expected one line: <url>.")
        }

        val urlParam = request[0].trim()
        if (urlParam.contains("bad", ignoreCase = true)) {
            return listOf("URL blocked by firewall\n")
        }

        val body = fetchBody(urlParam)
        return if (body != null) {
            listOf(body)
        } else {
            listOf("Could not retrieve data for $urlParam")
        }
    }

    private fun fetchBody(urlString: String): String? {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return null
            }

            connection.inputStream.bufferedReader().use { it.readText() }
        } catch (_: Exception) {
            null
        }
    }
}
