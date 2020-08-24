package edu.se181;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameLogic {
    private Board board;
    protected boolean finished;
    protected int finishState;
    // Draw conditions:
    // 0 - Stalemate
    // 1 - Insufficient Material
    // 2 - Repetition
    // 3 - 50 Move Rule
    // 4 - Mutual Draw Agreement
    protected int drawCondition;
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

    protected boolean isFinished(){
        return finished;
    }

    protected int getFinishState(){
        return finishState;
    }

    protected int getDrawCondition(){
        return drawCondition;
    }

    public void analyzeGameState(boolean lastMoveWhite, int halfMoveClock, int repetitionIndex, List<String> boardStates) {
        evaluateDrawConditionsExceptStalemate(halfMoveClock, repetitionIndex, boardStates);
        findChecks(lastMoveWhite);
        if (checkState > 0) {
            System.out.println(checkState + " - check"); //debug
        }
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

    protected void evaluateDrawConditionsExceptStalemate(int halfMoveClock, int repetitionIndex, List<String> boardStates) {
        if (DrawChecker.isDeadPosition(board)){
            finished = true;
            finishState = 0;
            drawCondition = 1;
        }
        else if (DrawChecker.isThreefoldRepetition(boardStates, repetitionIndex)){
            finished = true;
            finishState = 0;
            drawCondition = 2;
        }
        else if (halfMoveClock == 100){
            finished = true;
            finishState = 0;
            drawCondition = 3;
        }
    }

    protected void findChecks(boolean lastMoveWhite) {
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

    protected boolean hasLegalMove(boolean white) {
        List<Piece> pieces;
        if (white)
            pieces = board.getWhitePieces();
        else
            pieces = board.getBlackPieces();
        for (Piece piece : pieces) {
            if (getLegalMoves(piece).size() > 0)
                return true;
        }
        return false;
    }

    protected void determinePins(boolean lastMoveWhite) {
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
                        moves.add(new EnPassantMove((Pawn) piece, targetSquare.getRank() + sign, targetSquare.getFile()));
                }
            }
        } else if (piece instanceof Knight) {
            // -2, -1, 1, 2
            int[][] knightMoves = new int[8][2];
            knightMoves[0] = new int[]{-2, -1};
            knightMoves[1] = new int[]{-2, 1};
            knightMoves[2] = new int[]{-1, 2};
            knightMoves[3] = new int[]{1, 2};
            knightMoves[4] = new int[]{2, 1};
            knightMoves[5] = new int[]{2, -1};
            knightMoves[6] = new int[]{1, -2};
            knightMoves[7] = new int[]{-1, -2};
            for (int[] knightMove : knightMoves) {
                targetSquare = board.getSquareByPieceOffset(piece, knightMove[0], knightMove[1]);
                if (targetSquare != null) {
                    targetPiece = targetSquare.getOccupant();
                    if (targetPiece == null)
                        moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                    else if (targetPiece.isWhite() != piece.isWhite())
                        moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), true));
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
                                if (!ignoreEnemyKing || !(targetPiece instanceof King) || targetPiece.isWhite() == piece.isWhite())
                                    break;
                            } else {
                                moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                                break;
                            }
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
                            if (!ignoreEnemyKing || !(targetPiece instanceof King) || targetPiece.isWhite() == piece.isWhite())
                                break;
                        } else {
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                            break;
                        }
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
                            if (!ignoreEnemyKing || !(targetPiece instanceof King) || targetPiece.isWhite() == piece.isWhite())
                                break;
                        } else {
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                            break;
                        }
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
                                if (!ignoreEnemyKing || !(targetPiece instanceof King) || targetPiece.isWhite() == piece.isWhite())
                                    break;
                            } else {
                                moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                                break;
                            }
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
                            if (!ignoreEnemyKing || !(targetPiece instanceof King) || targetPiece.isWhite() == piece.isWhite())
                                break;
                        } else {
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                            break;
                        }
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
                            if (!ignoreEnemyKing || !(targetPiece instanceof King) || targetPiece.isWhite() == piece.isWhite())
                                break;
                        } else {
                            moves.add(new RegularMove(piece, targetSquare.getRank(), targetSquare.getFile(), false));
                            break;
                        }
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
        moves = moves.stream().filter((Move m) -> {
            Piece p = board.getSquareByRankFile(m.getRankDest(), m.getFileDest()).getOccupant();
            return p == null || p.isWhite() != piece.isWhite();
        }).collect(Collectors.toList());
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
            moves = moves.stream()
                    .filter((Move move) -> pinSquares.contains(board.getSquareByRankFile(move.getRankDest(), move.getFileDest())))
                    .collect(Collectors.toList());
        }
        if (piece instanceof King) {
            // Filter king's legal squares to ones that opponent pieces can't move to
            if (piece.isWhite())
                for (Piece other : board.getBlackPieces()) {
                    moves = moves.stream()
                            .filter((Move m) -> getBaseMoves(other, true)
                                    .stream().noneMatch((Move m2) -> m.getRankDest() == m2.getRankDest() && m.getFileDest() == m2.getFileDest()))
                            .collect(Collectors.toList());
                }
            else
                for (Piece other : board.getWhitePieces()) {
                    moves = moves.stream()
                            .filter((Move m) -> getBaseMoves(other, true)
                                    .stream().noneMatch((Move m2) -> m.getRankDest() == m2.getRankDest() && m.getFileDest() == m2.getFileDest()))
                            .collect(Collectors.toList());
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
                            targetSquare = board.getSquareByPieceOffset(piece, 0, direction * distance);
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
                    return !rankSliderFound || !kingFound;
                }
                return true;
            }).collect(Collectors.toList());
        }
        return moves;
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
            return new PromoteMove((Pawn)piece, rank, file, isCapture, promotedPiece);
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
