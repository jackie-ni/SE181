package edu.se181;

import java.util.List;
import java.util.stream.Collectors;

public class GameLogic {
    private Board board;
    private boolean finished;
    private int finishState;
    // Draw conditions:
    // 0 - Stalemate
    // 1 - Insufficient Material
    // 2 - Repetition
    // 3 - 50 Move Rule
    // 4 - Mutual Draw Agreement
    private int drawCondition;
    private int checkState;
    private List<Square> checkSquares;

    public GameLogic(Board b) {
        board = b;
    }

    public void analyzeGameState(boolean lastMoveWhite) {
        evaluateDrawConditionsExceptStalemate();
        findChecks(lastMoveWhite);
        determinePins(lastMoveWhite);
        boolean hasMove = hasLegalMove(!lastMoveWhite);
        if (!hasMove) {
            finished = true;
            if (checkState > 0)
                finishState = lastMoveWhite ? 1 : -1;
            else {
                finishState = 0;
                drawCondition = 0;
            }
        }
    }

    public void evaluateDrawConditionsExceptStalemate() {
        // TODO: Implement in issue 30
    }

    public void findChecks(boolean lastMoveWhite) {
        checkState = 0;
        checkSquares.clear();

        List<Piece> enemyPieces;
        King king;
        if (lastMoveWhite) {
            enemyPieces = board.getWhitePieces();
            king = (King) board.getBlackPieces().stream().filter((Piece p) -> p instanceof King).collect(Collectors.toList()).get(0);
        } else {
            enemyPieces = board.getBlackPieces();
            king = (King) board.getWhitePieces().stream().filter((Piece p) -> p instanceof King).collect(Collectors.toList()).get(0);
        }
        for (Piece piece : enemyPieces) {
            List<Move> enemyMoves = getBaseMoves(piece, false);
            if (enemyMoves.stream().anyMatch((Move m) -> m.getRankDest() == king.getRank() && m.getFileDest() == king.getFile())) {
                checkState++;
                if (checkState == 2) {
                    checkSquares.clear();
                    break;
                }
                checkSquares.add(board.getSquareByPieceOffset(piece, 0, 0));
                if (piece instanceof Bishop || piece instanceof Rook || piece instanceof Queen) {
                    Square targetSquare;
                    int distance = 1;
                    int rankDir = 0;
                    int fileDir = 0;
                    if (piece.getRank() < king.getRank())
                        rankDir = 1;
                    else if (piece.getRank() > king.getRank())
                        rankDir = -1;
                    if (piece.getFile() < king.getFile())
                        fileDir = 1;
                    else if (piece.getFile() > king.getFile())
                        fileDir = -1;
                    do {
                        targetSquare = board.getSquareByPieceOffset(piece, rankDir * distance, fileDir * distance);
                        if (targetSquare != null)
                            checkSquares.add(targetSquare);
                        distance++;
                    } while (targetSquare != null && (targetSquare.getOccupant() == null));
                }
            }
        }
    }

    public boolean hasLegalMove(boolean white) {
        boolean hasMove = false;
        List<Piece> pieces;
        if (white)
            pieces = board.getWhitePieces();
        else
            pieces = board.getBlackPieces();
        for (Piece piece : pieces) {
            hasMove = hasMove || getLegalMoves(piece).size() > 0;
        }
        return hasMove;
    }

    public void determinePins(boolean lastMoveWhite) {
        List<Piece> enemyPieces;
        King king;
        if (lastMoveWhite) {
            enemyPieces = board.getWhitePieces();
            king = (King) board.getBlackPieces().stream().filter((Piece p) -> p instanceof King).collect(Collectors.toList()).get(0);
        } else {
            enemyPieces = board.getBlackPieces();
            king = (King) board.getWhitePieces().stream().filter((Piece p) -> p instanceof King).collect(Collectors.toList()).get(0);
        }
        for (Piece piece : enemyPieces) {
            if (piece instanceof Bishop) {
                if (Math.abs(piece.getRank() - king.getRank()) == Math.abs(piece.getFile() - king.getFile())) {
                    Piece potentialPinned = null;
                    Piece targetPiece;
                    Square targetSquare;
                    int distance = 1;
                    int rankDir = 0;
                    int fileDir = 0;
                    if (piece.getRank() < king.getRank())
                        rankDir = 1;
                    else if (piece.getRank() > king.getRank())
                        rankDir = -1;
                    if (piece.getFile() < king.getFile())
                        fileDir = 1;
                    else if (piece.getFile() > king.getFile())
                        fileDir = -1;
                    do {
                        targetSquare = board.getSquareByPieceOffset(piece, rankDir * distance, fileDir * distance);
                        if (targetSquare != null) {
                            targetPiece = targetSquare.getOccupant();
                            if (potentialPinned == null) {
                                if (targetPiece instanceof King)
                                    break;
                                else if (targetPiece != null)
                                    potentialPinned = targetPiece;
                            } else {
                                if (targetPiece instanceof King)
                                    potentialPinned.setPinnedBy(piece);
                                else if (targetPiece != null)
                                    break;
                            }
                        }
                    } while (targetSquare != null);
                }
            } else if (piece instanceof Rook) {
                Piece potentialPinned = null;
                Piece targetPiece;
                Square targetSquare;
                int distance = 1;
                if (piece.getRank() == king.getRank()) {
                    int fileDir;
                    if (piece.getFile() < king.getFile())
                        fileDir = 1;
                    else
                        fileDir = -1;
                    do {
                        targetSquare = board.getSquareByPieceOffset(piece, 0, fileDir * distance);
                        if (targetSquare != null) {
                            targetPiece = targetSquare.getOccupant();
                            if (potentialPinned == null) {
                                if (targetPiece instanceof King)
                                    break;
                                else if (targetPiece != null)
                                    potentialPinned = targetPiece;
                            } else {
                                if (targetPiece instanceof King)
                                    potentialPinned.setPinnedBy(piece);
                                else if (targetPiece != null)
                                    break;
                            }
                        }
                    } while (targetSquare != null);
                } else if (piece.getFile() == king.getFile()) {
                    int rankDir;
                    if (piece.getRank() < king.getRank())
                        rankDir = 1;
                    else
                        rankDir = -1;
                    do {
                        targetSquare = board.getSquareByPieceOffset(piece, rankDir * distance, 0);
                        if (targetSquare != null) {
                            targetPiece = targetSquare.getOccupant();
                            if (potentialPinned == null) {
                                if (targetPiece instanceof King)
                                    break;
                                else if (targetPiece != null)
                                    potentialPinned = targetPiece;
                            } else {
                                if (targetPiece instanceof King)
                                    potentialPinned.setPinnedBy(piece);
                                else if (targetPiece != null)
                                    break;
                            }
                        }
                    } while (targetSquare != null);
                }
            } else if (piece instanceof Queen) {
                Piece potentialPinned = null;
                Piece targetPiece;
                Square targetSquare;
                int distance = 1;
                int rankDir = 0;
                int fileDir = 0;
                // Bishop check
                if (Math.abs(piece.getRank() - king.getRank()) == Math.abs(piece.getFile() - king.getFile())) {
                    if (piece.getRank() < king.getRank())
                        rankDir = 1;
                    else if (piece.getRank() > king.getRank())
                        rankDir = -1;
                    if (piece.getFile() < king.getFile())
                        fileDir = 1;
                    else if (piece.getFile() > king.getFile())
                        fileDir = -1;
                    do {
                        targetSquare = board.getSquareByPieceOffset(piece, rankDir * distance, fileDir * distance);
                        if (targetSquare != null) {
                            targetPiece = targetSquare.getOccupant();
                            if (potentialPinned == null) {
                                if (targetPiece instanceof King)
                                    break;
                                else if (targetPiece != null)
                                    potentialPinned = targetPiece;
                            } else {
                                if (targetPiece instanceof King)
                                    potentialPinned.setPinnedBy(piece);
                                else if (targetPiece != null)
                                    break;
                            }
                        }
                    } while (targetSquare != null);
                }
                // Rook check
                if (piece.getRank() == king.getRank()) {
                    if (piece.getFile() < king.getFile())
                        fileDir = 1;
                    else
                        fileDir = -1;
                    do {
                        targetSquare = board.getSquareByPieceOffset(piece, 0, fileDir * distance);
                        if (targetSquare != null) {
                            targetPiece = targetSquare.getOccupant();
                            if (potentialPinned == null) {
                                if (targetPiece instanceof King)
                                    break;
                                else if (targetPiece != null)
                                    potentialPinned = targetPiece;
                            } else {
                                if (targetPiece instanceof King)
                                    potentialPinned.setPinnedBy(piece);
                                else if (targetPiece != null)
                                    break;
                            }
                        }
                    } while (targetSquare != null);
                } else if (piece.getFile() == king.getFile()) {
                    if (piece.getRank() < king.getRank())
                        rankDir = 1;
                    else
                        rankDir = -1;
                    do {
                        targetSquare = board.getSquareByPieceOffset(piece, rankDir * distance, 0);
                        if (targetSquare != null) {
                            targetPiece = targetSquare.getOccupant();
                            if (potentialPinned == null) {
                                if (targetPiece instanceof King)
                                    break;
                                else if (targetPiece != null)
                                    potentialPinned = targetPiece;
                            } else {
                                if (targetPiece instanceof King)
                                    potentialPinned.setPinnedBy(piece);
                                else if (targetPiece != null)
                                    break;
                            }
                        }
                    } while (targetSquare != null);
                }
            }
        }
    }
}
