package edu.se181;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameLogicTest {
    private static Comparator<Move> moveComparator = (Move self, Move other) -> {
        if (self.getRankDest() > other.getRankDest())
            return 1;
        else if (self.getRankDest() < other.getRankDest())
            return -1;
        else
        if (self.getFileDest() > other.getFileDest())
            return 1;
        else if (self.getFileDest() < other.getFileDest())
            return -1;
        else
            return 0;
    };
    private GameLogic gameLogic;

    @Before
    public void beforeEach() {
        Board board = new Board();
        board.initialize();
        gameLogic = new GameLogic(board);
    }

    @Test
    public void getBaseMovesTest() {
        gameLogic.getBoard().loadBoard("c50000rnb0k00rppqpn0p00000000000pP00N00b00000000000R0pPPPBQPP0RN00KB00");
        // https://lichess.org/editor/rnb1k2r/ppqpn1p1/8/2pP2N1/1b6/5R1p/PPPBQPP1/RN2KB2_w_Qkq_-_0_1
        Pawn enPassantingPawn = (Pawn) gameLogic.getBoard().getSquareByNotation("d5").getOccupant();
        Pawn initialPawn = (Pawn) gameLogic.getBoard().getSquareByNotation("g2").getOccupant();
        Bishop pinnedBishop = (Bishop) gameLogic.getBoard().getSquareByNotation("d2").getOccupant();
        Bishop bishop = (Bishop) gameLogic.getBoard().getSquareByNotation("b4").getOccupant();
        pinnedBishop.setPinnedBy(bishop);
        Knight pinnedKnight = (Knight) gameLogic.getBoard().getSquareByNotation("e7").getOccupant();
        Queen queen = (Queen) gameLogic.getBoard().getSquareByNotation("e2").getOccupant();
        pinnedKnight.setPinnedBy(queen);
        Knight knight = (Knight) gameLogic.getBoard().getSquareByNotation("g5").getOccupant();
        Rook rook = (Rook) gameLogic.getBoard().getSquareByNotation("f3").getOccupant();
        King king = (King) gameLogic.getBoard().getSquareByNotation("e8").getOccupant();

        /*List<Square> enPassantingPawnMoves = gameLogic.getBaseMoves(enPassantingPawn, false);
        List<Square> enPassantingPawnExpected = new ArrayList<>();
        enPassantingPawnExpected.add(gameLogic.getBoard().getSquareByNotation("c6"));
        enPassantingPawnExpected.add(gameLogic.getBoard().getSquareByNotation("d6"));
        enPassantingPawnMoves.sort(squareComparator);
        enPassantingPawnExpected.sort(squareComparator);
        Assert.assertEquals(enPassantingPawnExpected, enPassantingPawnMoves);

        List<Square> initialPawnMoves = gameLogic.getBaseMoves(initialPawn, false);
        List<Square> initialPawnExpected = new ArrayList<>();
        initialPawnExpected.add(gameLogic.getBoard().getSquareByNotation("g3"));
        initialPawnExpected.add(gameLogic.getBoard().getSquareByNotation("g4"));
        initialPawnExpected.add(gameLogic.getBoard().getSquareByNotation("h3"));
        initialPawnMoves.sort(squareComparator);
        initialPawnExpected.sort(squareComparator);
        Assert.assertEquals(initialPawnExpected, initialPawnMoves);

        List<Square> bishopMoves = gameLogic.getBaseMoves(bishop, false);


        List<Square> knightMoves = gameLogic.getBaseMoves(knight, false);


        List<Square> rookMoves = gameLogic.getBaseMoves(rook, false);


        List<Square> queenMoves = gameLogic.getBaseMoves(queen, false);


        List<Square> kingMoves = gameLogic.getBaseMoves(king, false);
         */
    }

    @Test
    public void getLegalMovesTest() {

    }
}