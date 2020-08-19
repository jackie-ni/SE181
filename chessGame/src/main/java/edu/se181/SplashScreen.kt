package edu.se181

import edu.se181.ReusedStrings.MAIN_MENU_SCREEN_PATH
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent

class SplashScreen {

    @FXML
    private fun startButtonAction(event: ActionEvent) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource(MAIN_MENU_SCREEN_PATH))
        MainApp.updateStage(root)
    }
}
