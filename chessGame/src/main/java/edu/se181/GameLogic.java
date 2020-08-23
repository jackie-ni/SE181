package edu.se181;

import java.util.List;

public class GameLogic {
    private Board board;
    private boolean finished;
    private int finishState;
    private int drawCondition;
    private int checkState;
    private List<Square> checkSquares;

    public GameLogic(Board b) {
        board = b;
    }

    public Board getBoard(){
        return board;
    }

    public Move convertToMove(String notation){
        boolean isCapture = false;
        String startSquare, destSqaure;
        int rank, file;
        Piece piece;
        Piece promotedPiece;
        String lastChar;

        if (notation.contains("O") ){
            startSquare = notation.substring(1,3);
            String castle = notation.substring(3);
            piece = board.getSquareByNotation(startSquare).getOccupant();
            if (castle.equals("O-O-O")){
                return new CastleMove((King) piece, false);
            }
            else if (castle.equals("O-O")){
                return new CastleMove((King)piece, true);
            }
        }

        if (notation.contains("x")){
            isCapture = true;
            notation = notation.replace("x", "");
        }

        if (Character.isUpperCase(notation.charAt(0))){
            notation = notation.substring(1);
        }

        startSquare = notation.substring(0,2);
        destSqaure = notation.substring(2,4);

        piece = board.getSquareByNotation(startSquare).getOccupant();

        file = board.getSquareByNotation(destSqaure).getFile();
        rank = board.getSquareByNotation(destSqaure).getRank();

        if (notation.contains("=")){
            lastChar = notation.substring(notation.length()-1);
            promotedPiece = null;
            if(lastChar.equals("R")){
                promotedPiece = new Rook(rank, file, piece.isWhite());
            }
            if(lastChar.equals("N")){
                promotedPiece = new Knight(rank, file, piece.isWhite());
            }
            if(lastChar.equals("B")){
                promotedPiece = new Bishop(rank, file, piece.isWhite());
            }
            if(lastChar.equals("Q")){
                promotedPiece = new Queen(rank, file, piece.isWhite());
            }
            return new PromoteMove((Pawn)piece, rank, file, promotedPiece);
        }

        if (notation.contains("e.p.")){
            return new EnPassantMove((Pawn)piece, rank, file);
        }

        return new RegularMove(piece, rank, file, isCapture);
    }

    public String convertToNotation(Move move){
        Piece piece = move.getPiece();
        String notation = "";

        if (piece instanceof Rook){
            notation += "R";
        }
        if (piece instanceof Knight){
            notation += "N";
        }
        if (piece instanceof Bishop){
            notation += "B";
        }
        if (piece instanceof Queen){
            notation += "Q";
        }
        if (piece instanceof King){
            notation += "K";
        }

        notation += (char) (97+piece.getFile());
        notation += String.valueOf(piece.getRank()+1);

        if (move instanceof CastleMove){
            if (((CastleMove) move).isKingSide){
                return notation + "O-O";
            }else{
                return notation + "O-O-O";
            }
        }

        if (move.isCapture()){
            notation += "x";
        }

        notation += (char) (97+move.getFileDest());
        notation += String.valueOf(move.getRankDest()+1);

        if (move instanceof PromoteMove){
            notation += "=";
            Piece promotedPiece = ((PromoteMove) move).promotePiece;
            if (promotedPiece instanceof Rook){
                notation += "R";
            }
            if (promotedPiece instanceof Knight){
                notation += "N";
            }
            if (promotedPiece instanceof Bishop){
                notation += "B";
            }
            if (promotedPiece instanceof Queen){
                notation += "Q";
            }
        }

        if (move instanceof EnPassantMove){
            notation += "e.p.";
        }

        return notation;
    }
}
