package edu.se181;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameLogicTest {
    private static Comparator<Square> squareComparator = (Square self, Square other) -> {
        if (self.getRank() > other.getRank())
            return 1;
        else if (self.getRank() < other.getRank())
            return -1;
        else
            return Integer.compare(self.getFile(), other.getFile());
    };
    private Square mapMoveToSquare(Move move) {
        return gameLogic.getBoard().getSquareByRankFile(move.getRankDest(), move.getFileDest());
    }
    private GameLogic gameLogic;

    @Before
    public void beforeEach() {
        Board board = new Board();
        board.initialize();
        gameLogic = new GameLogic(board);
    }

    // getBaseMoves Tests

    @Test
    public void getBaseMoves_PawnWithEnPassant_GetsAllBaseMoves() {
        gameLogic.getBoard().loadBoard("c50010rnb0k00rppqpn0p000000000P0pP00N00b00000000000R0p0PPBQPP0RN00KB00");
        // https://lichess.org/editor/rnb1k2r/ppqpn1p1/8/P1pP2N1/1b6/5R1p/1PPBQPP1/RN2KB2_w_k_c6_0_1
        Pawn enPassantingPawn = (Pawn) gameLogic.getBoard().getSquareByNotation("d5").getOccupant();

        List<Move> enPassantingPawnMoves = gameLogic.getBaseMoves(enPassantingPawn, false);
        List<Square> enPassantingPawnSquares = enPassantingPawnMoves.stream().map(this::mapMoveToSquare).collect(Collectors.toList());
        List<Square> enPassantingPawnExpected = new ArrayList<>();
        enPassantingPawnExpected.add(gameLogic.getBoard().getSquareByNotation("c6"));
        enPassantingPawnExpected.add(gameLogic.getBoard().getSquareByNotation("d6"));
        enPassantingPawnSquares.sort(squareComparator);
        enPassantingPawnExpected.sort(squareComparator);
        Assert.assertEquals(enPassantingPawnExpected, enPassantingPawnSquares);

        for (Move move : enPassantingPawnMoves) {
            if (move.getFileDest() == 2)
                Assert.assertTrue(move instanceof EnPassantMove);
            else
                Assert.assertFalse(move.isCapture());
        }
    }

    @Test
    public void getBaseMoves_PawnWithInitialMoveAndRegularCapture_GetsAllBaseMoves() {
        gameLogic.getBoard().loadBoard("c50010rnb0k00rppqpn0p000000000P0pP00N00b00000000000R0p0PPBQPP0RN00KB00");
        // https://lichess.org/editor/rnb1k2r/ppqpn1p1/8/P1pP2N1/1b6/5R1p/1PPBQPP1/RN2KB2_w_k_c6_0_1
        Pawn initialPawn = (Pawn) gameLogic.getBoard().getSquareByNotation("g2").getOccupant();

        List<Move> initialPawnMoves = gameLogic.getBaseMoves(initialPawn, false);
        List<Square> initialPawnSquares = initialPawnMoves.stream().map(this::mapMoveToSquare).collect(Collectors.toList());
        List<Square> initialPawnExpected = new ArrayList<>();
        initialPawnExpected.add(gameLogic.getBoard().getSquareByNotation("g3"));
        initialPawnExpected.add(gameLogic.getBoard().getSquareByNotation("g4"));
        initialPawnExpected.add(gameLogic.getBoard().getSquareByNotation("h3"));
        initialPawnSquares.sort(squareComparator);
        initialPawnExpected.sort(squareComparator);
        Assert.assertEquals(initialPawnExpected, initialPawnSquares);

        for (Move move : initialPawnMoves) {
            if (move.getFileDest() == 7)
                Assert.assertTrue(move.isCapture());
            else
                Assert.assertFalse(move.isCapture());
        }
    }

    @Test
    public void getBaseMoves_PawnThatPromotesWithCapture_GetsAllBaseMoves() {
        gameLogic.getBoard().loadBoard("000000000b000k0000P00000000000000000000000000000000000000000000000K000");
        // https://lichess.org/editor/3b3k/4P3/8/8/8/8/8/4K3_w_-_-_0_1
        Pawn promotePawn = (Pawn) gameLogic.getBoard().getSquareByNotation("e7").getOccupant();
        
        List<Move> promotePawnMoves = gameLogic.getBaseMoves(promotePawn, false);
        List<Square> promotePawnSquares = promotePawnMoves.stream().map(this::mapMoveToSquare).collect(Collectors.toList());
        List<Square> promotePawnExpected = new ArrayList<>();
        promotePawnExpected.add(gameLogic.getBoard().getSquareByNotation("d8"));
        promotePawnExpected.add(gameLogic.getBoard().getSquareByNotation("e8"));
        promotePawnSquares.sort(squareComparator);
        promotePawnExpected.sort(squareComparator);
        Assert.assertEquals(promotePawnExpected, promotePawnSquares);

        for (Move move : promotePawnMoves) {
            Assert.assertTrue(move instanceof PromoteMove);
            if (move.getFileDest() == 3)
                Assert.assertTrue(move.isCapture());
            else
                Assert.assertFalse(move.isCapture());
        }
    }

    @Test
    public void getBaseMoves_Bishop_GetsAllBaseMoves() {
        gameLogic.getBoard().loadBoard("c50010rnb0k00rppqpn0p000000000P0pP00N00b00000000000R0p0PPBQPP0RN00KB00");
        // https://lichess.org/editor/rnb1k2r/ppqpn1p1/8/P1pP2N1/1b6/5R1p/1PPBQPP1/RN2KB2_w_k_c6_0_1
        Bishop bishop = (Bishop) gameLogic.getBoard().getSquareByNotation("b4").getOccupant();

        List<Move> bishopMoves = gameLogic.getBaseMoves(bishop, false);
        List<Square> bishopSquares = bishopMoves.stream().map(this::mapMoveToSquare).collect(Collectors.toList());
        List<Square> bishopExpected = new ArrayList<>();
        bishopExpected.add(gameLogic.getBoard().getSquareByNotation("a5"));
        bishopExpected.add(gameLogic.getBoard().getSquareByNotation("a3"));
        bishopExpected.add(gameLogic.getBoard().getSquareByNotation("c3"));
        bishopExpected.add(gameLogic.getBoard().getSquareByNotation("d2"));
        bishopSquares.sort(squareComparator);
        bishopExpected.sort(squareComparator);
        Assert.assertEquals(bishopExpected, bishopSquares);

        for (Move move : bishopMoves) {
            if (move.getFileDest() == 3 || (move.getFileDest() == 0 && move.getRankDest() == 4))
                Assert.assertTrue(move.isCapture());
            else
                Assert.assertFalse(move.isCapture());
        }
    }

    @Test
    public void getBaseMoves_Knight_GetsAllBaseMoves() {
        gameLogic.getBoard().loadBoard("c50010rnb0k00rppqpn0p000000000P0pP00N00b00000000000R0p0PPBQPP0RN00KB00");
        // https://lichess.org/editor/rnb1k2r/ppqpn1p1/8/P1pP2N1/1b6/5R1p/1PPBQPP1/RN2KB2_w_k_c6_0_1
        Knight knight = (Knight) gameLogic.getBoard().getSquareByNotation("g5").getOccupant();

        List<Move> knightMoves = gameLogic.getBaseMoves(knight, false);
        List<Square> knightSquares = knightMoves.stream().map(this::mapMoveToSquare).collect(Collectors.toList());
        List<Square> knightExpected = new ArrayList<>();
        knightExpected.add(gameLogic.getBoard().getSquareByNotation("h7"));
        knightExpected.add(gameLogic.getBoard().getSquareByNotation("f7"));
        knightExpected.add(gameLogic.getBoard().getSquareByNotation("e6"));
        knightExpected.add(gameLogic.getBoard().getSquareByNotation("e4"));
        knightExpected.add(gameLogic.getBoard().getSquareByNotation("h3"));
        knightSquares.sort(squareComparator);
        knightExpected.sort(squareComparator);
        Assert.assertEquals(knightExpected, knightSquares);

        for (Move move : knightMoves) {
            if (move.getFileDest() == 7 && move.getRankDest() == 2)
                Assert.assertTrue(move.isCapture());
            else
                Assert.assertFalse(move.isCapture());
        }
    }

    @Test
    public void getBaseMoves_Rook_GetsAllBaseMoves() {
        gameLogic.getBoard().loadBoard("c50010rnb0k00rppqpn0p000000000P0pP00N00b00000000000R0p0PPBQPP0RN00KB00");
        // https://lichess.org/editor/rnb1k2r/ppqpn1p1/8/P1pP2N1/1b6/5R1p/1PPBQPP1/RN2KB2_w_k_c6_0_1
        Rook rook = (Rook) gameLogic.getBoard().getSquareByNotation("f3").getOccupant();

        List<Move> rookMoves = gameLogic.getBaseMoves(rook, false);
        List<Square> rookSquares = rookMoves.stream().map(this::mapMoveToSquare).collect(Collectors.toList());
        List<Square> rookExpected = new ArrayList<>();
        rookExpected.add(gameLogic.getBoard().getSquareByNotation("g3"));
        rookExpected.add(gameLogic.getBoard().getSquareByNotation("h3"));
        rookExpected.add(gameLogic.getBoard().getSquareByNotation("e3"));
        rookExpected.add(gameLogic.getBoard().getSquareByNotation("d3"));
        rookExpected.add(gameLogic.getBoard().getSquareByNotation("c3"));
        rookExpected.add(gameLogic.getBoard().getSquareByNotation("b3"));
        rookExpected.add(gameLogic.getBoard().getSquareByNotation("a3"));
        rookExpected.add(gameLogic.getBoard().getSquareByNotation("f4"));
        rookExpected.add(gameLogic.getBoard().getSquareByNotation("f5"));
        rookExpected.add(gameLogic.getBoard().getSquareByNotation("f6"));
        rookExpected.add(gameLogic.getBoard().getSquareByNotation("f7"));
        rookExpected.add(gameLogic.getBoard().getSquareByNotation("f8"));
        rookSquares.sort(squareComparator);
        rookExpected.sort(squareComparator);
        Assert.assertEquals(rookExpected, rookSquares);

        for (Move move : rookMoves) {
            if (move.getFileDest() == 7 && move.getRankDest() == 2)
                Assert.assertTrue(move.isCapture());
            else
                Assert.assertFalse(move.isCapture());
        }
    }

    @Test
    public void getBaseMoves_Queen_GetsAllBaseMoves() {
        gameLogic.getBoard().loadBoard("c50010rnb0k00rppqpn0p000000000P0pP00N00b00000000000R0p0PPBQPP0RN00KB00");
        // https://lichess.org/editor/rnb1k2r/ppqpn1p1/8/P1pP2N1/1b6/5R1p/1PPBQPP1/RN2KB2_w_k_c6_0_1
        Queen queen = (Queen) gameLogic.getBoard().getSquareByNotation("c7").getOccupant();

        List<Move> queenMoves = gameLogic.getBaseMoves(queen, false);
        List<Square> queenSquares = queenMoves.stream().map(this::mapMoveToSquare).collect(Collectors.toList());
        List<Square> queenExpected = new ArrayList<>();
        queenExpected.add(gameLogic.getBoard().getSquareByNotation("b6"));
        queenExpected.add(gameLogic.getBoard().getSquareByNotation("a5"));
        queenExpected.add(gameLogic.getBoard().getSquareByNotation("d8"));
        queenExpected.add(gameLogic.getBoard().getSquareByNotation("c6"));
        queenExpected.add(gameLogic.getBoard().getSquareByNotation("d6"));
        queenExpected.add(gameLogic.getBoard().getSquareByNotation("e5"));
        queenExpected.add(gameLogic.getBoard().getSquareByNotation("f4"));
        queenExpected.add(gameLogic.getBoard().getSquareByNotation("g3"));
        queenExpected.add(gameLogic.getBoard().getSquareByNotation("h2"));
        queenSquares.sort(squareComparator);
        queenExpected.sort(squareComparator);
        Assert.assertEquals(queenExpected, queenSquares);

        for (Move move : queenMoves) {
            if (move.getFileDest() == 0)
                Assert.assertTrue(move.isCapture());
            else
                Assert.assertFalse(move.isCapture());
        }
    }

    @Test
    public void getBaseMoves_King_GetsAllBaseMoves() {
        gameLogic.getBoard().loadBoard("c50010rnb0k00rppqpn0p000000000P0pP00N00b00000000000R0p0PPBQPP0RN00KB00");
        // https://lichess.org/editor/rnb1k2r/ppqpn1p1/8/P1pP2N1/1b6/5R1p/1PPBQPP1/RN2KB2_w_k_c6_0_1
        King king = (King) gameLogic.getBoard().getSquareByNotation("e8").getOccupant();

        List<Move> kingMoves = gameLogic.getBaseMoves(king, false);
        List<Square> kingSquares = kingMoves.stream().map(this::mapMoveToSquare).collect(Collectors.toList());
        List<Square> kingExpected = new ArrayList<>();
        kingExpected.add(gameLogic.getBoard().getSquareByNotation("d8"));
        kingExpected.add(gameLogic.getBoard().getSquareByNotation("f8"));
        kingExpected.add(gameLogic.getBoard().getSquareByNotation("f7"));
        kingExpected.add(gameLogic.getBoard().getSquareByNotation("g8"));
        kingSquares.sort(squareComparator);
        kingExpected.sort(squareComparator);
        Assert.assertEquals(kingExpected, kingSquares);

        for (Move move : kingMoves) {
            if (move.getFileDest() == 6)
                Assert.assertTrue(move instanceof CastleMove);
            else
                Assert.assertTrue(move instanceof RegularMove);
        }
    }

    @Test
    public void getBaseMoves_IgnoreEnemyKingTrue_GetsAllBaseMovesIncludingPastEnemyKing() {
        gameLogic.getBoard().loadBoard("001100r000000000q0k0000000000000Q000rb000000000000000000000000R000K00R");
        // https://lichess.org/editor/r7/2q1k3/8/2Q3rb/8/8/8/R3K2R_b_KQ_-_0_1
        Queen queen = (Queen) gameLogic.getBoard().getSquareByNotation("c5").getOccupant();

        List<Move> noIgnore = gameLogic.getBaseMoves(queen, false);
        List<Move> ignore = gameLogic.getBaseMoves(queen, true);

        Assert.assertEquals(noIgnore.size() + 1, ignore.size());
        Assert.assertTrue(ignore.stream().anyMatch((Move m) -> m.getRankDest() == 6 && m.getFileDest() == 4));
        Assert.assertTrue(ignore.stream().anyMatch((Move m) -> m.getRankDest() == 7 && m.getFileDest() == 5));
        for (Move move : noIgnore) {
            Assert.assertTrue(ignore.stream().anyMatch((Move m) -> m.getRankDest() == move.getRankDest() && m.getFileDest() == move.getFileDest()));
        }
    }

    /* getLegalMoves Tests

    Things to cover:
    regular moves
    cant move king into check
    cant castle king into check
    cant castle king while in check
    cant castle king through check
    cant move piece such that king is in check (pinned)
    cant en passant into check
    capture check pawn by en passant
    must block or capture when in check and not moving king (king move rules remain the same)
     */
    @Test
    public void getLegalMoves_RegularMove_AllBaseMovesIncluded() {
        gameLogic.getBoard().loadBoard("001100r000000000q0k0000000000000Q000rb000000000000000000000000R000K00R");
        // https://lichess.org/editor/r7/2q1k3/8/2Q3rb/8/8/8/R3K2R_b_KQ_-_0_1
        Rook rook = (Rook) gameLogic.getBoard().getSquareByNotation("h1").getOccupant();

        List<Move> baseMoves = gameLogic.getBaseMoves(rook, false);
        List<Move> legalMoves = gameLogic.getLegalMoves(rook);

        Assert.assertEquals(baseMoves.size(), legalMoves.size());
        for (Move move : baseMoves) {
            Assert.assertTrue(baseMoves.stream().anyMatch((Move m) -> m.getRankDest() == move.getRankDest() && m.getFileDest() == move.getFileDest()));
        }
    }

    @Test
    public void getLegalMoves_KingIntoCheck_NotAllowed() {
        gameLogic.getBoard().loadBoard("001100r000000000q0k0000000000000Q000rb000000000000000000000000R000K00R");
        // https://lichess.org/editor/r7/2q1k3/8/2Q3rb/8/8/8/R3K2R_b_KQ_-_0_1
        King king = (King) gameLogic.getBoard().getSquareByNotation("e1").getOccupant();

        List<Move> baseMoves = gameLogic.getBaseMoves(king, false);
        List<Move> legalMoves = gameLogic.getLegalMoves(king);

        Assert.assertTrue(baseMoves.stream().anyMatch((Move m) -> m.getRankDest() == 0 && m.getFileDest() == 3));
        Assert.assertTrue(baseMoves.stream().anyMatch((Move m) -> m.getRankDest() == 1 && m.getFileDest() == 4));
        Assert.assertTrue(legalMoves.stream().noneMatch((Move m) -> m.getRankDest() == 0 && m.getFileDest() == 3));
        Assert.assertTrue(legalMoves.stream().noneMatch((Move m) -> m.getRankDest() == 1 && m.getFileDest() == 4));
    }

    @Test
    public void getLegalMoves_InCheckMoveKing_OnlyEscapeMoves() {
        gameLogic.getBoard().loadBoard("001100r000000000q0k0000000000000Q000rb000000000000000000000000R000K00R");
        // https://lichess.org/editor/r7/2q1k3/8/2Q3rb/8/8/8/R3K2R_b_KQ_-_0_1
        King king = (King) gameLogic.getBoard().getSquareByNotation("e7").getOccupant();

        gameLogic.checkState = 1;
        gameLogic.checkSquares.add(gameLogic.getBoard().getSquareByNotation("c5"));
        gameLogic.checkSquares.add(gameLogic.getBoard().getSquareByNotation("d6"));

        List<Move> baseMoves = gameLogic.getBaseMoves(king, false);
        List<Move> legalMoves = gameLogic.getLegalMoves(king);

        Assert.assertEquals(8, baseMoves.size());
        Assert.assertEquals(6, legalMoves.size());

        Assert.assertTrue(legalMoves.stream().allMatch((Move m) -> m.getRankDest() - m.getFileDest() != 2));
        for (Move move : legalMoves) {
            Assert.assertTrue(baseMoves.stream().anyMatch((Move m) -> m.getRankDest() == move.getRankDest() && m.getFileDest() == move.getFileDest()));
        }
    }

    @Test
    public void getLegalMoves_CastleKingIntoCheck_NotAllowed() {
        gameLogic.getBoard().loadBoard("001100r000000000q0k0000000000000Q000rb000000000000000000000000R000K00R");
        // https://lichess.org/editor/r7/2q1k3/8/2Q3rb/8/8/8/R3K2R_b_KQ_-_0_1
        King king = (King) gameLogic.getBoard().getSquareByNotation("e1").getOccupant();

        List<Move> baseMoves = gameLogic.getBaseMoves(king, false);
        List<Move> legalMoves = gameLogic.getLegalMoves(king);

        Assert.assertTrue(baseMoves.stream().anyMatch((Move m) -> m.getRankDest() == 0 && m.getFileDest() == 6));
        Assert.assertTrue(legalMoves.stream().noneMatch((Move m) -> m.getRankDest() == 0 && m.getFileDest() == 6));
    }

    @Test
    public void getLegalMoves_CastleKingThroughCheck_NotAllowed() {
        gameLogic.getBoard().loadBoard("001100r000000000q0k0000000000000Q000rb000000000000000000000000R000K00R");
        // https://lichess.org/editor/r7/2q1k3/8/2Q3rb/8/8/8/R3K2R_b_KQ_-_0_1
        King king = (King) gameLogic.getBoard().getSquareByNotation("e1").getOccupant();

        List<Move> baseMoves = gameLogic.getBaseMoves(king, false);
        List<Move> legalMoves = gameLogic.getLegalMoves(king);

        Assert.assertTrue(baseMoves.stream().anyMatch((Move m) -> m.getRankDest() == 0 && m.getFileDest() == 2));
        Assert.assertTrue(legalMoves.stream().noneMatch((Move m) -> m.getRankDest() == 0 && m.getFileDest() == 2));
    }

    @Test
    public void getLegalMoves_CastleKingWhileInCheck_NotAllowed() {
        gameLogic.getBoard().loadBoard("001100r000000000q0k00000000000000000r000Q0000b0000000000000000R000K00R");
        // https://lichess.org/editor/r7/2q1k3/8/6r1/2Q4b/8/8/R3K2R_w_KQ_-_0_1
        King king = (King) gameLogic.getBoard().getSquareByNotation("e1").getOccupant();

        gameLogic.checkState = 1;
        gameLogic.checkSquares.add(gameLogic.getBoard().getSquareByNotation("h4"));
        gameLogic.checkSquares.add(gameLogic.getBoard().getSquareByNotation("g3"));
        gameLogic.checkSquares.add(gameLogic.getBoard().getSquareByNotation("f2"));

        List<Move> baseMoves = gameLogic.getBaseMoves(king, false);
        List<Move> legalMoves = gameLogic.getLegalMoves(king);

        Assert.assertTrue(baseMoves.stream().anyMatch((Move m) -> m.getRankDest() == 0 && m.getFileDest() == 2));
        Assert.assertTrue(baseMoves.stream().anyMatch((Move m) -> m.getRankDest() == 0 && m.getFileDest() == 6));
        Assert.assertTrue(legalMoves.stream().noneMatch((Move m) -> m.getRankDest() == 0 && m.getFileDest() == 2));
        Assert.assertTrue(legalMoves.stream().noneMatch((Move m) -> m.getRankDest() == 0 && m.getFileDest() == 6));
    }

    @Test
    public void getLegalMoves_MovePiecePinned_OnlyAllowOnPinAxis() {
        // d40000 00000000 00000000 00000000 00000000 k0pP0Q0q 00000000 00000B00 0000K000
        gameLogic.getBoard().loadBoard("d4000000000000000000000000000000000000k0pP0Q0q0000000000000B000000K000");
        // https://lichess.org/editor/8/8/8/8/k1pP1Q1q/8/5B2/4K3_b_-_d3_0_1
        Bishop bishop = (Bishop) gameLogic.getBoard().getSquareByNotation("f2").getOccupant();
        Queen queen = (Queen) gameLogic.getBoard().getSquareByNotation("h4").getOccupant();

        bishop.setPinnedBy(queen);

        List<Move> legalMoves = gameLogic.getLegalMoves(bishop);

        Assert.assertEquals(2, legalMoves.size());
        Assert.assertTrue(legalMoves.stream().anyMatch((Move m) -> m.getRankDest() == 2 && m.getFileDest() == 6));
        Assert.assertTrue(legalMoves.stream().anyMatch((Move m) -> m.getRankDest() == 3 && m.getFileDest() == 7));
    }

    @Test
    public void getLegalMoves_EnPassantSelfCheck_NotALlowed() {
        gameLogic.getBoard().loadBoard("d4000000000000000000000000000000000000k0pP0Q0q0000000000000B000000K000");
        // https://lichess.org/editor/8/8/8/8/k1pP1Q1q/8/5B2/4K3_b_-_d3_0_1
        Pawn pawn = (Pawn) gameLogic.getBoard().getSquareByNotation("c4").getOccupant();

        List<Move> baseMoves = gameLogic.getBaseMoves(pawn, false);
        List<Move> legalMoves = gameLogic.getLegalMoves(pawn);

        Assert.assertTrue(baseMoves.stream().anyMatch((Move m) -> m.getRankDest() == 2 && m.getFileDest() == 3));
        Assert.assertTrue(legalMoves.stream().noneMatch((Move m) -> m.getRankDest() == 2 && m.getFileDest() == 3));
    }

    @Test
    public void getLegalMoves_CaptureCheckingPawnByEnPassant_Allowed() {
        gameLogic.getBoard().loadBoard("d4000000000000000000000000000000k0000000pP0Q0B00000000000000000000K000");
        // https://lichess.org/editor/8/8/8/2k5/2pP1Q1B/8/8/4K3_b_-_d3_0_1
        Pawn pawn = (Pawn) gameLogic.getBoard().getSquareByNotation("c4").getOccupant();

        gameLogic.checkState = 1;
        gameLogic.checkSquares.add(gameLogic.getBoard().getSquareByNotation("d4"));

        List<Move> legalMoves = gameLogic.getLegalMoves(pawn);

        Assert.assertEquals(1, legalMoves.size());
        Assert.assertTrue(legalMoves.stream().anyMatch((Move m) -> m.getRankDest() == 2 && m.getFileDest() == 3));
    }

    @Test
    public void getLegalMoves_InCheckMoveNonKing_OnlyBlockCapture() {
        gameLogic.getBoard().loadBoard("001100r000000000q0k0000000000000Q000rb000000000000000000000000R000K00R");
        // https://lichess.org/editor/r7/2q1k3/8/2Q3rb/8/8/8/R3K2R_b_KQ_-_0_1

        gameLogic.checkState = 1;
        gameLogic.checkSquares.add(gameLogic.getBoard().getSquareByNotation("c5"));
        gameLogic.checkSquares.add(gameLogic.getBoard().getSquareByNotation("d6"));

        Rook rook = (Rook) gameLogic.getBoard().getSquareByNotation("g5").getOccupant();
        Queen queen = (Queen) gameLogic.getBoard().getSquareByNotation("c7").getOccupant();

        List<Move> rookLegalMoves = gameLogic.getLegalMoves(rook);
        List<Move> queenLegalMoves = gameLogic.getLegalMoves(queen);

        Assert.assertEquals(1, rookLegalMoves.size());
        Assert.assertEquals(2, queenLegalMoves.size());

        Assert.assertTrue(rookLegalMoves.stream().anyMatch((Move m) -> m.getRankDest() == 4 && m.getFileDest() == 2));
        Assert.assertTrue(queenLegalMoves.stream().anyMatch((Move m) -> m.getRankDest() == 4 && m.getFileDest() == 2));
        Assert.assertTrue(queenLegalMoves.stream().anyMatch((Move m) -> m.getRankDest() == 5 && m.getFileDest() == 3));
    }
}