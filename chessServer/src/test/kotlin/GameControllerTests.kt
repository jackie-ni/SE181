import org.junit.Assert.*
import io.javalin.Javalin
import io.javalin.plugin.json.JavalinJson
import kong.unirest.Unirest
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class GameControllerTests {

    companion object {

        private lateinit var app: Javalin

        private var testGames = mutableListOf<Game>(
            Game("test1", false, "test-game-1", 0, ""),
            Game("test2", true, "test-game-2", 0, "wet-ass-p-word")
        )

        private var testGamesJson = JavalinJson.toJson(testGames)

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
    fun `GET to fetch games returns list of games`() {
        val response = Unirest.get("http://localhost:6969/games").asString()
        assertEquals(200, response.status)
        assertEquals(testGamesJson, response.body)
    }
}