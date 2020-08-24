package edu.se181;

import java.util.HashMap;
import java.util.List;

public class DrawChecker {


    public DrawChecker(){}

    public static int isIrreversibleMove(Move move){
        Piece piece = move.getPiece();
        if (piece instanceof Pawn || move.isCapture()){
            return 1;
        }

        if (piece instanceof Rook || piece instanceof King){
            if (!piece.hasMoved()){
                return 2;
            }
        }

        return 0;
    }

    public static boolean isThreefoldRepetition(List<String> boardStateList, int irreversibleMoveIndex){
        HashMap<String, Integer> whiteBoardStateMap = new HashMap<>();
        HashMap<String, Integer> blackBoardStateMap = new HashMap<>();
        boolean checkWhite;
        String boardState;

        if (boardStateList.size() < 3){
            return false;
        }
        for(int i = irreversibleMoveIndex; i < boardStateList.size(); i++){
            boardState = boardStateList.get(i);
            checkWhite = i  % 2 == 0;

            if (checkWhite){
                whiteBoardStateMap = updateHashMap(whiteBoardStateMap, boardState);
                if (whiteBoardStateMap.get(boardState) == 3){
                    return true;
                }
            }else{
                blackBoardStateMap = updateHashMap(blackBoardStateMap, boardState);
                if (blackBoardStateMap.get(boardState) == 3){
                    return true;
                }
            }
        }
        return false;
    }

    private static HashMap<String, Integer> updateHashMap(HashMap<String, Integer> hashMap, String boardState){
        if (hashMap.containsKey(boardState)){
            hashMap.replace(boardState, hashMap.get(boardState)+1);
        }
        else{
            hashMap.put(boardState, 1);
        }
        return hashMap;
    }

    public static boolean isDeadPosition(Board board){
        //going under the assumption that the pieces always contain the king
        List<Piece> whitePieces = board.getWhitePieces();
        List<Piece> blackPieces = board.getBlackPieces();

        int whitePiecesSize = whitePieces.size();
        int blackPiecesSize = blackPieces.size();

        Piece whiteBishop = null;
        Piece blackBishop= null;
        Piece whiteKnight = null;
        Piece blackKnight = null;

        if (whitePiecesSize > 2 || blackPiecesSize > 2){
            return false;
        }

        for(Piece piece : whitePieces){
            if (piece instanceof Bishop){
                whiteBishop = piece;
            }
            else if (piece instanceof Knight){
                whiteKnight = piece;
            }
        }

        for(Piece piece : blackPieces){
            if (piece instanceof Bishop){
                blackBishop = piece;
            }
            else if (piece instanceof Knight){
                blackKnight = piece;
            }
        }

        if (whitePiecesSize == 1 && blackPiecesSize == 1){
            //king vs king
            return true;
        }

        if (whitePiecesSize == 2 && blackPiecesSize == 2) {
            if (whiteBishop != null && blackBishop != null) {
                boolean whiteBishopTileColor = (whiteBishop.getRank() + whiteBishop.getFile()) % 2 == 0;
                boolean blackBishopTileColor = (blackBishop.getRank() + blackBishop.getFile()) % 2 == 0;

                if (whiteBishopTileColor == blackBishopTileColor) {
                    //king and bishop vs king and bishop with same tile color bishops
                    return true;
                }
            }
        }

        if (blackPiecesSize == 1 && whitePiecesSize == 2) {
            if (whiteBishop != null || whiteKnight != null) {
                //king vs king and bishop
                //king vs king and knight
                return true;
            }
        }
        if (blackPiecesSize == 2 && whitePiecesSize == 1){
            if (blackKnight != null || blackBishop != null){
                //king vs king and bishop
                //king vs king and knight
                return true;
            }
        }

        return false;
    }
}