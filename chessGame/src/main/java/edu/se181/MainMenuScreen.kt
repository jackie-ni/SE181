package edu.se181

import edu.se181.ReusedStrings.ABOUT_SCREEN_PATH
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent

class MainMenuScreen {

    @FXML
    private fun findGameButtonAction(event: ActionEvent) {
        //todo
    }

    @FXML
    private fun aboutButtonAction(event: ActionEvent) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource(ABOUT_SCREEN_PATH))
        MainApp.updateStage(root)
    }

    @FXML
    private fun exitButtonAction(event: ActionEvent) {
        MainApp.exitApp()
    }
}
