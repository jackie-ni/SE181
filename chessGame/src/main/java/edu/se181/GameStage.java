package edu.se181;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class GameStage {
    private static final String CHESS_GAME = "Chess Game";
    public static void launch(Stage stage) {
        stage.setTitle(CHESS_GAME);

        GridPane chessBoard = new GridPane();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle square = new Rectangle(50, 50);

                Color color = Color.GRAY;
                if ((row + col) % 2 == 0) {
                    color = Color.BEIGE;
                }
                square.setFill(color);
                chessBoard.add(square, col, row);
            }
        }
        Scene scene = new Scene(chessBoard, 500, 500);
        stage.setScene(scene);
        stage.show();
    }
}
