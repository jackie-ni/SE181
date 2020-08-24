import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJson
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

    fun checkPassword(ctx: Context) {
        val gameId = ctx.pathParam("gameId")
        val password = ctx.queryParam("password")

        var game = games.firstOrNull { x -> x.gameId == gameId }

        if (game == null) {
            ctx
                    .status(404)
                    .json(WebError("game with gameId $gameId not found"))
            return
        }

        if (!game.checkPassword(password)) {
            ctx
                    .status(401)
                    .json(WebError("incorrect password"))
            return
        }

        ctx.status(200)
    }

    fun onConnect(ctx: WsContext) {
        val gameId = ctx.pathParam(":gameId")
        val game = games.find { x -> x.gameId == gameId }

        if (game != null && game.players < 2) {
            if (game.private) {
                var password = ctx.queryParam("password")

                if (game.checkPassword(password)) {
                    game.addPlayer(ctx, "player${game.players}")
                    return
                }
            }
            else {
                game.addPlayer(ctx, "player${game.players}")
                return
            }
        }

        ctx.session.disconnect()
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
