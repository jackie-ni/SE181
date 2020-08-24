package edu.se181;

import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//Visual creation of the chessboard
public class Chessboard {

    final int[] original = {0,0};
    final int[] clicked = {0,0};    //row,col in GridPane (chessboard) where user clicked

    public Sprite selectedPiece = null;
    public List<Move> selectedLegalMoves = new ArrayList<>();
    public GridPane chessBoard;
    public ArrayList<Sprite> blackPieces = new ArrayList<>();
    public ArrayList<Sprite> whitePieces = new ArrayList<>();
    public ArrayList<Sprite> capturedWhitePieces = new ArrayList<>();
    public ArrayList<Sprite> capturedBlackPieces = new ArrayList<>();
    public CaptureBox whiteCaptured = new CaptureBox();
    public CaptureBox blackCaptured = new CaptureBox();
    private boolean whiteTurn = true;
    private Game game = new Game();

    public Chessboard(){
        createChessBoard();
        HttpUtil.INSTANCE.setGame(game);
        game.setChessboard(this);
    }

    public void setSelectedPiece(Sprite piece){
        this.selectedPiece = piece;
    }
    public Sprite getSelectedPiece(){
        return this.selectedPiece;
    }
    public ArrayList<Sprite> getCapturedWhitePieces(){
        return this.capturedWhitePieces;
    }
    public ArrayList<Sprite> getCapturedBlackPieces(){
        return this.capturedBlackPieces;
    }
    public void setSelectedLegalMoves(List<Move> moves){
        this.selectedLegalMoves=moves;
    }
    public List<Move> getSelectedLegalMoves(){
        return this.selectedLegalMoves;
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
            sprites.setPosition(GridPane.getColumnIndex(sprites),GridPane.getRowIndex(sprites));
            addHandler(sprites, false);
        }
        for(Sprite sprites: whitePieces){
            sprites.setPosition(GridPane.getColumnIndex(sprites),GridPane.getRowIndex(sprites));
            addHandler(sprites, true);
        }
        board.getChildren().addAll(blackPieces);
        board.getChildren().addAll(whitePieces);

    }

    public void addHandler(Sprite sprite, boolean forWhite) {
        if (forWhite) {
            sprite.setOnMouseClicked(e->{
                if(!whiteTurn && blackPieces.contains(getSelectedPiece())){
                    move(sprite.getXPos(),sprite.getYPos());
                }
                else if (whiteTurn){
                    if (selectedPiece == null){
                        highlightSquare(sprite.getXPos(), sprite.getYPos());
                    }else{
                        unhighlightSquare(selectedPiece.getXPos(), selectedPiece.getYPos());
                        for(Move move : selectedLegalMoves){
                            unhighlightSquare(move.getFileDest(), 7 - move.getRankDest());
                        }
                        highlightSquare(sprite.getXPos(), sprite.getYPos());
                    }

                    setSelectedPiece(sprite);
                    setSelectedLegalMoves(getLegalMoves(sprite));
                    for(Move move : selectedLegalMoves){
                        highlightSquare(move.getFileDest(), 7 - move.getRankDest());
                    }

                }
            });
        } else {
            sprite.setOnMouseClicked(e->{
                if(whiteTurn && whitePieces.contains(getSelectedPiece())){
                    move(sprite.getXPos(),sprite.getYPos());
                }
                else if (!whiteTurn){
                    if (selectedPiece == null){
                        highlightSquare(sprite.getXPos(), sprite.getYPos());
                    }else{
                        unhighlightSquare(selectedPiece.getXPos(), selectedPiece.getYPos());
                        for(Move move : selectedLegalMoves){
                            unhighlightSquare(move.getFileDest(), 7 - move.getRankDest());
                        }
                        highlightSquare(sprite.getXPos(), sprite.getYPos());
                    }
                    setSelectedPiece(sprite);
                    setSelectedLegalMoves(getLegalMoves(sprite));
                    for(Move move : selectedLegalMoves){
                        highlightSquare(move.getFileDest(), 7 - move.getRankDest());
                    }
                }

            });
        }
    }

    public void move(int x, int y){
        if(getSelectedPiece()==null || (whiteTurn && blackPieces.contains(getSelectedPiece())) ||
                (!whiteTurn && whitePieces.contains(getSelectedPiece()))){
            return ;
        }
        List<Move> moves = getSelectedLegalMoves().stream().filter((Move m) -> m.getRankDest() == 7 - y && m.getFileDest() == x).collect(Collectors.toList());
        Move move;
        if (moves.isEmpty()) {
            unhighlightSquare(getSelectedPiece().getXPos(), getSelectedPiece().getYPos());
            for(Move oldMove : selectedLegalMoves){
                unhighlightSquare(oldMove.getFileDest(), 7 - oldMove.getRankDest());
            }
            setSelectedPiece(null);
            getSelectedLegalMoves().clear();
            return ;
        } else
            move = moves.get(0);

        if (move instanceof EnPassantMove) {
            if (whiteTurn) {
                Sprite pawn = blackPieces.stream().filter((Sprite s) -> s.getXPos() == x && s.getYPos() == y + 1).collect(Collectors.toList()).get(0);
                remove(pawn);
            } else {
                Sprite pawn = whitePieces.stream().filter((Sprite s) -> s.getXPos() == x && s.getYPos() == y - 1).collect(Collectors.toList()).get(0);
                remove(pawn);
            }
        }

        unhighlightSquare(getSelectedPiece().getXPos(), getSelectedPiece().getYPos());
        for(Move move2 : selectedLegalMoves){
            unhighlightSquare(move2.getFileDest(), 7 - move2.getRankDest());
        }
        game.makeMove(move);


        if(move instanceof CastleMove){
            //move rook
            if(((CastleMove) move).isKingSide){
                if(whiteTurn) {
                    Sprite rook = whitePieces.stream().filter((Sprite s) -> s.getXPos() == 7 && s.getYPos() == 7).collect(Collectors.toList()).get(0);
                    GridPane.setConstraints(rook,5,7);
                    rook.setPosition(5,7);
                }
                else{
                    Sprite rook = blackPieces.stream().filter((Sprite s) -> s.getXPos() == 7 && s.getYPos() == 0).collect(Collectors.toList()).get(0);
                    GridPane.setConstraints(rook,5,0);
                    rook.setPosition(5,0);
                }
            }
            else{
                if(whiteTurn){
                    Sprite rook = whitePieces.stream().filter((Sprite s) -> s.getXPos() == 0 && s.getYPos() == 7).collect(Collectors.toList()).get(0);
                    GridPane.setConstraints(rook,3,7);
                    rook.setPosition(3,7);
                }
                else{
                    Sprite rook = blackPieces.stream().filter((Sprite s) -> s.getXPos() == 0 && s.getYPos() == 0).collect(Collectors.toList()).get(0);
                    GridPane.setConstraints(rook,3,0);
                    rook.setPosition(3,0);
                }
            }
            GridPane.setConstraints(getSelectedPiece(),x,y);
        }

        if (move instanceof PromoteMove) {
            List<String> promotePieces = new ArrayList<>();
            String promotePieceString;
            promotePieces.add("Queen");
            promotePieces.add("Rook");
            promotePieces.add("Bishop");
            promotePieces.add("Knight");
            ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(promotePieces.get(0), promotePieces);
            choiceDialog.setTitle("Pawn Promotion");
            choiceDialog.setHeaderText("Select piece to promote pawn to");
            Optional<String> result = choiceDialog.showAndWait();
            if (result.isPresent()) {
                promotePieceString = result.get();
            } else {
                setSelectedPiece(null);
                getSelectedLegalMoves().clear();
                return;
            }
            int pieceType = -1;
            for (int i = 0; i < promotePieces.size(); i++) {
                if (promotePieces.get(i).equals(promotePieceString)) {
                    pieceType = i;
                    break;
                }
            }
            Piece promotePiece;
            Sprite promotePieceSprite;
            if (pieceType == 0) {
                promotePiece = new Queen(7 - y, x, whiteTurn);
                if (whiteTurn)
                    promotePieceSprite = new Sprite(new Image(StringSources.INSTANCE.getWHITE_QUEEN()));
                else
                    promotePieceSprite = new Sprite(new Image(StringSources.INSTANCE.getBLACK_QUEEN()));
            } else if (pieceType == 1) {
                promotePiece = new Rook(7 - y, x, whiteTurn);
                if (whiteTurn)
                    promotePieceSprite = new Sprite(new Image(StringSources.INSTANCE.getWHITE_ROOK()));
                else
                    promotePieceSprite = new Sprite(new Image(StringSources.INSTANCE.getBLACK_ROOK()));
            } else if (pieceType == 2) {
                promotePiece = new Bishop(7 - y, x, whiteTurn);
                if (whiteTurn)
                    promotePieceSprite = new Sprite(new Image(StringSources.INSTANCE.getWHITE_BISHOP()));
                else
                    promotePieceSprite = new Sprite(new Image(StringSources.INSTANCE.getBLACK_BISHOP()));
            } else {
                promotePiece = new Knight(7 - y, x, whiteTurn);
                if (whiteTurn)
                    promotePieceSprite = new Sprite(new Image(StringSources.INSTANCE.getWHITE_KNIGHT()));
                else
                    promotePieceSprite = new Sprite(new Image(StringSources.INSTANCE.getBLACK_KNIGHT()));
            }
            ((PromoteMove) move).setPromotePiece(promotePiece);
            GridPane.setConstraints(promotePieceSprite, x, y);
            if (whiteTurn) {
                whitePieces.remove(getSelectedPiece());
                whitePieces.add(promotePieceSprite);
                System.out.println("setting event handler");
            } else {
                blackPieces.remove(getSelectedPiece());
                blackPieces.add(promotePieceSprite);
                System.out.println("setting event handler");
            }
            addHandler(promotePieceSprite, whiteTurn);
            chessBoard.getChildren().remove(getSelectedPiece());
            chessBoard.getChildren().add(promotePieceSprite);
            setSelectedPiece(promotePieceSprite);
        }

        if(whiteTurn){
            for(Sprite piece: blackPieces){
                if(GridPane.getRowIndex(piece)==y && GridPane.getColumnIndex(piece)==x){
                    remove(piece);
                    break;
                }
            }
        }
        else{
            for(Sprite piece: whitePieces){
                if(GridPane.getRowIndex(piece)==y && GridPane.getColumnIndex(piece)==x){
                    remove(piece);
                    break;
                }
            }
        }

        GridPane.setConstraints(getSelectedPiece(), x, y);
        getSelectedPiece().setPosition(x, y);
        unhighlightSquare(getSelectedPiece().getXPos(), getSelectedPiece().getYPos());
        for(Move move1 : selectedLegalMoves){
            unhighlightSquare(move1.getFileDest(), 7 - move1.getRankDest());
        }
        setSelectedPiece(null);
        getSelectedLegalMoves().clear();
        whiteTurn = !whiteTurn;

        game.makeMove(move);
    }

    public void remove(Sprite piece){
        chessBoard.getChildren().remove(piece);
        if(whiteTurn){
            blackPieces.remove(piece);
            capturedBlackPieces.add(piece);
            blackCaptured.showPiece(piece);
        }
        else{
            whitePieces.remove(piece);
            capturedWhitePieces.add(piece);
            whiteCaptured.showPiece(piece);
        }
    }

    public void highlightSquare(int x, int y){
        List<Node> nodes = chessBoard.getChildren().filtered((Node s)->
                s instanceof StackPane &&
                        GridPane.getColumnIndex(s)==x &&
                        GridPane.getRowIndex(s)==y);
        ((Rectangle) (((StackPane)nodes.get(0)).getChildren().get(0))).setFill(Color.GREEN);
    }

    public void unhighlightSquare(int x, int y){
        List<Node> nodes = chessBoard.getChildren().filtered((Node s)->
                s instanceof StackPane &&
                        GridPane.getColumnIndex(s)==x &&
                        GridPane.getRowIndex(s)==y);

        if ((x+y) % 2 == 1){
            ((Rectangle) (((StackPane)nodes.get(0)).getChildren().get(0))).setFill(Color.GRAY);
        }
        else{
            ((Rectangle) (((StackPane)nodes.get(0)).getChildren().get(0))).setFill(Color.BEIGE);
        }

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

    public List<Move> getLegalMoves(Sprite sprite){
        List<Move> moves = game.getLegalMoves(7-sprite.getYPos(),sprite.getXPos());
        if (moves.isEmpty())
            System.out.println("No moves"); // debug
        for(Move m : moves) {
            System.out.print((char) (m.getFileDest() + 97));
            System.out.println((char) (m.getRankDest() + 49));
        }
        System.out.println(game.halfMoveClock);
        System.out.println("----");
        return moves;
    }

}
