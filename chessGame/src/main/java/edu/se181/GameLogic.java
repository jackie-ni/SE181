package edu.se181;

import java.util.List;

public class GameLogic {
    private Board board;
    private boolean finished;
    private int finishState;
    private int drawCondition;
    private int checkState;
    private List<Square> checkSquares;

    public GameLogic(Board b) {
        board = b;
    }
}
