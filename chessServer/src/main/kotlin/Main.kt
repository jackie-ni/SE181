import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*

fun main(args: Array<String>) {
    JavalinApp(7000).init()
}

class JavalinApp(private val port: Int) {

    fun init(): Javalin {
        val app = Javalin.create().start(port)

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

        return app
    }
}