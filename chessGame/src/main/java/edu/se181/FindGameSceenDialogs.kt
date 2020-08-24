package edu.se181

import javafx.event.ActionEvent
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.TextInputDialog
import java.util.*

object FindGameSceenDialogs {
    private val DIALOG_TITLE = "Make Game"
    private val PRIAVTE_DIALOG_HEADER_TEXT = "Do you want your game to be a private game?"
    private val PRIAVTE_DIALOG_CONTENT_TEXT = "Select Yes or No."
    private val YES = "Yes"
    private val NO = "No"
    private val CANCEL = "Cancel"
    private val NAME_DIALOG_HEADER_TEXT = "Enter the name for your game. Input cannot be empty."
    private val NAME_DIALOG_CONTENT_TEXT = "Game name:"
    private val PASSWORD_DIALOG_HEADER_TEXT = "Enter the password for your game. Input cannot be empty."
    private val PASSWORD_DIALOG_CONTENT_TEXT = "Password:"
    private val JOIN_GAME_TITLE = "Join Game"
    private val JOIN_GAME_HEADER_TEXT = "Join this game?"
    private val JOIN_PRIVATE_GAME_CONTENT_TEXT = "Please enter the the password for this game. The input cannot be empty."
    private val WRONG_PASSWORD = "You entered the wrong password!"
    private val ERROR = "ERROR!"
    private val yesButton = ButtonType(YES)
    private val noButton = ButtonType(NO)
    private val cancelButton = ButtonType(CANCEL, ButtonBar.ButtonData.CANCEL_CLOSE)

    fun initialDialog(): String {
        return Alert(Alert.AlertType.CONFIRMATION).apply {
            title = DIALOG_TITLE
            headerText = PRIAVTE_DIALOG_HEADER_TEXT
            contentText = PRIAVTE_DIALOG_CONTENT_TEXT
            buttonTypes.setAll(yesButton, noButton, cancelButton)
        }.showAndWait().yesOrNo()
    }

    fun nameDialog(): Optional<String> {
        var button: Node
        return TextInputDialog().apply {
            title = DIALOG_TITLE
            headerText = NAME_DIALOG_HEADER_TEXT
            contentText = NAME_DIALOG_CONTENT_TEXT
            button = dialogPane.lookupButton(ButtonType.OK)
            button.addEventFilter(ActionEvent.ACTION) { event: ActionEvent? -> event.filter(editor.text) }
        }.showAndWait()
    }

    fun passwordDialog(): Optional<String> {
        var button: Node
        return TextInputDialog().apply {
            title = DIALOG_TITLE
            headerText = PASSWORD_DIALOG_HEADER_TEXT
            contentText = PASSWORD_DIALOG_CONTENT_TEXT
            button = dialogPane.lookupButton(ButtonType.OK)
            button.addEventFilter(ActionEvent.ACTION) { event: ActionEvent? -> event.filter(editor.text) }
        }.showAndWait()
    }

    fun joinPrivateGameDialog(): Optional<String> {
        var button: Node
        return TextInputDialog().apply {
            title = JOIN_GAME_TITLE
            headerText = JOIN_GAME_HEADER_TEXT
            contentText = JOIN_PRIVATE_GAME_CONTENT_TEXT
            button = dialogPane.lookupButton(ButtonType.OK)
            button.addEventFilter(ActionEvent.ACTION) { event: ActionEvent? -> event.filter(editor.text) }
        }.showAndWait()
    }

    fun joinGameDialog(): String {
        return Alert(Alert.AlertType.CONFIRMATION).apply {
            title = JOIN_GAME_TITLE
            headerText = JOIN_GAME_HEADER_TEXT
            buttonTypes.setAll(yesButton, noButton)
        }.showAndWait().yesOrNo()
    }

    fun wrongPasswordDialog() {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = ERROR
        alert.headerText = null
        alert.contentText = WRONG_PASSWORD
        alert.showAndWait()
    }

    private fun ActionEvent?.filter(input: String) {
        if (!isValid(input)) {
            this?.consume()
        }
    }

    private fun isValid(input: String) = input.trim() != ""

    private fun Optional<ButtonType>.yesOrNo(): String {
        return when {
            this.get() == yesButton -> YES
            this.get() == noButton -> NO
            else -> CANCEL

        }
    }
}
