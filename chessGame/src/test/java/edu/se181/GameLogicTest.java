package edu.se181;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameLogicTest {
    private static final Comparator<Square> squareComparator = (Square self, Square other) -> {
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

    @Test
    public void evaluateDrawConditionsExceptStalemate_GivenDeadBoardState_SetsDrawAndFinishStates(){
        gameLogic.getBoard().loadBoard("00000000000k0000000000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/8/8/8/8/3K4/8/8_b_-_-_0_1

        gameLogic.evaluateDrawConditionsExceptStalemate(0, 0, new ArrayList<String>());

        Assert.assertTrue(gameLogic.getFinished());
        Assert.assertEquals(0, gameLogic.getFinishState());
        Assert.assertEquals(1, gameLogic.getDrawCondition());
    }

    @Test
    public void evaluateDrawConditionsExceptStalemate_GivenThreeFoldRepetition_SetsDrawAndFinishStates(){
        List<String> boardStates = new ArrayList<>();
        boardStates.add("string1");
        boardStates.add("string1");
        boardStates.add("string1");

        gameLogic.evaluateDrawConditionsExceptStalemate(0, 0, boardStates);

        Assert.assertTrue(gameLogic.getFinished());
        Assert.assertEquals(0, gameLogic.getFinishState());
        Assert.assertEquals(2, gameLogic.getDrawCondition());
    }

    @Test
    public void evaluateDrawConditionsExceptStalemate_Given100HalfMoves_SetsDrawAndFinishStates(){

        gameLogic.evaluateDrawConditionsExceptStalemate(100, 0, new ArrayList<String>());

        Assert.assertTrue(gameLogic.getFinished());
        Assert.assertEquals(0, gameLogic.getFinishState());
        Assert.assertEquals(3, gameLogic.getDrawCondition());
    }

    // findChecks Tests

    @Test
    public void findChecks_NoChecks() {
        gameLogic.getBoard().loadBoard("00000000000k000r000000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/1r6/8/8/8/3K4/8/8_b_-_-_0_1
        gameLogic.findChecks(true);
        Assert.assertEquals(0, gameLogic.checkState);
        Assert.assertEquals(0, gameLogic.checkSquares.size());
    }

    @Test
    public void findChecks_SliderPiece_OneCheck() {
        gameLogic.getBoard().loadBoard("00000000000k000r000000000000000000000000000000000K000000000Q0000000000");
        // https://lichess.org/editor/5k2/1r6/8/8/8/3K4/5Q2/8_b_-_-_0_1
        gameLogic.findChecks(true);
        Assert.assertEquals(1, gameLogic.checkState);
        Assert.assertEquals(7, gameLogic.checkSquares.size());
        for (int i = 1; i <=7 ; i++) {
            int rank = i;
            Assert.assertTrue(gameLogic.checkSquares.stream().anyMatch((Square s) -> s.getRank() == rank && s.getFile() == 5));
        }
    }

    @Test
    public void findChecks_NonSliderPiece_OneCheck() {
        gameLogic.getBoard().loadBoard("00000000000k000r0000000000N0000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/1r6/4N3/8/8/3K4/8/8_b_-_-_0_1
        gameLogic.findChecks(true);
        Assert.assertEquals(1, gameLogic.checkState);
        Assert.assertEquals(1, gameLogic.checkSquares.size());
        Assert.assertTrue(gameLogic.checkSquares.stream().anyMatch((Square s) -> s.getRank() == 5 && s.getFile() == 4));
    }

    @Test
    public void findChecks_TwoChecks() {
        gameLogic.getBoard().loadBoard("00000000000k000r0000000000N0000000000000000000000K000000000Q0000000000");
        // https://lichess.org/editor/5k2/1r6/4N3/8/8/3K4/5Q2/8_b_-_-_0_1
        gameLogic.findChecks(true);
        Assert.assertEquals(2, gameLogic.checkState);
        Assert.assertEquals(0, gameLogic.checkSquares.size());
    }

    // hasLegalMove Tests

    @Test
    public void hasLegalMove_NoMoveInCheck_ReturnsFalse() {
        gameLogic.getBoard().loadBoard("000000000000rk00000Npp00000000000000000000000000000000000000000000K000");
        // https://lichess.org/editor/6rk/5Npp/8/8/8/8/8/4K3_b_-_-_0_1

        gameLogic.checkState = 1;
        gameLogic.checkSquares.add(gameLogic.getBoard().getSquareByNotation("f7"));

        Assert.assertFalse(gameLogic.hasLegalMove(false));
    }

    @Test
    public void hasLegalMove_NoMoveNoCheck_ReturnsFalse() {
        gameLogic.getBoard().loadBoard("0000000000000k000000000000000000000000000000000000000000000q000000000K");
        // https://lichess.org/editor/7k/8/8/8/8/8/5q2/7K_w_-_-_0_1
        Assert.assertFalse(gameLogic.hasLegalMove(true));
    }

    @Test
    public void hasLegalMove_MoveInCheck_ReturnsTrue() {
        gameLogic.getBoard().loadBoard("000000000000rk000q0Npp00000000000000000000000000000000000000000000K000");
        // https://lichess.org/editor/6rk/3q1Npp/8/8/8/8/8/4K3_b_-_-_0_1

        gameLogic.checkState = 1;
        gameLogic.checkSquares.add(gameLogic.getBoard().getSquareByNotation("f7"));

        Assert.assertTrue(gameLogic.hasLegalMove(false));
    }

    @Test
    public void hasLegalMove_MoveNoCheck_ReturnsTrue() {
        // board.initialize() called in test setup
        Assert.assertTrue(gameLogic.hasLegalMove(true));
    }

    // determinePins Tests

    @Test
    public void determinePins_PinByBishop_PinnerSet() {
        gameLogic.getBoard().loadBoard("0000000000000000000000000q000000000000b00B00b00Q00000000R0N000000K0R0r");
        // https://lichess.org/editor/8/8/3q4/8/b2B2b1/1Q6/2R1N3/3K1R1r_w_-_-_0_1
        gameLogic.determinePins(false);

        Piece pinner = gameLogic.getBoard().getSquareByNotation("g4").getOccupant();
        Piece pinnee = gameLogic.getBoard().getSquareByNotation("e2").getOccupant();

        Assert.assertEquals(pinner, pinnee.getPinnedBy());
    }

    @Test
    public void determinePins_PinByRook_PinnerSet() {
        gameLogic.getBoard().loadBoard("0000000000000000000000000q000000000000b00B00b00Q00000000R0N000000K0R0r");
        // https://lichess.org/editor/8/8/3q4/8/b2B2b1/1Q6/2R1N3/3K1R1r_w_-_-_0_1
        gameLogic.determinePins(false);

        Piece pinner = gameLogic.getBoard().getSquareByNotation("h1").getOccupant();
        Piece pinnee = gameLogic.getBoard().getSquareByNotation("f1").getOccupant();

        Assert.assertEquals(pinner, pinnee.getPinnedBy());
    }

    @Test
    public void determinePins_PinByQueen_PinnerSet() {
        gameLogic.getBoard().loadBoard("0000000000000000000000000q000000000000b00B00b00Q00000000R0N000000K0R0r");
        // https://lichess.org/editor/8/8/3q4/8/b2B2b1/1Q6/2R1N3/3K1R1r_w_-_-_0_1
        gameLogic.determinePins(false);

        Piece pinner = gameLogic.getBoard().getSquareByNotation("d6").getOccupant();
        Piece pinnee = gameLogic.getBoard().getSquareByNotation("d4").getOccupant();

        Assert.assertEquals(pinner, pinnee.getPinnedBy());
    }

    @Test
    public void determinePins_TwoBetweenNoPins_NoPinnersSet() {
        gameLogic.getBoard().loadBoard("0000000000000000000000000q000000000000b00B00b00Q00000000R0N000000K0R0r");
        // https://lichess.org/editor/8/8/3q4/8/b2B2b1/1Q6/2R1N3/3K1R1r_w_-_-_0_1
        gameLogic.determinePins(false);

        Piece notPinnee = gameLogic.getBoard().getSquareByNotation("b3").getOccupant();
        Piece notPinnee2 = gameLogic.getBoard().getSquareByNotation("c2").getOccupant();

        Assert.assertNull(notPinnee.getPinnedBy());
        Assert.assertNull(notPinnee2.getPinnedBy());
    }

    @Test
    public void convertToMove_GivenKingSideCastleNotation_ReturnsCorrectCastleMove(){
        String notation = "Ke1O-O";
        Piece piece = gameLogic.getBoard().getSquareByNotation("e1").getOccupant();

        Move expectedResult = new CastleMove((King)piece, true);
        Move result = gameLogic.convertToMove(notation);

        Assert.assertEquals(result.getPiece(), expectedResult.getPiece());
        Assert.assertEquals(result.getFileDest(), expectedResult.getFileDest());
        Assert.assertEquals(result.getRankDest(), expectedResult.getRankDest());
        Assert.assertTrue(result instanceof CastleMove);
        Assert.assertTrue(((CastleMove) result).isKingSide);
    }

    @Test
    public void convertToMove_GivenQueenSideCastleNotation_ReturnsCorrectCastleMove(){
        String notation = "Ke1O-O-O";
        Piece piece = gameLogic.getBoard().getSquareByNotation("e1").getOccupant();

        Move expectedResult = new CastleMove((King)piece, false);
        Move result = gameLogic.convertToMove(notation);

        Assert.assertEquals(result.getPiece(), expectedResult.getPiece());
        Assert.assertEquals(result.getFileDest(), expectedResult.getFileDest());
        Assert.assertEquals(result.getRankDest(), expectedResult.getRankDest());
        Assert.assertTrue(result instanceof CastleMove);
        Assert.assertFalse(((CastleMove) result).isKingSide);
    }

    @Test
    public void convertToMove_GivenCaptureNotation_ReturnsCorrectMoveWithCapture(){
        String notation = "Ra1xa2";

        Piece piece = gameLogic.getBoard().getSquareByNotation("a1").getOccupant();

        Move expectedResult = new RegularMove(piece, 1, 0, true);
        Move result = gameLogic.convertToMove(notation);

        Assert.assertEquals(result.getPiece(), expectedResult.getPiece());
        Assert.assertEquals(result.getFileDest(), expectedResult.getFileDest());
        Assert.assertEquals(result.getRankDest(), expectedResult.getRankDest());
        Assert.assertTrue(result instanceof RegularMove);
        Assert.assertTrue(result.isCapture());
    }

    @Test
    public void convertToMove_GivenRegularNotation_ReturnsCorrectRegularMove(){
        String notation = "Ra1a2";

        Piece piece = gameLogic.getBoard().getSquareByNotation("a1").getOccupant();

        Move expectedResult = new RegularMove(piece, 1, 0, false);
        Move result = gameLogic.convertToMove(notation);

        Assert.assertEquals(result.getPiece(), expectedResult.getPiece());
        Assert.assertEquals(result.getFileDest(), expectedResult.getFileDest());
        Assert.assertEquals(result.getRankDest(), expectedResult.getRankDest());
        Assert.assertTrue(result instanceof RegularMove);
        Assert.assertFalse(result.isCapture());
    }

    @Test
    public void convertToMove_GivenPromoteNotation_ReturnsCorrectPromoteMove(){
        String notation = "a7a8=Q";

        Piece piece = gameLogic.getBoard().getSquareByNotation("a7").getOccupant();
        Piece promotedPiece = new Queen(7,0,piece.isWhite());

        Move expectedResult = new PromoteMove((Pawn)piece, 7, 0, false, promotedPiece);
        Move result = gameLogic.convertToMove(notation);

        Assert.assertEquals(result.getPiece(), expectedResult.getPiece());
        Assert.assertEquals(result.getFileDest(), expectedResult.getFileDest());
        Assert.assertEquals(result.getRankDest(), expectedResult.getRankDest());
        Assert.assertTrue(result instanceof PromoteMove);
        Assert.assertTrue(((PromoteMove) result).getPromotePiece() instanceof Queen);
        Assert.assertEquals(((PromoteMove) result).getPromotePiece().getFile(), promotedPiece.getFile());
        Assert.assertEquals(((PromoteMove) result).getPromotePiece().getRank(), promotedPiece.getRank());
    }

    @Test
    public void convertToMove_GivenEnPassantNotation_ReturnsCorrectEnPassantMove(){
        String notation = "a5xb6e.p.";

        Piece piece = gameLogic.getBoard().getSquareByNotation("a5").getOccupant();

        Move expectedResult = new EnPassantMove((Pawn)piece, 5, 1);
        Move result = gameLogic.convertToMove(notation);

        Assert.assertEquals(result.getPiece(), expectedResult.getPiece());
        Assert.assertEquals(result.getFileDest(), expectedResult.getFileDest());
        Assert.assertEquals(result.getRankDest(), expectedResult.getRankDest());
        Assert.assertTrue(result instanceof EnPassantMove);
    }

    @Test
    public void convertToNotation_GivenRegularMoveAndNoCapture_ReturnsCorrectNotation(){
        Piece piece = new Rook(0,0, true);
        Move move = new RegularMove(piece, 1, 0, false);

        String result = gameLogic.convertToNotation(move);

        Assert.assertEquals(result, "Ra1a2");
    }

    @Test
    public void convertToNotation_GivenMoveWithCapture_ReturnsCorrectNotation(){
        Piece piece = new Rook(0,0, true);

        Move move = new RegularMove(piece, 1, 0, true);

        String result = gameLogic.convertToNotation(move);

        Assert.assertEquals(result, "Ra1xa2");
    }

    @Test
    public void convertToNotation_GivenCastleMoveKingSide_ReturnsCorrectNotation(){
        King piece = new King(0,4, true);

        Move move = new CastleMove(piece,true);

        String result = gameLogic.convertToNotation(move);

        Assert.assertEquals(result, "Ke1O-O");
    }

    @Test
    public void convertToNotation_GivenCastleMoveQueenSide_ReturnsCorrectNotation(){
        King piece = new King(0,4, true);

        Move move = new CastleMove(piece,false);

        String result = gameLogic.convertToNotation(move);

        Assert.assertEquals(result, "Ke1O-O-O");
    }

    @Test
    public void convertToNotation_GivenPromoteMove_ReturnsCorrectNotation(){
        Pawn piece = new Pawn(6,0, true);
        Piece promotedPiece = new Queen(7,0,true);

        Move move = new PromoteMove(piece, 7, 0, false, promotedPiece);

        String result = gameLogic.convertToNotation(move);

        Assert.assertEquals(result, "a7a8=Q");
    }

    @Test
    public void convertToNotation_GivenEnPassantMove_ReturnsCorrectNotation(){
        Pawn piece = new Pawn(4,0, true);

        Move move = new EnPassantMove(piece,5, 1);

        String result = gameLogic.convertToNotation(move);

        Assert.assertEquals(result, "a5xb6e.p.");
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