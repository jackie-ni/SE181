package edu.se181

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javafx.application.Platform
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_10
import org.java_websocket.drafts.Draft_17
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object HttpUtil {
    val GAMES_URL = "http://localhost:7000/games"
    val WEBSOCkET_URL = "ws://localhost:7000/games"
    val PORT = 7000
    val HOST = "localhost"

    val gson = Gson()

    private lateinit var wsClient: WebSocketClient

    var game: Game? = null
    var connectedGame: String = ""

    fun connect(gameId: String, password: String = "") {
        wsClient = NewWebSocketClient(gameId, password)
        wsClient.connect()
        connectedGame = gameId
    }

    fun getGames(): List<MatchProperties> {
        val sType = object : TypeToken<List<MatchProperties>>() {}.type
        val request = HttpRequest.newBuilder()
                .uri(URI.create(GAMES_URL))
                .GET()
                .build()
        val response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString())
        return gson.fromJson<List<MatchProperties>>(response.body(), sType)
    }

    fun makeGame(matchProperties: MatchProperties): String {
        val request = HttpRequest.newBuilder()
                .uri(URI.create(GAMES_URL))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(matchProperties)))
                .build()
        val response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString())
        return gson.fromJson<MatchProperties>(response.body(), MatchProperties::class.java).gameId
    }

    fun deleteGame() {
        wsClient.close()
        val request = HttpRequest.newBuilder()
                .uri(URI.create("$GAMES_URL/$connectedGame"))
                .DELETE()
                .build()
        HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun checkPassword(gameId: String, password: String): Boolean {
        val request = HttpRequest.newBuilder()
                .uri(URI.create("$GAMES_URL/$gameId?password=$password"))
                .GET()
                .build()
        return HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString()).statusCode() == 200
    }

    fun sendMessage(type: String, data: String) {
        val message = gson.toJson(Message(type, data))

        wsClient.send(message)
    }

    private class NewWebSocketClient(gameId: String, password: String) :
            WebSocketClient(URI("$WEBSOCkET_URL/$gameId?password=$password"), Draft_17()) {

        override fun onOpen(handshakedata: ServerHandshake?) {
            println("Opened")
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            println("Closed because: $reason")
        }

        override fun onMessage(message: String?) {
            val message2 = gson.fromJson<Message>(message, Message::class.java)

            when (message2.type) {
                "move" -> Platform.runLater(Runnable {game?.makeMove(game?.logicUnit?.convertToMove(message2.data))})
                "resign" -> println("resign") //TODO: handle resign message types
                "draw" -> println("draw") //TODO: handle draw message types
                "end" -> println("end") //TODO: handle end message types
                else -> println("This line should never be printed")
            }
        }

        override fun onError(ex: Exception?) {
            ex?.printStackTrace()
        }

    }

    private data class Message(val type: String, val data: String) {}
}
