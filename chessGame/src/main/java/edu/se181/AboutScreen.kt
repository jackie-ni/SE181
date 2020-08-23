package edu.se181

import edu.se181.StringSources.CREDITS
import edu.se181.StringSources.CREDITS_DIALOG
import edu.se181.StringSources.MAIN_MENU_SCREEN_PATH
import edu.se181.StringSources.VERSION
import edu.se181.StringSources.VERSION_DIALOG
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType


class AboutScreen {

    @FXML
    private fun versionButtonAction(event: ActionEvent) {
        val alert = Alert(AlertType.INFORMATION)
        alert.title = VERSION_DIALOG
        alert.headerText = null
        alert.contentText = VERSION

        alert.showAndWait()
    }

    @FXML
    private fun creditsButtonAction(event: ActionEvent) {
        val alert = Alert(AlertType.INFORMATION)
        alert.title = CREDITS_DIALOG
        alert.headerText = null
        alert.contentText = CREDITS

        alert.showAndWait()
    }

    @FXML
    private fun backButtonAction(event: ActionEvent) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource(MAIN_MENU_SCREEN_PATH))
        MainApp.updateStage(root)
    }
}
