package edu.se181;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.StageStyle;

import java.io.File;

//Contain the chessboard, capture piece boxes, draw/surrender buttons
public class GameStage {
    private static final String CHESS_GAME = "Chess Game";

    public static void launch(Stage stage) {
        stage.setTitle(CHESS_GAME);

        BorderPane layout = new BorderPane();
        GridPane chessBoard = Chessboard.createChessBoard();

        chessBoard.setGridLinesVisible(true);
        HBox top = new HBox();
        HBox bottom = new HBox(20);
        VBox left = new VBox();
        VBox right = new VBox();

        left.setPrefHeight(600);
        left.setPrefWidth(100);
        right.setPrefHeight(600);
        right.setPrefWidth(100);
        top.setPrefHeight(100);
        top.setPrefWidth(600);
        bottom.setPrefHeight(100);
        bottom.setPrefWidth(600);

        layout.setCenter(chessBoard);
        layout.setTop(top);
        layout.setBottom(bottom);
        layout.setLeft(left);
        layout.setRight(right);

        //TODO: Implement Surrender/Draw button functions
        Button surrender = new Button("Surrender");
        Button draw = new Button("Draw");
        bottom.setAlignment(Pos.CENTER);
        bottom.getChildren().addAll(surrender,draw);

        Scene scene = new Scene(layout, 600, 600);
        stage.setScene(scene);
        stage.show();
    }
}
