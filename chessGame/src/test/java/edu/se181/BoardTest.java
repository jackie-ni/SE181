package edu.se181;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BoardTest {
    private Board board;

    @Before
    public void beforeEach() {
        board = new Board();
    }

    @Test
    public void resetTest() {
        boolean properSquares = true;
        Square[][] boardSquares = board.getSquares();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                properSquares = properSquares && boardSquares[i][j].getRank() == (7 - i) && boardSquares[i][j].getFile() == j;
            }
        }
        Assert.assertTrue(properSquares);
        Assert.assertTrue(board.getWhitePieces() instanceof ArrayList && board.getWhitePieces().size() == 0);
        Assert.assertTrue(board.getBlackPieces() instanceof ArrayList && board.getBlackPieces().size() == 0);
    }

    @Test
    public void initializeTest() {
        Square testSquare;
        Square testSquare2;
        Class target;
            board = new Board();
            board.initialize();
            for (int j = 0; j < 8; j++) {
                testSquare = board.getSquareByNotation((char) (97 + j) + "1");
                testSquare2 = board.getSquareByNotation((char) (97 + j) + "8");
                if (j == 0 || j == 7) {
                    target = Rook.class;
                } else if (j == 1 || j == 6) {
                    target = Knight.class;
                } else if (j == 2 || j == 5) {
                    target = Bishop.class;
                } else if (j == 3) {
                    target = Queen.class;
                } else {
                    target = King.class;
                }
                Assert.assertTrue(target.isInstance(testSquare.getOccupant()) && testSquare.getOccupant().isWhite());
                Assert.assertTrue(target.isInstance(testSquare2.getOccupant()) && !testSquare2.getOccupant().isWhite());
            }
            for (int j = 0; j < 8; j++) {
                testSquare = board.getSquareByNotation((char) (97 + j) + "2");
                testSquare2 = board.getSquareByNotation((char) (97 + j) + "7");
                target = Pawn.class;
                Assert.assertTrue(target.isInstance(testSquare.getOccupant()) && testSquare.getOccupant().isWhite());
                Assert.assertTrue(target.isInstance(testSquare2.getOccupant()) && !testSquare2.getOccupant().isWhite());
            }
        Assert.assertEquals(board.getWhitePieces().size(), 16);
        Assert.assertEquals(board.getBlackPieces().size(), 16);
    }

    @Test
    public void makeMove_GivenRegularMoveAndNotCapture_UpdatesBoardState(){
        Square[][] boardSquares = board.getSquares();
        Piece rook = new Rook(0, 0, true);

        boardSquares[7][0].setOccupant(rook);

        Move move = new RegularMove(rook, 1, 0, false);

        board.makeMove(move);

        Assert.assertEquals(boardSquares[6][0].getOccupant(), rook);
        Assert.assertNull(boardSquares[7][0].getOccupant());
    }

    @Test
    public void makeMove_GivenRegularMoveAndCaptureAndWhite_UpdatesBoardState(){
        Square[][] boardSquares = board.getSquares();

        Piece rook = new Rook(0, 0, true);
        Piece enemyPiece = new Rook(1, 0, false);

        boardSquares[7][0].setOccupant(rook);
        boardSquares[6][0].setOccupant(enemyPiece);

        board.getWhitePieces().add(rook);
        board.getBlackPieces().add(enemyPiece);

        Move move = new RegularMove(rook, 1, 0, true);

        board.makeMove(move);

        Assert.assertEquals(boardSquares[6][0].getOccupant(), rook);
        Assert.assertNull(boardSquares[7][0].getOccupant());

        Assert.assertFalse(board.getBlackPieces().contains(enemyPiece));
    }

    @Test
    public void makeMove_GivenRegularMoveAndCaptureAndBlack_UpdatesBoardState(){
        Square[][] boardSquares = board.getSquares();

        Piece rook = new Rook(0, 0, false);
        Piece enemyPiece = new Rook(1, 0, true);

        boardSquares[7][0].setOccupant(rook);
        boardSquares[6][0].setOccupant(enemyPiece);

        board.getWhitePieces().add(rook);
        board.getBlackPieces().add(enemyPiece);

        Move move = new RegularMove(rook, 1, 0, true);

        board.makeMove(move);

        Assert.assertEquals(boardSquares[6][0].getOccupant(), rook);
        Assert.assertNull(boardSquares[7][0].getOccupant());

        Assert.assertFalse(board.getWhitePieces().contains(enemyPiece));
    }

    @Test
    public void makeMove_GivenCastleMoveAndKingSide_UpdatesBoardState(){
        Square[][] boardSquares = board.getSquares();
        Piece king = new King(0, 4, true);
        Piece rook = new Rook(0, 7, true);

        boardSquares[7][4].setOccupant(king);
        boardSquares[7][7].setOccupant(rook);

        Move move = new CastleMove(((King)king), true);

        board.makeMove(move);

        Assert.assertEquals(boardSquares[7][6].getOccupant(), king);
        Assert.assertNull(boardSquares[7][4].getOccupant());

        Assert.assertEquals(boardSquares[7][5].getOccupant(), rook);
        Assert.assertNull(boardSquares[7][7].getOccupant());
    }

    @Test
    public void makeMove_GivenCastleMoveAndQueenSide_UpdatesBoardState(){
        Square[][] boardSquares = board.getSquares();
        Piece king = new King(0, 4, true);
        Piece rook = new Rook(0, 0, true);

        boardSquares[7][4].setOccupant(king);
        boardSquares[7][0].setOccupant(rook);

        Move move = new CastleMove(((King)king), false);

        board.makeMove(move);

        Assert.assertEquals(boardSquares[7][2].getOccupant(), king);
        Assert.assertNull(boardSquares[7][4].getOccupant());

        Assert.assertEquals(boardSquares[7][3].getOccupant(), rook);
        Assert.assertNull(boardSquares[7][0].getOccupant());
    }

    @Test
    public void makeMove_GivenPromoteMoveAndWhite_UpdatesBoardState(){
        Square[][] boardSquares = board.getSquares();
        Piece pawn = new Pawn(6, 0, true);
        Piece promotedPiece = new Queen(7,0, true);

        boardSquares[1][0].setOccupant(pawn);

        board.getWhitePieces().add(pawn);

        Move move = new PromoteMove(((Pawn)pawn), 7, 0,  promotedPiece);

        board.makeMove(move);

        Assert.assertNull(boardSquares[1][0].getOccupant());
        Assert.assertEquals(boardSquares[0][0].getOccupant(), promotedPiece);

        Assert.assertFalse(board.getWhitePieces().contains(pawn));
        Assert.assertTrue(board.getWhitePieces().contains(promotedPiece));
    }

    @Test
    public void makeMove_GivenPromoteMoveAndBlack_UpdatesBoardState(){
        Square[][] boardSquares = board.getSquares();
        Piece pawn = new Pawn(1, 0, false);
        Piece promotedPiece = new Queen(0,0, false);

        boardSquares[6][0].setOccupant(pawn);

        board.getBlackPieces().add(pawn);

        Move move = new PromoteMove(((Pawn)pawn), 0, 0,  promotedPiece);

        board.makeMove(move);

        Assert.assertNull(boardSquares[6][0].getOccupant());
        Assert.assertEquals(boardSquares[7][0].getOccupant(), promotedPiece);

        Assert.assertFalse(board.getBlackPieces().contains(pawn));
        Assert.assertTrue(board.getBlackPieces().contains(promotedPiece));
    }

    @Test
    public void makeMove_GivenEnPassantMoveAndWhite_UpdatesBoardState(){
        Square[][] boardSquares = board.getSquares();
        Piece pawn = new Pawn(4, 0, true);
        Piece enemyPawn = new Pawn(4,1, false);

        boardSquares[3][0].setOccupant(pawn);
        boardSquares[3][1].setOccupant(enemyPawn);

        board.getWhitePieces().add(pawn);
        board.getBlackPieces().add(enemyPawn);

        Move move = new EnPassantMove(((Pawn)pawn), 5, 1);

        board.makeMove(move);

        Assert.assertNull(boardSquares[3][0].getOccupant());
        Assert.assertEquals(boardSquares[2][1].getOccupant(), pawn);

        Assert.assertNull(boardSquares[3][1].getOccupant());

        Assert.assertFalse(board.getBlackPieces().contains(enemyPawn));
    }

    @Test
    public void makeMove_GivenEnPassantMoveAndBlack_UpdatesBoardState(){
        Square[][] boardSquares = board.getSquares();
        Piece pawn = new Pawn(3, 0, false);
        Piece enemyPawn = new Pawn(3,1, true);

        boardSquares[4][0].setOccupant(pawn);
        boardSquares[4][1].setOccupant(enemyPawn);

        board.getWhitePieces().add(pawn);
        board.getBlackPieces().add(enemyPawn);

        Move move = new EnPassantMove(((Pawn)pawn), 2, 1);

        board.makeMove(move);

        Assert.assertNull(boardSquares[4][0].getOccupant());
        Assert.assertEquals(boardSquares[5][1].getOccupant(), pawn);

        Assert.assertNull(boardSquares[4][1].getOccupant());

        Assert.assertFalse(board.getWhitePieces().contains(enemyPawn));
    }

    @Test
    public void getSquareByPieceOffsetTest() {
        board.initialize();
        // white king
        Piece piece = board.getSquareByNotation("e1").getOccupant();
        Assert.assertTrue(board.getSquareByPieceOffset(piece, 0, -2).getOccupant() instanceof Bishop);
        Assert.assertTrue(board.getSquareByPieceOffset(piece, 0, 2).getOccupant() instanceof Knight);
        Assert.assertNull(board.getSquareByPieceOffset(piece, -2, 0));
        Assert.assertNull(board.getSquareByPieceOffset(piece, 2, 0).getOccupant());
    }

    @Test
    public void getSquareByNotationTest() {
        board.initialize();
        Square target;
        target = board.getSquareByNotation("e1");
        Assert.assertTrue(target.getOccupant() instanceof King && target.getOccupant().isWhite());
        target = board.getSquareByNotation("d8");
        Assert.assertTrue(target.getOccupant() instanceof Queen && !target.getOccupant().isWhite());
    }

    @Test
    public void loadBoardTest() {
        String testStr = "001111rnbqkbnrpppppppp00000000000000000000000000000000PPPPPPPPRNBQKBNR";
        board.loadBoard(testStr);
        Square target;
        target = board.getSquares()[0][3];
        Assert.assertTrue(target.getOccupant() instanceof Queen && !target.getOccupant().isWhite());
        target = board.getSquareByNotation("e1");
        Assert.assertTrue(target.getOccupant() instanceof King && target.getOccupant().isWhite());
        Assert.assertEquals(board.getWhitePieces().size(), 16);
        Assert.assertEquals(board.getBlackPieces().size(), 16);
        testStr = "e41001rnbqkbnrpppppppp00000000000000000000P00000000000PPPP0PPPRNBQKBNR";
        board.loadBoard(testStr);
        target = board.getSquares()[7][4];
        Assert.assertTrue(target.getOccupant() instanceof King && target.getOccupant().isWhite());
        target = board.getSquareByNotation("e4");
        Assert.assertTrue(target.getOccupant() instanceof Pawn && target.getOccupant().isWhite());
        Assert.assertTrue(target.getOccupant() instanceof Pawn && ((Pawn) target.getOccupant()).isEnPassantable());
        target = board.getSquareByNotation("a1");
        Assert.assertTrue(target.getOccupant() instanceof Rook && target.getOccupant().hasMoved());
        target = board.getSquareByNotation("h8");
        Assert.assertTrue(target.getOccupant() instanceof Rook && target.getOccupant().hasMoved());
        Assert.assertEquals(board.getWhitePieces().size(), 16);
        Assert.assertEquals(board.getBlackPieces().size(), 16);
    }

    @Test
    public void toBoardStateTest() {
        board.initialize();
        String testStr = board.toBoardState();
        Assert.assertEquals(testStr, "001111rnbqkbnrpppppppp00000000000000000000000000000000PPPPPPPPRNBQKBNR");
        board.getSquareByNotation("a1").getOccupant().firstMovePerformed();
        board.getSquareByNotation("e8").getOccupant().firstMovePerformed();
        ((Pawn) (board.getSquareByNotation("c7").getOccupant())).setEnPassantable(true);
        testStr = board.toBoardState();
        Assert.assertEquals(testStr, "c71000rnbqkbnrpppppppp00000000000000000000000000000000PPPPPPPPRNBQKBNR");
    }
}
