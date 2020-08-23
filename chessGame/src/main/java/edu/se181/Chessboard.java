package edu.se181;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//Visual creation of the chessboard
public class Chessboard {

    public static GridPane createChessBoard() {

        GridPane chessBoard = new GridPane();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle background = new Rectangle(50, 50);
                StackPane square = new StackPane();
                Color color = Color.GRAY;
                if ((row + col) % 2 == 0) {
                    color = Color.BEIGE;
                }
                background.setFill(color);
                square.getChildren().addAll(background);
                chessBoard.add(square, col, row);
            }
        }
        return chessBoard;
    }


}
