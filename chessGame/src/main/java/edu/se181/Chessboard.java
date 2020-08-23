package edu.se181;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//Visual creation of the chessboard
public class Chessboard {
    private static final Image frog = new Image("/images/frug.png");
    private static final Image bPawn = new Image("/images/blackPawn.png");

    final int[] clicked = {0,0};    //row,col in GridPane (chessboard) where user clicked

    public ImageView selectedPiece = null;
    public GridPane chessBoard;

    public Chessboard(){
        createChessBoard();
    }

    public void setSelectedPiece(ImageView piece){
        this.selectedPiece = piece;
    }
    public ImageView getSelectedPiece(){
        return this.selectedPiece;
    }

    public void createChessBoard() {
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
                square.setOnMouseClicked(e->{
                    clicked[0] = GridPane.getRowIndex(square);
                    clicked[1] = GridPane.getColumnIndex(square);
                    move(clicked[1],clicked[0]);
                    //selectedPiece = null;
                });
                square.getChildren().addAll(background);
                chessBoard.add(square, col, row);
            }
        }
        setPieces(chessBoard);
        this.chessBoard = chessBoard;
        //return chessBoard;
    }


    public void setPieces(GridPane board){
        //TODO Initialize all pieces and set to starting position
//        ImageView iv = new ImageView(frog);
//        ImageView bp = new ImageView(bPawn);
//        iv.setFitWidth(40);
//        iv.setFitHeight(40);
//        iv.setPreserveRatio(true);
//        bp.setOnMouseClicked(e->{
//            setSelectedPiece(bp);
//        });
//        iv.setOnMouseClicked(e->{
//            setSelectedPiece(iv);
//        });
        Sprite bp = new Sprite(bPawn);
        Sprite frug = new Sprite(frog);
        frug.setSize(40,40);
        bp.setOnMouseClicked(e->{
            setSelectedPiece(bp);
        });

        GridPane.setConstraints(frug,0,0);
        GridPane.setConstraints(bp,4,4);
        board.getChildren().addAll(bp,frug);
    }

    public void move(int x, int y){
        if(getSelectedPiece()==null){
            return ;
        }
        //TODO Add check for legality here
        GridPane.setConstraints(getSelectedPiece(),x,y);
        setSelectedPiece(null);
    }


    public void highlight(ImageView piece){
        int y = GridPane.getRowIndex(piece);
        int x = GridPane.getColumnIndex(piece);
        Rectangle nSquare = new Rectangle(50,50);
        nSquare.setFill(Color.TRANSPARENT);

        StackPane newSquare = new StackPane();
        newSquare.getChildren().addAll(nSquare);
        newSquare.setBorder(new Border(new BorderStroke(Color.BLUE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        chessBoard.add(newSquare,x,y);
    }

}
