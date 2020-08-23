import io.javalin.http.Context
import io.javalin.websocket.WsContext
import io.javalin.websocket.WsMessageContext
import java.lang.Exception
import java.util.*

object GameController {

    var games = mutableListOf<Game>()

    fun getGames(ctx: Context) {
        ctx.json(games)
    }

    fun createGame(ctx: Context) {
        var newGame: Game

        try {
            newGame = ctx.body<Game>()
        }
        catch (e: Exception) {
            ctx
                .status(400)
                .json(WebError("could not deserialize request body into Game object"))
            return
        }

        newGame.gameId = randomId()
        games.add(newGame)

        ctx.json(newGame)
    }

    fun deleteGame(ctx: Context) {
        val game = ctx.body<Game>()

        if (game.gameId != "") {
            games.removeAll { x -> x.gameId == game.gameId }
        }
        else {
            ctx
                .status(400)
                .json(WebError("must supply game id"))
        }
    }

    fun onConnect(ctx: WsContext) {
        val gameId = ctx.pathParam(":gameId")
        val game = games.find { x -> x.gameId == gameId }

        var password = ctx.queryParam("password")

        if (game != null && game.checkPassword(password) && game.players < 2) {
            game.addPlayer(ctx, "player${game.players}")
        }
        else {
            ctx.session.disconnect()
        }
    }

    fun onClose(ctx: WsContext) {
        val gameId = ctx.pathParam(":gameId")
        val game = games.find { x -> x.gameId == gameId }

        game?.removePlayer(ctx)
    }

    fun onMessage(ctx: WsMessageContext) {
        val gameId = ctx.pathParam(":gameId")
        val game = games.find { x -> x.gameId == gameId }

        game?.sendMessage(ctx.message())
    }

    private fun randomId() = UUID.randomUUID().toString()
}