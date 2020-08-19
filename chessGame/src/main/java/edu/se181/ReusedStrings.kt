package edu.se181

import java.io.File

object ReusedStrings {
    private val FILE_SEPARATOR = File.separator
    private val FXML = "fxml"
    private val MAIN_MENU = "MainMenuScreen.fxml"
    private val SPLASH_SCREEN =  "SplashScreen.fxml"
    private val ABOUT = "AboutScreen.fxml"
    val ABOUT_SCREEN_PATH = "$FILE_SEPARATOR$FXML$FILE_SEPARATOR$ABOUT"
    val SPLASH_SCREEN_PATH = "$FILE_SEPARATOR$FXML$FILE_SEPARATOR$SPLASH_SCREEN"
    val CHESS = "Chess"
    val MAIN_MENU_SCREEN_PATH = "$FILE_SEPARATOR$FXML$FILE_SEPARATOR$MAIN_MENU"
}
