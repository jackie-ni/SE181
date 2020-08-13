package edu.se181;

import edu.se181.King;
import edu.se181.Piece;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PieceTest {

    private Piece piece;

    @Before
    public void beforeEach() {
        piece = new King(0, 0, true);
    }

    @Test
    public void firstMovePerformed() {
        Assert.assertFalse(piece.hasMoved());
        piece.firstMovePerformed();
        Assert.assertTrue(piece.hasMoved());
    }
}