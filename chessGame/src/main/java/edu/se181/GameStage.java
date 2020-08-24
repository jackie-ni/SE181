package edu.se181;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

//Contain the chessboard, capture piece boxes, draw/surrender buttons
public class GameStage {
    public static ArrayList<Sprite> capturedWhitePieces;
    public static ArrayList<Sprite> capturedBlackPieces;
    public static HBox top, bottom;
    public static CaptureBox left, right;

    public static void launch(boolean white) {
        BorderPane layout = new BorderPane();
        Game game = new Game(white);
        Chessboard cb = new Chessboard(game);
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
        surrender.setOnAction(e -> {
            game.resign();
        });
        Button draw = new Button("Draw");
        draw.setOnAction(e -> {
            game.offerDraw();
        });
        bottom.setAlignment(Pos.CENTER);
        bottom.getChildren().addAll(surrender, draw);

        MainApp.Companion.updateStage(layout);
    }

    public static void endGame(int winner, int drawCondition) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Finished");
        alert.setHeaderText("The game has ended.");
        String contentText;
        if (winner == -1)
            contentText = "Black is the winner!";
        else if (winner == -2)
            contentText = "White forfeited!";
        else if (winner == 1)
            contentText = "White is the winner!";
        else if (winner == 2)
            contentText = "Black forfeited!";
        else {
            if (drawCondition == 0)
                contentText = "Draw by stalemate!";
            else if (drawCondition == 1)
                contentText = "Draw by insufficient material!";
            else if (drawCondition == 2)
                contentText = "Draw by threefold repetition!";
            else if (drawCondition == 3)
                contentText = "Draw by fifty-move rule!";
            else
                contentText = "You and your opponent agreed to a draw!";
        }
        alert.setContentText(contentText);
        alert.showAndWait();
        Parent root;
        try {
            root = FXMLLoader.load(GameStage.class.getResource(StringSources.INSTANCE.getMAIN_MENU_SCREEN_PATH()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        MainApp.Companion.updateStage(root);
    }

    public static boolean offerDraw() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Draw Offer");
        alert.setHeaderText("Your opponent has offered a draw.");
        alert.setContentText("Do you want to accept the draw?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

}
