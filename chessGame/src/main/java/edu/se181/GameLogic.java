package edu.se181;

import java.util.ArrayList;
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
    protected int checkState;
    protected List<Square> checkSquares;

    public GameLogic(Board b) {
        board = b;
        finished = false;
        checkState = 0;
        checkSquares = new ArrayList<>();
    }

    protected Board getBoard() {
        return board;
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
                        distance++;
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
                        distance++;
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
                        distance++;
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
                        distance++;
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
                        distance++;
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
                        distance++;
                    } while (targetSquare != null);
                }
            }
        }
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
                if (targetSquare.getRank() == 3.5 + 3.5 * sign)
                    moves.add(new PromoteMove((Pawn) piece, targetSquare.getRank(), targetSquare.getFile(), false, null));
                else
                    moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                // inside this block because we only check to move two if the square in between is empty
                if (!piece.hasMoved()) {
                    targetSquare = board.getSquareByPieceOffset(piece, 2 * sign, 0);
                    if (targetSquare.getOccupant() == null)
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
                        moves.add(new EnPassantMove((Pawn) piece, targetSquare.getRank() + (piece.isWhite() ? 1 : -1), targetSquare.getFile()));
                }
            }
        } else if (piece instanceof Knight) {
            // -2, -1, 1, 2
            for (int i = -2; i <= 2; i++) {
                if (i == 0)
                    continue;
                // -1, 1 if i is -2, 2; -2, 2 if i is -1, 1
                for (int j = -3 + Math.abs(i); j <= 3 - Math.abs(i); j += 3 - Math.abs(i)) {
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
            for (int i = -1; i <= 1; i += 2) {
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
            for (int i = -1; i <= 1; i += 2) {
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
                        if (!(rook instanceof Rook) || rook.hasMoved() || rook.getRank() != 0 || (rook.getFile() != 0 && rook.getFile() != 7))
                            continue;
                        if (rook.getFile() == 0)
                            moves.add(new CastleMove((King) piece, false));
                        else
                            moves.add(new CastleMove((King) piece, true));
                    }
                else
                    for (Piece rook : board.getBlackPieces()) {
                        if (!(rook instanceof Rook) || rook.hasMoved() || rook.getRank() != 7 || (rook.getFile() != 0 && rook.getFile() != 7))
                            continue;
                        if (rook.getFile() == 0)
                            moves.add(new CastleMove((King) piece, false));
                        else
                            moves.add(new CastleMove((King) piece, true));
                    }
            }
        }
        return moves;
    }

    public List<Move> getLegalMoves(Piece piece) {
        List<Move> moves = getBaseMoves(piece, false);
        // Single check, must capture or block. King handled separately below
        if (checkState == 1 && !(piece instanceof King)) {
            moves = moves.stream().filter((Move move) -> {
                // Capture checking pawn by en passant
                if (move instanceof EnPassantMove && // We are capturing en passant
                        checkSquares.get(0).getOccupant() instanceof Pawn && // The check is delivered by a pawn
                        checkSquares.get(0).getFile() == move.getFileDest() // Our en passant move captures the right pawn
                )
                    return true;
                return checkSquares.contains(board.getSquareByRankFile(move.getRankDest(), move.getFileDest()));
            }).collect(Collectors.toList());
        } else if (checkState == 2) {
            // Double check - Only king can move so all other pieces have no moves
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
            // Legal to capture the pinner if the piece is able
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
                distance++;
            } while (targetSquare != null && (targetSquare.getOccupant() == null || targetSquare.getOccupant() == piece));
            moves = moves.stream().filter((Move move) -> pinSquares.contains(board.getSquareByRankFile(move.getRankDest(), move.getFileDest()))).collect(Collectors.toList());
        }
        if (piece instanceof King) {
            // Filter king's legal squares to ones that opponent pieces can't move to
            if (piece.isWhite())
                for (Piece other : board.getBlackPieces()) {
                    moves = moves.stream().filter((Move m) -> getBaseMoves(other, true).stream().noneMatch((Move m2) -> m.getRankDest() == m2.getRankDest() && m.getFileDest() == m2.getFileDest())).collect(Collectors.toList());
                }
            else
                for (Piece other : board.getWhitePieces()) {
                    moves = moves.stream().filter((Move m) -> getBaseMoves(other, true).stream().noneMatch((Move m2) -> m.getRankDest() == m2.getRankDest() && m.getFileDest() == m2.getFileDest())).collect(Collectors.toList());
                }
            // Check in between square for check if castling
            List<Move> kingMoves = moves;
            moves = moves.stream().filter((Move move) -> {
                if (move instanceof CastleMove) {
                    if (checkState > 0)
                        return false;
                    if (((CastleMove) move).isKingSide())
                        return kingMoves.stream().anyMatch((Move m) -> m.getFileDest() == 5 && m.getRankDest() == 3.5 + 3.5 * (piece.isWhite() ? -1 : 1));
                    else
                        return kingMoves.stream().anyMatch((Move m) -> m.getFileDest() == 3 && m.getRankDest() == 3.5 + 3.5 * (piece.isWhite() ? -1 : 1));
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
                    Square targetSquare;
                    Piece targetPiece;
                    for (int direction = -1; direction <= 1; direction += 2) {
                        int distance = 1;
                        do {
                            targetSquare =  board.getSquareByPieceOffset(piece, 0, direction * distance);
                            if (targetSquare == null)
                                return true;
                            targetPiece = targetSquare.getOccupant();
                            if (targetPiece != null && targetPiece.getFile() == move.getFileDest()) {
                                distance++;
                                targetPiece = null;
                                continue;
                            }
                            if ((targetPiece instanceof Rook || targetPiece instanceof Queen) && targetPiece.isWhite() != piece.isWhite())
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
