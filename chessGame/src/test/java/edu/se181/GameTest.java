package edu.se181;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class GameTest {
    private Game game;

    @Before
    public void beforeEach() {
        game = new Game(true);
        game.boardStates.add("testBoardState1");
        game.boardStates.add("testBoardState2");
        game.halfMoveClock = 2;
        game.repetitionIndex = 0;
    }

    @Test
    public void handleIrreversible_ReversibleMove_NoCounterUpdates() {
        Knight knight = (Knight) game.getBoard().getSquareByNotation("b1").getOccupant();
        Move move = new RegularMove(knight, 2, 2, false);
        game.handleIrreversible(move);
        Assert.assertEquals(2, game.halfMoveClock);
        Assert.assertEquals(0, game.repetitionIndex);
    }

    @Test
    public void handleIrreversible_IrreversiblePawnMove_BothCounterUpdates() {
        Pawn pawn = (Pawn) game.getBoard().getSquareByNotation("b2").getOccupant();
        Move move = new RegularMove(pawn, 3, 1, false);
        game.handleIrreversible(move);
        Assert.assertEquals(0, game.halfMoveClock);
        Assert.assertEquals(3, game.repetitionIndex);
    }

    @Test
    public void handleIrreversible_IrreversibleCaptureMove_BothCounterUpdates() {
        Knight knight = (Knight) game.getBoard().getSquareByNotation("b1").getOccupant();
        Move move = new RegularMove(knight, 2, 2, true);
        game.handleIrreversible(move);
        Assert.assertEquals(0, game.halfMoveClock);
        Assert.assertEquals(3, game.repetitionIndex);
    }

    @Test
    public void handleIrreversible_IrreversibleCastleMove_RepetitionIndexUpdate() {
        King king = (King) game.getBoard().getSquareByNotation("e1").getOccupant();
        Move move = new CastleMove(king, true);
        game.handleIrreversible(move);
        Assert.assertEquals(2, game.halfMoveClock);
        Assert.assertEquals(3, game.repetitionIndex);
    }

    @Test
    public void handleIrreversible_IrreversibleRookHasNotMoved_RepetitionIndexUpdate() {
        Rook rook = (Rook) game.getBoard().getSquareByNotation("a1").getOccupant();
        Move move = new RegularMove(rook, 4, 0, false);
        game.handleIrreversible(move);
        Assert.assertEquals(2, game.halfMoveClock);
        Assert.assertEquals(3, game.repetitionIndex);
    }

    @Test
    public void handleIrreversible_IrreversibleKingHasNotMoved_RepetitionIndexUpdate() {
        King king = (King) game.getBoard().getSquareByNotation("e1").getOccupant();
        Move move = new RegularMove(king, 4, 1, false);
        game.handleIrreversible(move);
        Assert.assertEquals(2, game.halfMoveClock);
        Assert.assertEquals(3, game.repetitionIndex);
    }

    @Test
    public void handleIrreversible_IrreversibleRookHasMoved_NoCounterUpdates() {
        Rook rook = (Rook) game.getBoard().getSquareByNotation("a1").getOccupant();
        rook.firstMovePerformed();
        Move move = new RegularMove(rook, 4, 0, false);
        game.handleIrreversible(move);
        Assert.assertEquals(2, game.halfMoveClock);
        Assert.assertEquals(0, game.repetitionIndex);
    }

    @Test
    public void handleIrreversible_IrreversibleKingHasMoved_NoCounterUpdates() {
        King king = (King) game.getBoard().getSquareByNotation("e1").getOccupant();
        king.firstMovePerformed();
        Move move = new RegularMove(king, 4, 1, false);
        game.handleIrreversible(move);
        Assert.assertEquals(2, game.halfMoveClock);
        Assert.assertEquals(0, game.repetitionIndex);
    }

    @Test
    @Ignore
    public void handleFinish_WhiteWins() {
        game.logicUnit.finished = true;
        game.logicUnit.finishState = 1;
    }

    @Test
    @Ignore
    public void handleFinish_BlackWins() {
        game.logicUnit.finished = true;
        game.logicUnit.finishState = -1;
    }

    @Test
    @Ignore
    public void handleFinish_DrawStalemate() {
        game.logicUnit.finished = true;
        game.logicUnit.finishState = 0;
        game.logicUnit.drawCondition = 0;
    }

    @Test
    @Ignore
    public void handleFinish_DrawInsufficientMaterial() {
        game.logicUnit.finished = true;
        game.logicUnit.finishState = 0;
        game.logicUnit.drawCondition = 1;
    }

    @Test
    @Ignore
    public void handleFinish_DrawRepetition() {
        game.logicUnit.finished = true;
        game.logicUnit.finishState = 0;
        game.logicUnit.drawCondition = 2;
    }

    @Test
    @Ignore
    public void handleFinish_DrawFiftyMoveRule() {
        game.logicUnit.finished = true;
        game.logicUnit.finishState = 0;
        game.logicUnit.drawCondition = 3;
    }

    @Test
    @Ignore
    public void handleFinish_DrawAgreement() {
        game.logicUnit.finished = true;
        game.logicUnit.finishState = 0;
        game.logicUnit.drawCondition = 4;
    }

    @Test
    public void makeMove_ReversibleMove_UpdatesInternals() {
        boolean lastTurn = game.isWhiteTurn();
        Knight knight = (Knight) game.getBoard().getSquareByNotation("b1").getOccupant();
        Move move = new RegularMove(knight, 2, 2, false);
        game.makeMove(move);
        Assert.assertTrue(lastTurn != game.isWhiteTurn());
        Assert.assertEquals(3, game.halfMoveClock);
        Assert.assertEquals(0, game.repetitionIndex);
        Assert.assertEquals(4, game.boardStates.size());
    }

    @Test
    public void makeMove_SemiIrreversibleMove_UpdatesInternals() {
        boolean lastTurn = game.isWhiteTurn();
        King king = (King) game.getBoard().getSquareByNotation("e1").getOccupant();
        Move move = new CastleMove(king, false);
        game.makeMove(move);
        Assert.assertTrue(lastTurn != game.isWhiteTurn());
        Assert.assertEquals(3, game.halfMoveClock);
        Assert.assertEquals(3, game.repetitionIndex);
        Assert.assertEquals(4, game.boardStates.size());
    }

    @Test
    public void makeMove_IrreversibleMove_UpdatesInternals() {
        boolean lastTurn = game.isWhiteTurn();
        Knight knight = (Knight) game.getBoard().getSquareByNotation("b1").getOccupant();
        Move move = new RegularMove(knight, 2, 2, true);
        game.makeMove(move);
        Assert.assertTrue(lastTurn != game.isWhiteTurn());
        Assert.assertEquals(1, game.halfMoveClock);
        Assert.assertEquals(3, game.repetitionIndex);
        Assert.assertEquals(4, game.boardStates.size());
    }
}