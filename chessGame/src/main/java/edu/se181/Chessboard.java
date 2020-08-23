package edu.se181;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;

//Visual creation of the chessboard
public class Chessboard {

    final int[] clicked = {0,0};    //row,col in GridPane (chessboard) where user clicked

    public ImageView selectedPiece = null;
    public GridPane chessBoard;
    public ArrayList<Sprite> blackPieces = new ArrayList<>();
    public ArrayList<Sprite> whitePieces = new ArrayList<>();
    private boolean whiteTurn = true;

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
                });
                square.getChildren().addAll(background);
                chessBoard.add(square, col, row);
            }
        }
        setPieces(chessBoard);
        this.chessBoard = chessBoard;
    }


    public void setPieces(GridPane board){
        //Initialize all pieces and set to starting position
        Sprite blackKnight1 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_KNIGHT()));
        Sprite blackKnight2 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_KNIGHT()));
        Sprite blackBishop1 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_BISHOP()));
        Sprite blackBishop2 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_BISHOP()));
        Sprite blackRook1 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_ROOK()));
        Sprite blackRook2 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_ROOK()));
        Sprite blackQueen = new Sprite(new Image(StringSources.INSTANCE.getBLACK_QUEEN()));
        Sprite blackKing = new Sprite(new Image(StringSources.INSTANCE.getBLACK_KING()));
        Sprite blackPawn1 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_PAWN()));
        Sprite blackPawn2 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_PAWN()));
        Sprite blackPawn3 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_PAWN()));
        Sprite blackPawn4 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_PAWN()));
        Sprite blackPawn5 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_PAWN()));
        Sprite blackPawn6 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_PAWN()));
        Sprite blackPawn7 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_PAWN()));
        Sprite blackPawn8 = new Sprite(new Image(StringSources.INSTANCE.getBLACK_PAWN()));

        Sprite whiteKnight1 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_KNIGHT()));
        Sprite whiteKnight2 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_KNIGHT()));
        Sprite whiteBishop1 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_BISHOP()));
        Sprite whiteBishop2 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_BISHOP()));
        Sprite whiteRook1 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_ROOK()));
        Sprite whiteRook2 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_ROOK()));
        Sprite whiteKing = new Sprite(new Image(StringSources.INSTANCE.getWHITE_KING()));
        Sprite whiteQueen = new Sprite(new Image(StringSources.INSTANCE.getWHITE_QUEEN()));
        Sprite whitePawn1 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_PAWN()));
        Sprite whitePawn2 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_PAWN()));
        Sprite whitePawn3 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_PAWN()));
        Sprite whitePawn4 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_PAWN()));
        Sprite whitePawn5 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_PAWN()));
        Sprite whitePawn6 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_PAWN()));
        Sprite whitePawn7 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_PAWN()));
        Sprite whitePawn8 = new Sprite(new Image(StringSources.INSTANCE.getWHITE_PAWN()));

        blackPieces.addAll(Arrays.asList(blackRook1,blackKnight1,blackBishop1,blackQueen,blackKing,blackBishop2,
                blackKnight2,blackRook2,blackPawn1,blackPawn2,blackPawn3,blackPawn4,blackPawn5,blackPawn6,blackPawn7,
                blackPawn8));
        whitePieces.addAll(Arrays.asList(whiteRook1,whiteKnight1,whiteBishop1,whiteQueen,whiteKing,whiteBishop2,
                whiteKnight2,whiteRook2,whitePawn1,whitePawn2,whitePawn3,whitePawn4,whitePawn5,whitePawn6,whitePawn7,
                whitePawn8));

        for(int col=0;col<8;col++){
            for(int row=0;row<2;row++){
                int num;
                if(row>0){
                    num = 8+col;
                }
                else {
                    num = ((row + 1) * (col + 1)) - 1;
                }
                GridPane.setConstraints(blackPieces.get(num),col,row);
            }
        }

        GridPane.setConstraints(whiteBishop1,2,7);
        GridPane.setConstraints(whiteBishop2,5,7);
        GridPane.setConstraints(whiteRook1,0,7);
        GridPane.setConstraints(whiteRook2,7,7);
        GridPane.setConstraints(whiteKnight1,1,7);
        GridPane.setConstraints(whiteKnight2,6,7);
        GridPane.setConstraints(whiteKing,4,7);
        GridPane.setConstraints(whiteQueen,3,7);

        for(int col=0;col<8;col++){
            GridPane.setConstraints(whitePieces.get(col+8),col,6);
        }

        for(Sprite sprites: blackPieces){
            sprites.setOnMouseClicked(e->{
                setSelectedPiece(sprites);
            });
        }
        for(Sprite sprites: whitePieces){
            sprites.setOnMouseClicked(e->{
                setSelectedPiece(sprites);
            });
        }
        board.getChildren().addAll(blackPieces);
        board.getChildren().addAll(whitePieces);

    }

    public void move(int x, int y){
        if(getSelectedPiece()==null || (whiteTurn && blackPieces.contains((Sprite)getSelectedPiece())) ||
                (!whiteTurn && whitePieces.contains((Sprite)getSelectedPiece()))){
            return ;
        }
        if(whiteTurn){
            for(Sprite piece: blackPieces){
                if(GridPane.getRowIndex(piece)==y && GridPane.getColumnIndex(piece)==x){
                    remove(piece);
                }
            }
        }
        else{
            for(Sprite piece: whitePieces){
                if(GridPane.getRowIndex(piece)==y && GridPane.getColumnIndex(piece)==x){
                    remove(piece);
                }
            }
        }
        //TODO Add check for legality here
        GridPane.setConstraints(getSelectedPiece(),x,y);
        setSelectedPiece(null);
        whiteTurn = !whiteTurn;
    }

    public void remove(ImageView piece){
        chessBoard.getChildren().remove(piece);
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
