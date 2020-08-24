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
                //bug where passwords with whitespace won't work
                val password = FindGameSceenDialogs.passwordDialog()
                if (password.isPresent) {
                    //create match properties send to server
                    val gameId = HttpUtil.makeGame(MatchProperties(
                            name = matchName.get(),
                            password = password.get(),
                            private = true
                    ))
                    //connect game
                    HttpUtil.connect(gameId, password.get())
                    //launch game stage
                    GameStage.launch(true)
                }
            }
        } else if (isPrivate == NO) {
            val matchName = FindGameSceenDialogs.nameDialog()
            if (matchName.isPresent) {
                //create match properties send to server
                val gameId = HttpUtil.makeGame(MatchProperties(name = matchName.get()))
                //connect game
                HttpUtil.connect(gameId)
                //launch game stage
                GameStage.launch(true)
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
        private lateinit var listOfGames: List<MatchProperties>

        fun initialize() {
            //get matches from server and load them into listview
            //filter for not full games
            listOfGames = HttpUtil.getGames().filter { games -> games.players < 2 }

            listview = MainApp.returnObjectFromScene(LISTVIEW_ID) as ListView<MatchProperties>
            listview.setCellFactory { MatchCell() }
            listview.setOnMouseClicked { event: MouseEvent? ->
                if (listview.focusModel.focusedItem.private) {
                    val password = FindGameSceenDialogs.joinPrivateGameDialog()
                    if (password.isPresent) {
                        //call to server to checkpassword

                        val isCorrectPassword =
                                HttpUtil.checkPassword(listview.focusModel.focusedItem.gameId, password.get())

                        if (!isCorrectPassword) {
                            FindGameSceenDialogs.wrongPasswordDialog()
                        } else {
                            //connect game
                            HttpUtil.connect(listview.focusModel.focusedItem.gameId, password.get())
                            //launch game stage
                            GameStage.launch(false)
                        }
                    }
                } else {
                    val joinGame = FindGameSceenDialogs.joinGameDialog()
                    if (joinGame == YES) {
                        //connect game
                        HttpUtil.connect(listview.focusModel.focusedItem.gameId)
                        //launch game stage
                        GameStage.launch(false)
                    }
                }
            }
            listview.items.addAll(listOfGames)
        }

        private class MatchCell : ListCell<MatchProperties>() {
            override fun updateItem(item: MatchProperties?, empty: Boolean) {
                super.updateItem(item, empty)

                text = if (empty || item == null) {
                    null
                } else {
                    item.name
                }
            }
        }
    }
}
