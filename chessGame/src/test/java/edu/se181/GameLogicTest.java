package edu.se181;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameLogicTest {
    private GameLogic gameLogic;

    @Before
    public void beforeEach() {
        Board board = new Board();
        board.initialize(true);
        gameLogic = new GameLogic(board);
    }
}
