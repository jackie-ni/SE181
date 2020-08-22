package edu.se181;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    private Square[][] squares;
    private List<Piece> whitePieces;
    private List<Piece> blackPieces;


    public Board() {
        reset();
    }

    private void reset() {
        squares = new Square[8][8];
        for (int row = 0; row < 8; row++) {
            for (int file = 0; file < 8; file++) {
                // a8 ... h8
                // ...    ...
                // a1 ... h1
                squares[row][file] = new Square(7 - row, file, null);
            }
        }
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
    }

    public void initialize() {
        // opposing nonpawns
        squares[0][0].setOccupant(new Rook(squares[0][0].getRank(), squares[0][0].getFile(), false));
        squares[0][1].setOccupant(new Knight(squares[0][1].getRank(), squares[0][1].getFile(), false));
        squares[0][2].setOccupant(new Bishop(squares[0][2].getRank(), squares[0][2].getFile(), false));
        squares[0][3].setOccupant(new Queen(squares[0][3].getRank(), squares[0][3].getFile(), false));
        squares[0][4].setOccupant(new King(squares[0][4].getRank(), squares[0][4].getFile(), false));
        squares[0][5].setOccupant(new Bishop(squares[0][5].getRank(), squares[0][5].getFile(), false));
        squares[0][6].setOccupant(new Knight(squares[0][6].getRank(), squares[0][6].getFile(), false));
        squares[0][7].setOccupant(new Rook(squares[0][7].getRank(), squares[0][7].getFile(), false));

        // opposing pawns
        squares[1][0].setOccupant(new Pawn(squares[1][0].getRank(), squares[1][0].getFile(), false));
        squares[1][1].setOccupant(new Pawn(squares[1][1].getRank(), squares[1][1].getFile(), false));
        squares[1][2].setOccupant(new Pawn(squares[1][2].getRank(), squares[1][2].getFile(), false));
        squares[1][3].setOccupant(new Pawn(squares[1][3].getRank(), squares[1][3].getFile(), false));
        squares[1][4].setOccupant(new Pawn(squares[1][4].getRank(), squares[1][4].getFile(), false));
        squares[1][5].setOccupant(new Pawn(squares[1][5].getRank(), squares[1][5].getFile(), false));
        squares[1][6].setOccupant(new Pawn(squares[1][6].getRank(), squares[1][6].getFile(), false));
        squares[1][7].setOccupant(new Pawn(squares[1][7].getRank(), squares[1][7].getFile(), false));

        // player nonpawns
        squares[7][0].setOccupant(new Rook(squares[7][0].getRank(), squares[7][0].getFile(), true));
        squares[7][1].setOccupant(new Knight(squares[7][1].getRank(), squares[7][1].getFile(), true));
        squares[7][2].setOccupant(new Bishop(squares[7][2].getRank(), squares[7][2].getFile(), true));
        squares[7][3].setOccupant(new Queen(squares[7][3].getRank(), squares[7][3].getFile(), true));
        squares[7][4].setOccupant(new King(squares[7][4].getRank(), squares[7][4].getFile(), true));
        squares[7][5].setOccupant(new Bishop(squares[7][5].getRank(), squares[7][5].getFile(), true));
        squares[7][6].setOccupant(new Knight(squares[7][6].getRank(), squares[7][6].getFile(), true));
        squares[7][7].setOccupant(new Rook(squares[7][7].getRank(), squares[7][7].getFile(), true));

        //player pawns
        squares[6][0].setOccupant(new Pawn(squares[6][0].getRank(), squares[6][0].getFile(), true));
        squares[6][1].setOccupant(new Pawn(squares[6][1].getRank(), squares[6][1].getFile(), true));
        squares[6][2].setOccupant(new Pawn(squares[6][2].getRank(), squares[6][2].getFile(), true));
        squares[6][3].setOccupant(new Pawn(squares[6][3].getRank(), squares[6][3].getFile(), true));
        squares[6][4].setOccupant(new Pawn(squares[6][4].getRank(), squares[6][4].getFile(), true));
        squares[6][5].setOccupant(new Pawn(squares[6][5].getRank(), squares[6][5].getFile(), true));
        squares[6][6].setOccupant(new Pawn(squares[6][6].getRank(), squares[6][6].getFile(), true));
        squares[6][7].setOccupant(new Pawn(squares[6][7].getRank(), squares[6][7].getFile(), true));


        for (int row = 0; row < 2; row++) {
            for (int file = 0; file < 8; file++) {
                blackPieces.add(squares[row][file].getOccupant());
            }
        }

        for (int row = 6; row < 8; row++) {
            for (int file = 0; file < 8; file++) {
                whitePieces.add(squares[row][file].getOccupant());
            }
        }
    }

    public void makeMove(Move move) {
        Piece piece = move.getPiece();

        if (move instanceof RegularMove) {
            if (move.isCapture()){
                if (piece.isWhite()){
                    blackPieces = blackPieces.stream()
                                    .filter(p ->
                                            p.getRank() != move.getRankDest() && p.getFile() != move.getFileDest())
                                    .collect(Collectors.toList());
                }
                else {
                    whitePieces = whitePieces.stream()
                            .filter(p ->
                                    p.getRank() != move.getRankDest() && p.getFile() != move.getFileDest())
                            .collect(Collectors.toList());
                }
            }

            squares[7-piece.getRank()][piece.getFile()].setOccupant(null);
            squares[7-move.getRankDest()][piece.getFile()].setOccupant(piece);
            //feels awkward that we still need to change piece position
        }
        else if (move instanceof CastleMove){
            Piece rook;
            if (((CastleMove) move).isKingSide){
                rook = squares[7-piece.getRank()][7].getOccupant();
                squares[7-piece.getRank()][7].setOccupant(null);
                squares[7-piece.getRank()][piece.getFile()+1].setOccupant(rook);
            }else{
                rook = squares[7-piece.getRank()][0].getOccupant();
                squares[7-piece.getRank()][0].setOccupant(null);
                squares[7-piece.getRank()][piece.getFile()-1].setOccupant(rook);
            }

            squares[7-piece.getRank()][piece.getFile()].setOccupant(null);
            squares[7-move.getRankDest()][move.getFileDest()].setOccupant(piece);
        }
        else if (move instanceof PromoteMove){
            Piece promotedPiece = ((PromoteMove) move).promotePiece;
            if (piece.isWhite()){
                whitePieces = whitePieces.stream()
                        .filter(p ->
                                p.getFile() != piece.getFile() && p.getRank() != piece.getRank())
                        .collect(Collectors.toList());
                whitePieces.add(promotedPiece);
            }
            else {
                blackPieces = blackPieces.stream()
                        .filter(p ->
                                p.getFile() != piece.getFile() && p.getRank() != piece.getRank())
                        .collect(Collectors.toList());
                blackPieces.add(promotedPiece);
            }

            squares[7-piece.getRank()][piece.getFile()].setOccupant(null);
            squares[7-move.getRankDest()][move.getFileDest()].setOccupant(promotedPiece);
        }
        else if (move instanceof EnPassantMove){
            if (piece.isWhite()){
                blackPieces = blackPieces.stream()
                        .filter(p ->
                                p.getRank() != move.getRankDest()-1 && p.getFile() != move.getFileDest())
                        .collect(Collectors.toList());
                squares[7-move.getRankDest()+1][move.getFileDest()].setOccupant(null);
            }
            else {
                whitePieces = whitePieces.stream()
                        .filter(p ->
                                p.getRank() != move.getRankDest()+1 && p.getFile() != move.getFileDest())
                        .collect(Collectors.toList());
                squares[7-move.getRankDest()-1][move.getFileDest()].setOccupant(null);
            }

            squares[7-piece.getRank()][piece.getFile()].setOccupant(null);
            squares[7-move.getRankDest()][move.getFileDest()].setOccupant(piece);


        }
    }

    public Square[][] getSquares() {
        return squares;
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    public List<Piece> getBlackPieces() {
        return blackPieces;
    }

    public Square getSquareByRankFile(int rank, int file) {
        return squares[7 - rank][file];
    }

    public Square getSquareByPieceOffset(Piece piece, int rankOffset, int fileOffset) {
        int newRank = piece.getRank() + rankOffset;
        int newFile = piece.getFile() + fileOffset;
        if (newRank < 0 || newRank > 7 || newFile < 0 || newFile > 7)
            return null;

        return getSquareByRankFile(newRank, newFile);
    }

    public Square getSquareByNotation(String notation) {
        char rankChar = notation.charAt(1);
        char fileChar = notation.charAt(0);
        int rank = (int) rankChar - 49;
        int file = (int) fileChar - 97;

        return getSquareByRankFile(rank, file);
    }

    // format: bbcdefpppp...
    // bb - square of en passantable pawn or 00 if there isn't one
    // c - 1 if white can castle kingside, 0 otherwise
    // d - 1 if white can castle queenside, 0 otherwise
    // e - 1 if black can castle kingside, 0 otherwise
    // f - 1 if black can castle queenside, 0 otherwise
    // pppp... - 0 if no piece occupant, PNBRQK if white pawn, knight, etc, pnbrqk if black pawn, knight, etc
    // pppp... - indicates piece at a8, b8, ... h8, a7, b7, ..., h1 (top left to bottom right by row when white)
    public void loadBoard(String str) {
        reset();
        String meta = str.substring(0, 6);
        String pieces = str.substring(6);

        // i indicates the square on the board consistent with standard reading when playing white
        // that is a8, b8, ... h8, a7, b7, ..., h1
        for (int i = 0; i < 64; i++) {
            char pieceChar = pieces.charAt(i);
            if (pieceChar == '0')
                continue;
            int rank = 7 - (i / 8);
            int file = i % 8;
            Piece newPiece;
            if (pieceChar == 'P') {
                newPiece = new Pawn(rank, file, true);
            } else if (pieceChar == 'N') {
                newPiece = new Knight(rank, file, true);
            } else if (pieceChar == 'B') {
                newPiece = new Bishop(rank, file, true);
            } else if (pieceChar == 'R') {
                newPiece = new Rook(rank, file, true);
            } else if (pieceChar == 'Q') {
                newPiece = new Queen(rank, file, true);
            } else if (pieceChar == 'K') {
                newPiece = new King(rank, file, true);
            } else if (pieceChar == 'p') {
                newPiece = new Pawn(rank, file, false);
            } else if (pieceChar == 'n') {
                newPiece = new Knight(rank, file, false);
            } else if (pieceChar == 'b') {
                newPiece = new Bishop(rank, file, false);
            } else if (pieceChar == 'r') {
                newPiece = new Rook(rank, file, false);
            } else if (pieceChar == 'q') {
                newPiece = new Queen(rank, file, false);
            } else if (pieceChar == 'k') {
                newPiece = new King(rank, file, false);
            } else {
                System.out.println("Invalid character, " + pieceChar + ", in board string. Skipping...");
                continue;
            }
            if (pieceChar < 97)
                whitePieces.add(newPiece);
            else
                blackPieces.add(newPiece);
            getSquareByRankFile(rank, file).setOccupant(newPiece);
        }

        // en passant pawn
        if (!meta.substring(0, 2).equals("00")) {
            try {
                Pawn pawn = (Pawn) getSquareByNotation(meta.substring(0, 2)).getOccupant();
                pawn.setEnPassantable(true);
            } catch (ClassCastException e) {
                System.out.println("The piece on square " + meta.substring(0, 2) + " was not a pawn. Skipping en passantable...");
            } catch (NullPointerException e) {
                System.out.println("There is no piece on square " + meta.substring(0, 2) + " but it should be a pawn. Skipping en passantable...");
            }
        }

        // we don't have to check if the piece is a rook because a rook not on its starting square will disable castling automatically
        // white kingside castle
        if (meta.charAt(2) == '0') {
            Piece piece = getSquareByNotation("h1").getOccupant();
            if (piece != null)
                piece.firstMovePerformed();
        }

        // white queenside castle
        if (meta.charAt(3) == '0') {
            Piece piece = getSquareByNotation("a1").getOccupant();
            if (piece != null)
                piece.firstMovePerformed();
        }

        // black kingside castle
        if (meta.charAt(4) == '0') {
            Piece piece = getSquareByNotation("h8").getOccupant();
            if (piece != null)
                piece.firstMovePerformed();
        }

        // black queenside castle
        if (meta.charAt(5) == '0') {
            Piece piece = getSquareByNotation("a8").getOccupant();
            if (piece != null)
                piece.firstMovePerformed();
        }
    }

    public String toBoardState() {
        String ret = "";

        // en passant
        boolean foundEP = false;
        for (Piece piece : whitePieces) {
            if (piece instanceof Pawn && ((Pawn) piece).isEnPassantable()) {
                ret += (char) (piece.getFile() + 97);
                ret += (char) (piece.getRank() + 49);
                foundEP = true;
                break;
            }
        }
        for (Piece piece : blackPieces) {
            if (!foundEP && piece instanceof Pawn && ((Pawn) piece).isEnPassantable()) {
                ret += (char) (piece.getFile() + 97);
                ret += (char) (piece.getRank() + 49);
                foundEP = true;
                break;
            }
        }
        if (!foundEP)
            ret += "00";

        // white kingside castle
        Piece king = getSquareByNotation("e1").getOccupant();
        Piece rook = getSquareByNotation("h1").getOccupant();
        if (rook instanceof Rook && !rook.hasMoved() && king instanceof King && !king.hasMoved())
            ret += "1";
        else
            ret += "0";

        // white queenside castle
        rook = getSquareByNotation("a1").getOccupant();
        if (rook instanceof Rook && !rook.hasMoved() && king instanceof King && !king.hasMoved())
            ret += "1";
        else
            ret += "0";

        // black kingside castle
        king = getSquareByNotation("e8").getOccupant();
        rook = getSquareByNotation("h8").getOccupant();
        if (rook instanceof Rook && !rook.hasMoved() && king instanceof King && !king.hasMoved())
            ret += "1";
        else
            ret += "0";

        // black queenside castle
        rook = getSquareByNotation("a8").getOccupant();
        if (rook instanceof Rook && !rook.hasMoved() && king instanceof King && !king.hasMoved())
            ret += "1";
        else
            ret += "0";

        for (int row = 0; row < 8; row++) {
            for (int file = 0; file < 8; file++) {
                Piece piece = squares[row][file].getOccupant();
                if (piece instanceof Pawn && piece.isWhite())
                    ret += "P";
                else if (piece instanceof Knight && piece.isWhite())
                    ret += "N";
                else if (piece instanceof Bishop && piece.isWhite())
                    ret += "B";
                else if (piece instanceof Rook && piece.isWhite())
                    ret += "R";
                else if (piece instanceof Queen && piece.isWhite())
                    ret += "Q";
                else if (piece instanceof King && piece.isWhite())
                    ret += "K";
                else if (piece instanceof Pawn && !piece.isWhite())
                    ret += "p";
                else if (piece instanceof Knight && !piece.isWhite())
                    ret += "n";
                else if (piece instanceof Bishop && !piece.isWhite())
                    ret += "b";
                else if (piece instanceof Rook && !piece.isWhite())
                    ret += "r";
                else if (piece instanceof Queen && !piece.isWhite())
                    ret += "q";
                else if (piece instanceof King && !piece.isWhite())
                    ret += "k";
                else
                    ret += "0";
            }
        }

        return ret;
    }
}
