import org.junit.Assert.*
import io.javalin.Javalin
import io.javalin.plugin.json.JavalinJson
import io.javalin.websocket.WsContext
import io.mockk.every
import io.mockk.mockk
import kong.unirest.Unirest
import kong.unirest.json.JSONObject
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class GameControllerTests {

    companion object {

        private val mockWsContext = mockk<WsContext>()

        private lateinit var app: Javalin

        private var testGames = mutableListOf<Game>(
            Game("test1", false, "test-game-1", 0, ""),
            Game("test2", true, "test-game-2", 0, "wet-ass-p-word")
        )

        private var testGamesJson = JavalinJson.toJson(testGames)

        private var testUrl = "http://localhost:6969/games"

        @BeforeClass @JvmStatic
        fun setUp() {
            app = JavalinApp(6969).init()
            GameController.games.addAll(testGames)
        }

        @AfterClass @JvmStatic
        fun tearDown() {
            app.stop()
        }
    }

    @Test
    fun `GET games returns list of games`() {
        GameController.games = testGames
        val response = Unirest.get(testUrl).asString()
        assertEquals(200, response.status)
        assertEquals(testGamesJson, response.body)
    }

    @Test
    fun `POST games creates a game`() {
        val response = Unirest
                .post(testUrl)
                .body(JSONObject().put("name", "test3"))
                .asString()

        assertEquals(200, response.status)
        assertNotNull(GameController.games.firstOrNull{ x -> x.name == "test3" })
    }

    @Test
    fun `POST games with bad data returns 400`() {
        val response = Unirest
                .post(testUrl)
                .body("")
                .asString()

        assertEquals(400, response.status)
    }

    @Test
    fun `DELETE games removes a game`() {
        GameController.games.add(Game(name = "remove-test", gameId = "remove-test"))

        val response = Unirest
                .delete("$testUrl/remove-test")
                .asString()

        assertEquals(200, response.status)
        assertNull(GameController.games.firstOrNull{ x -> x.name == "remove-test" })
    }

    @Test
    fun `DELETE games with invalid gameId returns 404`() {
        val response = Unirest
                .delete("$testUrl/bad-id")
                .asString()

        assertEquals(404, response.status)
    }

    @Test
    fun `connecting to open websocket without password is successful`() {
        every { mockWsContext.pathParam(":gameId") } returns "test-game-1"

        GameController.onConnect(mockWsContext)

        var game = GameController
                .games
                .firstOrNull { x -> x.gameId == "test-game-1" }

        assertNotNull(game?.getPlayer(mockWsContext))
        game?.removePlayer(mockWsContext)
    }

    @Test
    fun `connecting to private websocket with password is successful`() {
        every { mockWsContext.pathParam(":gameId") } returns "test-game-2"
        every { mockWsContext.queryParam("password") } returns "wet-ass-p-word"

        GameController.onConnect(mockWsContext)

        var game = GameController
            .games
            .firstOrNull { x -> x.gameId == "test-game-2" }

        assertNotNull(game?.getPlayer(mockWsContext))
        game?.removePlayer(mockWsContext)
    }

    @Test
    fun `onClose removes a player from the game`() {
        every { mockWsContext.pathParam(":gameId") } returns "onClose-test"

        var game = Game(name = "onClose-test", gameId = "onClose-test")
        GameController.games.add(game)
        game.addPlayer(mockWsContext, "test-player")

        assertEquals(1, game.players)

        GameController.onClose(mockWsContext)

        assertEquals(0, game.players)
    }
}
