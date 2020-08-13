package edu.se181;

import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class GameStage {
    public static void launch(Stage stage) {
        stage.setTitle("Chess Game");

        GridPane chessBoard = new GridPane();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle square = new Rectangle(50, 50);

                Color color = Color.BLACK;
                if ((row + col) % 2 == 0) {
                    color = Color.WHITE;
                }
                square.setFill(color);

                chessBoard.add(square, col, row);
//                square.widthProperty().bind(ChessBoard.widthProperty().divide(8));
//                square.heightProperty().bind(ChessBoard.heightProperty().divide(8));
            }
        }
        Scene scene = new Scene(chessBoard, 400, 400);
        stage.setScene(scene);
        stage.show();
    }
}