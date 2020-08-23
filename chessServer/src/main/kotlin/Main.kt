import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*

fun main(args: Array<String>) {

    val app = Javalin.create().start(7000)

    app.routes {
        path("games") {
            get(GameController::getGames)
            post(GameController::createGame)
            delete(GameController::deleteGame)
        }

        ws("games/:gameId") { ws ->
            ws.onConnect { ctx -> GameController.onConnect(ctx) }
            ws.onClose { ctx -> GameController.onClose(ctx) }
            ws.onMessage { ctx -> GameController.onMessage(ctx) }
        }
    }
}