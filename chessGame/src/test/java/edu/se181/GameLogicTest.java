package edu.se181;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameLogicTest {
    private GameLogic gameLogic;

    @Before
    public void beforeEach() {
        Board board = new Board();
        board.initialize();
        gameLogic = new GameLogic(board);
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
    }

    @Test
    public void convertToMove_GivenPromoteNotation_ReturnsCorrectPromoteMove(){
        String notation = "a7a8=Q";

        Piece piece = gameLogic.getBoard().getSquareByNotation("a7").getOccupant();
        Piece promotedPiece = new Queen(7,0,piece.isWhite());

        Move expectedResult = new PromoteMove((Pawn)piece, 7, 0, promotedPiece);
        Move result = gameLogic.convertToMove(notation);

        Assert.assertEquals(result.getPiece(), expectedResult.getPiece());
        Assert.assertEquals(result.getFileDest(), expectedResult.getFileDest());
        Assert.assertEquals(result.getRankDest(), expectedResult.getRankDest());
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
        Piece piece = new King(0,4, true);

        Move move = new CastleMove((King) piece,true);

        String result = gameLogic.convertToNotation(move);

        Assert.assertEquals(result, "Ke1O-O");
    }

    @Test
    public void convertToNotation_GivenCastleMoveQueenSide_ReturnsCorrectNotation(){
        Piece piece = new King(0,4, true);

        Move move = new CastleMove((King) piece,false);

        String result = gameLogic.convertToNotation(move);

        Assert.assertEquals(result, "Ke1O-O-O");
    }

    @Test
    public void convertToNotation_GivenPromoteMove_ReturnsCorrectNotation(){
        Piece piece = new Pawn(6,0, true);
        Piece promotedPiece = new Queen(7,0,true);

        Move move = new PromoteMove((Pawn) piece,7, 0, promotedPiece);

        String result = gameLogic.convertToNotation(move);

        Assert.assertEquals(result, "a7a8=Q");
    }

    @Test
    public void convertToNotation_GivenEnPassantMove_ReturnsCorrectNotation(){
        Piece piece = new Pawn(4,0, true);

        Move move = new EnPassantMove((Pawn) piece,5, 1);

        String result = gameLogic.convertToNotation(move);

        Assert.assertEquals(result, "a5xb6e.p.");
    }


}