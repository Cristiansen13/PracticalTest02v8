
interface RequestProcessor {
    fun process(clientId: String, request: List<String>): List<String>
}