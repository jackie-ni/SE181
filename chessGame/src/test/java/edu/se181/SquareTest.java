package edu.se181;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SquareTest {
    private Square square;

    @Before
    public void beforeEach() {
        square = new Square(0, 0, null);
    }

    @Test
    public void isWhiteTest() {
        Square sq2 = new Square(0, 1, null);
        Square sq3 = new Square(7, 7, null);
        Square sq4 = new Square(3, 5, null);
        Assert.assertTrue(square.isWhite());
        Assert.assertFalse(sq2.isWhite());
        Assert.assertTrue(sq3.isWhite());
        Assert.assertTrue(sq4.isWhite());
    }
}
