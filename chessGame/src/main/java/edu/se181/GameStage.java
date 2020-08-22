package edu.se181;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;

//Contain the chessboard, capture piece boxes, draw/surrender buttons
public class GameStage {

    public static void launch(Stage stage) {

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
        bottom.getChildren().addAll(surrender, draw);

        MainApp.update(layout);
    }
}
