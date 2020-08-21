package edu.se181

import edu.se181.StringSources.MAIN_MENU_SCREEN_PATH
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent

class AboutScreen {

    @FXML
    private fun versionButtonAction(event: ActionEvent) {
        //todo
    }

    @FXML
    private fun creditsButtonAction(event: ActionEvent) {
        //todo
    }

    @FXML
    private fun backButtonAction(event: ActionEvent) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource(MAIN_MENU_SCREEN_PATH))
        MainApp.updateStage(root)
    }
}
