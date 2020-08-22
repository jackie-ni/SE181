package edu.se181;

import java.util.ArrayList;
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

    private List<Square> getBaseMoves(Piece piece, boolean ignorePieces) {
        List<Square> squares = new ArrayList<>();
        Square targetSquare = null;
        Piece targetPiece = null;
        if (piece instanceof Pawn) {
            // we don't have to null check target square for pawns because they can't even try to move off the board
            int sign = piece.isWhite() ? 1 : -1;
            // move forward 1
            targetSquare = board.getSquareByPieceOffset(piece, sign, 0);
            if (targetSquare.getOccupant() == null) {
                squares.add(targetSquare);
                // inside this block because we only check to move two if the square in between is empty
                if (!piece.hasMoved()) {
                    targetSquare = board.getSquareByPieceOffset(piece, 2 * sign, 0);
                    if (targetSquare.getOccupant() == null)
                        squares.add(targetSquare);
                }
            }
            // capturing
            for (int i = -1; i <= 1; i += 2) {
                // regular
                targetSquare = board.getSquareByPieceOffset(piece, sign, i);
                if (targetSquare != null)
                    targetPiece = targetSquare.getOccupant();
                if (targetPiece != null && targetPiece.isWhite() != piece.isWhite())
                    squares.add(targetSquare);
                // en passant
                targetSquare = board.getSquareByPieceOffset(piece, 0, i);
                if (targetSquare != null)
                    targetPiece = targetSquare.getOccupant();
                if (targetPiece instanceof Pawn && targetPiece.isWhite() != piece.isWhite() && ((Pawn) targetPiece).isEnPassantable())
                    squares.add(targetSquare);
            }
        } else if (piece instanceof Knight) {
            // -2, -1, 1, 2
            for (int i = -2; i <= 2; i++) {
                if (i == 0)
                    continue;
                // -1, 1 if i is -2, 2; -2, 2 if i is -1, 1
                for (int j = -3 - Math.abs(i); j <= 3 - Math.abs(i); j += 3 - Math.abs(i)) {
                    if (j == 0)
                        continue;
                    targetSquare = board.getSquareByPieceOffset(piece, i, j);
                    if (targetSquare != null)
                        targetPiece = targetSquare.getOccupant();
                    if (targetPiece == null || targetPiece.isWhite() != piece.isWhite())
                        squares.add(targetSquare);
                }
            }
        } else if (piece instanceof Bishop) {
            for (int i = -1; i <= 1; i += 2) {
                for (int j = -1; j <= 1; j += 2) {
                    int distance = 1;
                    do {
                        targetSquare = board.getSquareByPieceOffset(piece, i * distance, j * distance);
                        if (targetSquare != null)
                            targetPiece = targetSquare.getOccupant();
                        if (targetPiece == null)
                            squares.add(targetSquare);
                        else if (targetPiece.isWhite() != piece.isWhite()) {
                            squares.add(targetSquare);
                            if (!ignorePieces)
                                break;
                        } else
                            break;
                        distance++;
                    } while (targetSquare != null);
                }
            }
        } else if (piece instanceof Rook) {
            // we don't combine the rank and file checking into the same while loop because each needs its own break condition
            for (int i = -1; i < 1; i += 2) {
                int distance = 1;
                do {
                    targetSquare = board.getSquareByPieceOffset(piece, i * distance, 0);
                    if (targetSquare != null)
                        targetPiece = targetSquare.getOccupant();
                    if (targetPiece == null)
                        squares.add(targetSquare);
                    else if (targetPiece.isWhite() != piece.isWhite()) {
                        squares.add(targetSquare);
                        if (!ignorePieces)
                            break;
                    } else
                        break;
                    distance++;
                } while (targetSquare != null);
                distance = 1;
                do {
                    targetSquare = board.getSquareByPieceOffset(piece, 0, i * distance);
                    if (targetSquare != null)
                        targetPiece = targetSquare.getOccupant();
                    if (targetPiece == null)
                        squares.add(targetSquare);
                    else if (targetPiece.isWhite() != piece.isWhite()) {
                        squares.add(targetSquare);
                        if (!ignorePieces)
                            break;
                    } else
                        break;
                    distance++;
                } while (targetSquare != null);
            }
        } else if (piece instanceof Queen) {
            // Bishop movmement
            for (int i = -1; i <= 1; i += 2) {
                for (int j = -1; j <= 1; j += 2) {
                    int distance = 1;
                    do {
                        targetSquare = board.getSquareByPieceOffset(piece, i * distance, j * distance);
                        if (targetSquare != null)
                            targetPiece = targetSquare.getOccupant();
                        if (targetPiece == null)
                            squares.add(targetSquare);
                        else if (targetPiece.isWhite() != piece.isWhite()) {
                            squares.add(targetSquare);
                            if (!ignorePieces)
                                break;
                        } else
                            break;
                        distance++;
                    } while (targetSquare != null);
                }
            }
            // Rook movement
            for (int i = -1; i < 1; i += 2) {
                int distance = 1;
                do {
                    targetSquare = board.getSquareByPieceOffset(piece, i * distance, 0);
                    if (targetSquare != null)
                        targetPiece = targetSquare.getOccupant();
                    if (targetPiece == null)
                        squares.add(targetSquare);
                    else if (targetPiece.isWhite() != piece.isWhite()) {
                        squares.add(targetSquare);
                        if (!ignorePieces)
                            break;
                    } else
                        break;
                    distance++;
                } while (targetSquare != null);
                distance = 1;
                do {
                    targetSquare = board.getSquareByPieceOffset(piece, 0, i * distance);
                    if (targetSquare != null)
                        targetPiece = targetSquare.getOccupant();
                    if (targetPiece == null)
                        squares.add(targetSquare);
                    else if (targetPiece.isWhite() != piece.isWhite()) {
                        squares.add(targetSquare);
                        if (!ignorePieces)
                            break;
                    } else
                        break;
                    distance++;
                } while (targetSquare != null);
            }
        } else if (piece instanceof King) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0)
                        continue;
                    targetSquare = board.getSquareByPieceOffset(piece, i, j);
                    if (targetSquare != null)
                        targetPiece = targetSquare.getOccupant();
                    if (targetPiece == null || targetPiece.isWhite() != piece.isWhite())
                        squares.add(targetSquare);
                }
            }
        }
        return squares;
    }

    public List<Square> getLegalMoves(Piece piece) {

    }
}
