package edu.se181;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
    public void invertBoardTest() {
        board.invertBoard();
        boolean properSquares = true;
        Square[][] boardSquares = board.getSquares();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                properSquares = properSquares && boardSquares[i][j].getRank() == i && boardSquares[i][j].getFile() == 7 - j;
            }
        }
    }

    @Test
    public void initializeTest() {
        Square testSquare;
        Square testSquare2;
        Class target;
        for (int i = 0; i < 2; i++) {
            board = new Board();
            board.initialize(i == 0);
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
        }
        Assert.assertEquals(board.getWhitePieces().size(), 16);
        Assert.assertEquals(board.getBlackPieces().size(), 16);
    }

    @Test
    public void getSquareByPieceOffsetTest() {
        board.initialize(true);
        // white king
        Piece piece = board.getSquareByNotation("e1").getOccupant();
        Assert.assertTrue(board.getSquareByPieceOffset(piece, 0, -2).getOccupant() instanceof Bishop);
        Assert.assertTrue(board.getSquareByPieceOffset(piece, 0, 2).getOccupant() instanceof Knight);
        Assert.assertNull(board.getSquareByPieceOffset(piece, -2, 0));
        Assert.assertNull(board.getSquareByPieceOffset(piece, 2, 0).getOccupant());
    }

    @Test
    public void getSquareByNotationTest() {
        board.initialize(true);
        Square target;
        target = board.getSquareByNotation("e1");
        Assert.assertTrue(target.getOccupant() instanceof King && target.getOccupant().isWhite());
        target = board.getSquareByNotation("d8");
        Assert.assertTrue(target.getOccupant() instanceof Queen && !target.getOccupant().isWhite());
        board = new Board();
        board.initialize(false);
        target = board.getSquareByNotation("e1");
        Assert.assertTrue(target.getOccupant() instanceof King && target.getOccupant().isWhite());
        target = board.getSquareByNotation("d8");
        Assert.assertTrue(target.getOccupant() instanceof Queen && !target.getOccupant().isWhite());
    }

    @Test
    public void loadBoardTest() {
        String testStr = "1001111rnbqkbnrpppppppp00000000000000000000000000000000PPPPPPPPRNBQKBNR";
        board.loadBoard(testStr);
        Square target;
        target = board.getSquares()[0][3];
        Assert.assertTrue(target.getOccupant() instanceof Queen && !target.getOccupant().isWhite());
        target = board.getSquareByNotation("e1");
        Assert.assertTrue(target.getOccupant() instanceof King && target.getOccupant().isWhite());
        Assert.assertEquals(board.getWhitePieces().size(), 16);
        Assert.assertEquals(board.getBlackPieces().size(), 16);
        testStr = "0e41001rnbqkbnrpppppppp00000000000000000000P00000000000PPPP0PPPRNBQKBNR";
        board.loadBoard(testStr);
        target = board.getSquares()[0][3];
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
        board.initialize(true);
        String testStr = board.toBoardState();
        Assert.assertEquals(testStr, "1001111rnbqkbnrpppppppp00000000000000000000000000000000PPPPPPPPRNBQKBNR");
        board = new Board();
        board.initialize(false);
        testStr = board.toBoardState();
        Assert.assertEquals(testStr, "0001111rnbqkbnrpppppppp00000000000000000000000000000000PPPPPPPPRNBQKBNR");
        board.getSquareByNotation("a1").getOccupant().firstMovePerformed();
        board.getSquareByNotation("e8").getOccupant().firstMovePerformed();
        ((Pawn) (board.getSquareByNotation("c7").getOccupant())).setEnPassantable(true);
        testStr = board.toBoardState();
        Assert.assertEquals(testStr, "0c71000rnbqkbnrpppppppp00000000000000000000000000000000PPPPPPPPRNBQKBNR");
    }
}
