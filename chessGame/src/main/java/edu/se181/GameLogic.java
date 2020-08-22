package edu.se181;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    protected Board getBoard() {
        return board;
    }

    protected List<Move> getBaseMoves(Piece piece, boolean ignoreEnemyKing) {
        List<Move> moves = new ArrayList<>();
        Square targetSquare;
        Piece targetPiece = null;
        if (piece instanceof Pawn) {
            // we don't have to null check target square for pawns because they can't even try to move off the board
            int sign = piece.isWhite() ? 1 : -1;
            // move forward 1
            targetSquare = board.getSquareByPieceOffset(piece, sign, 0);
            if (targetSquare.getOccupant() == null) {
                moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                // inside this block because we only check to move two if the square in between is empty
                if (!piece.hasMoved()) {
                    targetSquare = board.getSquareByPieceOffset(piece, 2 * sign, 0);
                    if (targetSquare.getOccupant() == null)
                        if (targetSquare.getRank() == 3.5 + 3.5 * sign)
                            moves.add(new PromoteMove((Pawn) piece, targetSquare.getRank(), targetSquare.getFile(), false, null));
                        else
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                }
            }
            // capturing
            for (int i = -1; i <= 1; i += 2) {
                // regular
                targetSquare = board.getSquareByPieceOffset(piece, sign, i);
                if (targetSquare != null) {
                    targetPiece = targetSquare.getOccupant();
                    if (targetPiece != null && targetPiece.isWhite() != piece.isWhite())
                        if (targetSquare.getRank() == 3.5 + 3.5 * sign)
                            moves.add(new PromoteMove((Pawn) piece, targetSquare.getRank(), targetSquare.getFile(), true, null));
                        else
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), true));
                }
                // en passant
                targetSquare = board.getSquareByPieceOffset(piece, 0, i);
                if (targetSquare != null) {
                    targetPiece = targetSquare.getOccupant();
                    if (targetPiece instanceof Pawn && targetPiece.isWhite() != piece.isWhite() && ((Pawn) targetPiece).isEnPassantable())
                        moves.add(new EnPassantMove((Pawn) piece, targetSquare.getRank(), targetSquare.getFile()));
                }
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
                    if (targetSquare != null) {
                        targetPiece = targetSquare.getOccupant();
                        if (targetPiece == null)
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                        else if (targetPiece.isWhite() != piece.isWhite())
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), true));
                    }
                }
            }
        } else if (piece instanceof Bishop) {
            for (int i = -1; i <= 1; i += 2) {
                for (int j = -1; j <= 1; j += 2) {
                    int distance = 1;
                    do {
                        targetSquare = board.getSquareByPieceOffset(piece, i * distance, j * distance);
                        if (targetSquare != null) {
                            targetPiece = targetSquare.getOccupant();
                            if (targetPiece == null)
                                moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                            else if (targetPiece.isWhite() != piece.isWhite()) {
                                moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), true));
                                if (!(ignoreEnemyKing && targetPiece instanceof King && targetPiece.isWhite() != piece.isWhite()))
                                    break;
                            } else
                                break;
                            distance++;
                        }
                    } while (targetSquare != null);
                }
            }
        } else if (piece instanceof Rook) {
            // we don't combine the rank and file checking into the same while loop because each needs its own break condition
            for (int i = -1; i < 1; i += 2) {
                int distance = 1;
                do {
                    targetSquare = board.getSquareByPieceOffset(piece, i * distance, 0);
                    if (targetSquare != null) {
                        targetPiece = targetSquare.getOccupant();
                        if (targetPiece == null)
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                        else if (targetPiece.isWhite() != piece.isWhite()) {
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), true));
                            if (!(ignoreEnemyKing && targetPiece instanceof King && targetPiece.isWhite() != piece.isWhite()))
                                break;
                        } else
                            break;
                        distance++;
                    }
                } while (targetSquare != null);
                distance = 1;
                do {
                    targetSquare = board.getSquareByPieceOffset(piece, 0, i * distance);
                    if (targetSquare != null) {
                        targetPiece = targetSquare.getOccupant();
                        if (targetPiece == null)
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                        else if (targetPiece.isWhite() != piece.isWhite()) {
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), true));
                            if (!(ignoreEnemyKing && targetPiece instanceof King && targetPiece.isWhite() != piece.isWhite()))
                                break;
                        } else
                            break;
                        distance++;
                    }
                } while (targetSquare != null);
            }
        } else if (piece instanceof Queen) {
            // Bishop movmement
            for (int i = -1; i <= 1; i += 2) {
                for (int j = -1; j <= 1; j += 2) {
                    int distance = 1;
                    do {
                        targetSquare = board.getSquareByPieceOffset(piece, i * distance, j * distance);
                        if (targetSquare != null) {
                            targetPiece = targetSquare.getOccupant();
                            if (targetPiece == null)
                                moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                            else if (targetPiece.isWhite() != piece.isWhite()) {
                                moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), true));
                                if (!(ignoreEnemyKing && targetPiece instanceof King && targetPiece.isWhite() != piece.isWhite()))
                                    break;
                            } else
                                break;
                            distance++;
                        }
                    } while (targetSquare != null);
                }
            }
            // Rook movement
            for (int i = -1; i < 1; i += 2) {
                int distance = 1;
                do {
                    targetSquare = board.getSquareByPieceOffset(piece, i * distance, 0);
                    if (targetSquare != null) {
                        targetPiece = targetSquare.getOccupant();
                        if (targetPiece == null)
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                        else if (targetPiece.isWhite() != piece.isWhite()) {
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), true));
                            if (!(ignoreEnemyKing && targetPiece instanceof King && targetPiece.isWhite() != piece.isWhite()))
                                break;
                        } else
                            break;
                        distance++;
                    }
                } while (targetSquare != null);
                distance = 1;
                do {
                    targetSquare = board.getSquareByPieceOffset(piece, 0, i * distance);
                    if (targetSquare != null) {
                        targetPiece = targetSquare.getOccupant();
                        if (targetPiece == null)
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                        else if (targetPiece.isWhite() != piece.isWhite()) {
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), true));
                            if (!(ignoreEnemyKing && targetPiece instanceof King && targetPiece.isWhite() != piece.isWhite()))
                                break;
                        } else
                            break;
                        distance++;
                    }
                } while (targetSquare != null);
            }
        } else if (piece instanceof King) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0)
                        continue;
                    targetSquare = board.getSquareByPieceOffset(piece, i, j);
                    if (targetSquare != null) {
                        targetPiece = targetSquare.getOccupant();
                        if (targetPiece == null)
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                        else if (targetPiece.isWhite() != piece.isWhite())
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), true));
                    }
                }
            }
            if (!piece.hasMoved()) {
                if (piece.isWhite())
                    for (Piece rook : board.getWhitePieces()) {
                        if (!(rook instanceof Rook) || rook.hasMoved())
                            continue;
                        if (rook.getFile() == 0)
                            moves.add(new CastleMove((King) piece, 2, false));
                        else
                            moves.add(new CastleMove((King) piece, 6, true));
                    }
                else
                    for (Piece rook : board.getBlackPieces()) {
                        if (!(rook instanceof Rook) || rook.hasMoved())
                            continue;
                        if (rook.getFile() == 0)
                            moves.add(new CastleMove((King) piece, 2, false));
                        else
                            moves.add(new CastleMove((King) piece, 6, true));
                    }
            }
        }
        return moves;
    }

    public List<Move> getLegalMoves(Piece piece) {
        List<Move> moves = getBaseMoves(piece, false);
        if (checkState == 1 && !(piece instanceof King)) {
            moves = moves.stream().filter((Move move) -> {
                // Capture checking pawn by en passant
                if (piece instanceof Pawn &&
                        checkSquares.get(0).getOccupant() instanceof Pawn &&
                        ((Pawn) checkSquares.get(0).getOccupant()).isEnPassantable() &&
                        Math.abs(checkSquares.get(0).getRank() - move.getRankDest()) == 1 &&
                        Math.abs(checkSquares.get(0).getFile() - move.getFileDest()) == 1) {
                    return true;
                }
                return checkSquares.contains(board.getSquareByRankFile(move.getRankDest(), move.getFileDest()));
            }).collect(Collectors.toList());
        } else if (checkState == 2) {
            if (!(piece instanceof King)) {
                moves.clear();
                return moves;
            }
        }
        Piece pinner = piece.getPinnedBy();
        if (pinner != null) {
            List<Square> pinSquares = new ArrayList<>();
            Square targetSquare;
            int rankDir = 0;
            int fileDir = 0;
            int distance = 1;
            pinSquares.add(board.getSquareByPieceOffset(pinner, 0, 0));
            if (pinner.getRank() < piece.getRank())
                rankDir = 1;
            else if (pinner.getRank() > piece.getRank())
                rankDir = -1;
            if (pinner.getFile() < piece.getFile())
                fileDir = 1;
            else if (pinner.getFile() > piece.getFile())
                fileDir = -1;
            do {
                targetSquare = board.getSquareByPieceOffset(pinner, rankDir * distance, fileDir * distance);
                if (targetSquare != null)
                    pinSquares.add(targetSquare);
            } while (targetSquare != null);
            moves = moves.stream().filter((Move move) -> pinSquares.contains(board.getSquareByRankFile(move.getRankDest(), move.getFileDest()))).collect(Collectors.toList());
        }
        if (piece instanceof King) {
            // Filter king's legal squares to ones that opponent pieces can't move to
            if (piece.isWhite())
                for (Piece other : board.getBlackPieces()) {
                    moves = moves.stream().filter((Move m) -> !getBaseMoves(other, true).contains(m)).collect(Collectors.toList());
                }
            else
                for (Piece other : board.getWhitePieces()) {
                    moves = moves.stream().filter((Move m) -> !getBaseMoves(other, true).contains(m)).collect(Collectors.toList());
                }
            // Check in between square for check if castling
            List<Move> kingMoves = moves;
            moves = moves.stream().filter((Move move) -> {
                if (move instanceof CastleMove) {
                    if (checkState > 0)
                        return false;
                    if (move.getFileDest() == 2)
                        return kingMoves.stream().anyMatch((Move m) -> m.getFileDest() == 3 && m.getRankDest() == 3.5 + 3.5 * (piece.isWhite() ? -1 : 1));
                    else
                        return kingMoves.stream().anyMatch((Move m) -> m.getFileDest() == 5 && m.getRankDest() == 3.5 + 3.5 * (piece.isWhite() ? -1 : 1));
                }
                return true;
            }).collect(Collectors.toList());
        }
        if (piece instanceof Pawn) {
            // Prevent en passant capture from putting own king in check
            moves = moves.stream().filter((Move move) -> {
                // En passant capture
                if (move instanceof EnPassantMove) {
                    boolean rankSliderFound = false;
                    boolean kingFound = false;
                    Piece targetPiece;
                    for (int direction = -1; direction <= 1; direction += 2) {
                        int distance = 1;
                        do {
                            targetPiece = board.getSquareByPieceOffset(piece, 0, direction * distance).getOccupant();
                            if (targetPiece != null && targetPiece.getFile() == move.getFileDest()) {
                                distance++;
                                continue;
                            }
                            if (targetPiece instanceof Rook || targetPiece instanceof Queen && targetPiece.isWhite() != piece.isWhite())
                                rankSliderFound = true;
                            else if (targetPiece instanceof King && targetPiece.isWhite() == piece.isWhite())
                                kingFound = true;
                            distance++;
                        } while (targetPiece == null);
                    }
                    return !(rankSliderFound && kingFound);
                }
                return true;
            }).collect(Collectors.toList());
        }
        return moves;
    }
}
