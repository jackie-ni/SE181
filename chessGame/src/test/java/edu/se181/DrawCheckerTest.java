package edu.se181;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DrawCheckerTest {
    private DrawChecker drawChecker;
    private Board board;

    @Before
    public void beforeEach() {
        drawChecker = new DrawChecker();
        board = new Board();
    }

    @Test
    public void isIrreversibleMove_GivenPawnMove_Returns1() {
        Piece piece = new Pawn(1, 0, true);
        Move move = new RegularMove(piece, 2, 0, false);

        int result = DrawChecker.isIrreversibleMove(move);
        Assert.assertEquals(1, result);
    }

    @Test
    public void isIrreversibleMove_GivenMoveWithCapture_Returns1() {
        Piece piece = new Rook(1, 0, true);
        Move move = new RegularMove(piece, 2, 0, true);

        int result = DrawChecker.isIrreversibleMove(move);
        Assert.assertEquals(1, result);
    }

    @Test
    public void isIrreversibleMove_GivenFirstRookMove_Returns2() {
        Piece piece = new Rook(1, 0, true);
        Move move = new RegularMove(piece, 2, 0, false);

        int result = DrawChecker.isIrreversibleMove(move);
        Assert.assertEquals(2, result);
    }

    @Test
    public void isIrreversibleMove_GivenNotFirstRookMove_Returns0() {
        Piece piece = new Rook(1, 0, true);
        piece.firstMovePerformed();
        Move move = new RegularMove(piece, 2, 0, false);

        int result = DrawChecker.isIrreversibleMove(move);
        Assert.assertEquals(0, result);
    }

    @Test
    public void isIrreversibleMove_GivenFirstKingMove_Returns2() {
        Piece piece = new King(1, 0, true);
        Move move = new RegularMove(piece, 2, 0, false);

        int result = DrawChecker.isIrreversibleMove(move);
        Assert.assertEquals(2, result);
    }

    @Test
    public void isIrreversibleMove_GivenNotFirstKingMove_Returns0() {
        Piece piece = new King(1, 0, true);
        piece.firstMovePerformed();
        Move move = new RegularMove(piece, 2, 0, false);

        int result = DrawChecker.isIrreversibleMove(move);
        Assert.assertEquals(0, result);
    }

    @Test
    public void isIrreversibleMove_GivenCastleMove_Returns2() {
        King piece = new King(0, 4, true);
        Move move = new CastleMove(piece, true);

        int result = DrawChecker.isIrreversibleMove(move);
        Assert.assertEquals(2, result);
    }

    @Test
    public void isIrreversibleMove_GivenReversibleMove_Returns0() {
        Piece piece = new Queen(0, 3, true);
        Move move = new RegularMove(piece, 1, 3, false);

        int result = DrawChecker.isIrreversibleMove(move);
        Assert.assertEquals(0, result);
    }

    @Test
    public void isThreefoldRepetition_GivenNoRepetitionAndFullList_ReturnsFalse() {
        List<String> boardStateList = new ArrayList<String>();
        boardStateList.add("string1");
        boardStateList.add("string2");
        boardStateList.add("string3");

        int index = 0;

        boolean result = DrawChecker.isThreefoldRepetition(boardStateList, index);

        Assert.assertFalse(result);
    }

    @Test
    public void isThreefoldRepetition_GivenRepetitionAndFullList_ReturnsTrue() {
        List<String> boardStateList = new ArrayList<String>();
        boardStateList.add("string1");
        boardStateList.add("string2");
        boardStateList.add("string1");
        boardStateList.add("string3");
        boardStateList.add("string1");

        int index = 0;

        boolean result = DrawChecker.isThreefoldRepetition(boardStateList, index);

        Assert.assertTrue(result);
    }

    @Test
    public void isThreefoldRepetition_GivenRepetitionAndSubList_ReturnsTrue() {
        List<String> boardStateList = new ArrayList<String>();
        boardStateList.add("string1");
        boardStateList.add("string2");
        boardStateList.add("string3");
        boardStateList.add("string2");
        boardStateList.add("string3");
        boardStateList.add("string2");

        int index = 1;

        boolean result = DrawChecker.isThreefoldRepetition(boardStateList, index);

        Assert.assertTrue(result);
    }

    @Test
    public void isThreefoldRepetition_GivenNoRepetitionAndSubList_ReturnsTrue() {
        List<String> boardStateList = new ArrayList<String>();
        boardStateList.add("string1");
        boardStateList.add("string2");
        boardStateList.add("string3");
        boardStateList.add("string2");
        boardStateList.add("string3");
        boardStateList.add("string2");

        int index = 1;

        boolean result = DrawChecker.isThreefoldRepetition(boardStateList, index);

        Assert.assertTrue(result);
    }

    @Test
    public void isDeadPosition_GivenKingVsKing_returnsTrue() {
        board.loadBoard("00000000000k0000000000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/8/8/8/8/3K4/8/8_b_-_-_0_1

        boolean result = drawChecker.isDeadPosition(board);

        Assert.assertTrue(result);
    }

    @Test
    public void isDeadPosition_GivenKingRookVsKing_returnsFalse() {
        board.loadBoard("00000000000k000r000000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/1r6/8/8/8/3K4/8/8_b_-_-_0_1

        boolean result = drawChecker.isDeadPosition(board);

        Assert.assertFalse(result);
    }

    @Test
    public void isDeadPosition_GivenKingVsKingRook_returnsFalse() {
        board.loadBoard("00000000000k000R000000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/1R6/8/8/8/3K4/8/8_b_-_-_0_1

        boolean result = drawChecker.isDeadPosition(board);

        Assert.assertFalse(result);
    }

    @Test
    public void isDeadPosition_GivenKingKnightVsKing_returnsTrue() {
        board.loadBoard("00000000000k000n000000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/1n6/8/8/8/3K4/8/8_b_-_-_0_1

        boolean result = drawChecker.isDeadPosition(board);

        Assert.assertTrue(result);
    }

    @Test
    public void isDeadPosition_GivenKingBishopVsKing_returnsTrue() {
        board.loadBoard("00000000000k000b000000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/1b6/8/8/8/3K4/8/8_b_-_-_0_1

        boolean result = drawChecker.isDeadPosition(board);

        Assert.assertTrue(result);
    }

    @Test
    public void isDeadPosition_GivenKingVsKingKnight_returnsTrue() {
        board.loadBoard("00000000000k000N000000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/1N6/8/8/8/3K4/8/8_b_-_-_0_1

        boolean result = drawChecker.isDeadPosition(board);

        Assert.assertTrue(result);
    }

    @Test
    public void isDeadPosition_GivenKingVsKingBishop_returnsTrue() {
        board.loadBoard("00000000000k000B000000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/1B6/8/8/8/3K4/8/8_b_-_-_0_1

        boolean result = drawChecker.isDeadPosition(board);

        Assert.assertTrue(result);
    }

    @Test
    public void isDeadPosition_GivenWhiteMoreThan2Pieces_returnsFalse() {
        board.loadBoard("00000000000k000BB00000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/1BB5/8/8/8/3K4/8/8_b_-_-_0_1

        boolean result = drawChecker.isDeadPosition(board);

        Assert.assertFalse(result);
    }

    @Test
    public void isDeadPosition_GivenBlackMoreThan2Pieces_returnsFalse() {
        board.loadBoard("00000000000k000bb00000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/1bb5/8/8/8/3K4/8/8_b_-_-_0_1

        boolean result = drawChecker.isDeadPosition(board);

        Assert.assertFalse(result);
    }

    @Test
    public void isDeadPosition_GivenKingBishopVsKingBishopSameColors_returnsTrue() {
        board.loadBoard("00000000000k000b0B0000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/1b1B4/8/8/8/3K4/8/8_b_-_-_0_1

        boolean result = drawChecker.isDeadPosition(board);

        Assert.assertTrue(result);
    }

    @Test
    public void isDeadPosition_GivenKingBishopVsKingBishopDifferentColors_returnsFalse() {
        board.loadBoard("00000000000k000bB00000000000000000000000000000000K00000000000000000000");
        // https://lichess.org/editor/5k2/1bB5/8/8/8/3K4/8/8_b_-_-_0_1

        boolean result = drawChecker.isDeadPosition(board);

        Assert.assertFalse(result);
    }

}