package edu.se181

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.*
import javafx.scene.input.MouseEvent

class FindGameScreen {

    @FXML
    private fun makeGameButtonAction(event: ActionEvent) {
        val isPrivate = FindGameSceenDialogs.initialDialog()

        if (isPrivate == YES) {
            val matchName = FindGameSceenDialogs.nameDialog()
            if (matchName.isPresent) {
                val password = FindGameSceenDialogs.passwordDialog()
                if (password.isPresent) {
                    //create match properties send to server
                    //launch game
                    listview.items.add(MatchProperties(
                            matchName = matchName.get(),
                            password = password.get(),
                            isPrivate = true
                    ))
                }
            }
        } else if (isPrivate == NO) {
            val matchName = FindGameSceenDialogs.nameDialog()
            if (matchName.isPresent) {
                //create match properties send to server
                //launch game
                listview.items.add(MatchProperties(matchName = matchName.get()))
            }
        }
    }

    @FXML
    private fun backButtonAction(event: ActionEvent) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource(StringSources.MAIN_MENU_SCREEN_PATH))
        MainApp.updateStage(root)
    }

    companion object {
        private val LISTVIEW_ID = "#matches"
        private val YES = "Yes"
        private val NO = "No"
        private lateinit var listview: ListView<MatchProperties>

        fun initialize() {
            //get matches from server and load them into listview
            listview = MainApp.returnObjectFromScene(LISTVIEW_ID) as ListView<MatchProperties>
            listview.setCellFactory { MatchCell() }
            listview.setOnMouseClicked { event: MouseEvent? ->
                if (listview.focusModel.focusedItem.isPrivate) {
                    val password = FindGameSceenDialogs.joinPrivateGameDialog()
                    if(password.isPresent) {
                        if (password.get() == listview.focusModel.focusedItem.password) {
                            //join game
                        } else {
                            FindGameSceenDialogs.wrongPasswordDialog()
                        }
                    }
                } else {
                    val joinGame = FindGameSceenDialogs.joinGameDialog()
                    if (joinGame == YES) {
                        //joinGame
                    }
                }
            }
            listview.items.add(MatchProperties("matchName"))
        }

        private fun joinGame() {

        }

        private class MatchCell : ListCell<MatchProperties>() {
            override fun updateItem(item: MatchProperties?, empty: Boolean) {
                super.updateItem(item, empty)

                text = if (empty || item == null) {
                    null
                } else {
                    item.matchName
                }
            }
        }
    }
}
