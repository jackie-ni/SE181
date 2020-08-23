package edu.se181

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.ListView

class FindGameScreen {



    @FXML
    private fun makeGameButtonAction(event: ActionEvent) {
        //todo
    }

    @FXML
    private fun backButtonAction(event: ActionEvent) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource(StringSources.MAIN_MENU_SCREEN_PATH))
        MainApp.updateStage(root)
    }

    companion object {
        private val LISTVIEW_ID = "#matches"
        private lateinit var listview: ListView<String>

        fun initialize() {
           listview = MainApp.returnObjectFromScene(LISTVIEW_ID) as ListView<String>
        }
    }
}
