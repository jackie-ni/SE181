package edu.se181

import edu.se181.ReusedStrings.CHESS
import edu.se181.ReusedStrings.SPLASH_SCREEN_PATH
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class MainApp : Application() {

    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource(SPLASH_SCREEN_PATH))
        val scene = Scene(root)
        stage = primaryStage
        stage.title = CHESS
        stage.scene = scene
        stage.show()
    }

    companion object {
        private lateinit var stage: Stage

        fun updateStage(newRoot: Parent) {
            stage.scene.root = newRoot
        }

        fun exitApp() {
            stage.close()
        }
    }
}
