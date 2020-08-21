package edu.se181;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


//Visual creation of the chessboard
public class Chessboard {
    public static GridPane createChessBoard() {

        GridPane chessBoard = new GridPane();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int currentRow = row;
                int currentCol = col;

                Rectangle background = new Rectangle(50, 50);
                StackPane square = new StackPane();
                Color color = Color.GRAY;
                if ((row + col) % 2 == 0) {
                    color = Color.BEIGE;
                }
                background.setFill(color);
                square.getChildren().addAll(background);
                square.setOnMouseClicked((e) -> {
                    System.out.println(String.format("Row: %d\tCol:%d", currentRow+1, currentCol+1));
                });
                chessBoard.add(square, col, row);
            }
        }
        return chessBoard;
    }


}
