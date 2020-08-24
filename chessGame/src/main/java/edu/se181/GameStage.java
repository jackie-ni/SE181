package edu.se181;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;

//Contain the chessboard, capture piece boxes, draw/surrender buttons
public class GameStage {
    public static ArrayList<Sprite> capturedWhitePieces;
    public static ArrayList<Sprite> capturedBlackPieces;
    public static HBox top, bottom;
    public static CaptureBox left, right;

    public static void launch() {
        BorderPane layout = new BorderPane();
        Chessboard cb = new Chessboard();
        GridPane chessBoard = cb.chessBoard;
        capturedWhitePieces = cb.getCapturedWhitePieces();
        capturedBlackPieces = cb.blackPieces;

        chessBoard.setGridLinesVisible(true);
        top = new HBox();
        bottom = new HBox(20);
        left = cb.whiteCaptured;
        right = cb.blackCaptured;

        left.setPrefSize(100,600);
        right.setPrefSize(100,600);
        top.setPrefSize(600,100);
        bottom.setPrefSize(600,100);


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

        MainApp.Companion.updateStage(layout);
    }

}
