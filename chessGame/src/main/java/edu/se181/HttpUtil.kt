package edu.se181

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    private lateinit var mWs: WebSocketClient

    fun connect(gameId: String, password: String = "") {
        // "$WEBSOCkET_URL/$gameId?password=$password"
        mWs = NewWebSocketClient("$WEBSOCkET_URL/$gameId?password=$password")
        println("$WEBSOCkET_URL/$gameId?password=$password")
        mWs.connect()

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

    fun checkPassword(gameId: String, password: String): Boolean {
        val request = HttpRequest.newBuilder()
                .uri(URI.create("$GAMES_URL/$gameId?password=$password"))
                .GET()
                .build()
        return HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString()).statusCode() == 200
    }

    private class NewWebSocketClient(url: String) : WebSocketClient(URI(url), Draft_17()) {
        override fun onOpen(handshakedata: ServerHandshake?) {
            println("Opened")
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            println("Closed because: $reason")
        }

        override fun onMessage(message: String?) {
            //todo
        }

        override fun onError(ex: Exception?) {
            ex?.printStackTrace()
        }

    }

}
