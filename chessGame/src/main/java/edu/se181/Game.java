package edu.se181;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board board;
    private boolean whiteTurn;
    private boolean playerIsWhite;
    private Socket socket;
    private GameLogic logicUnit;
    private List<Move> moveList;
    private GameStage gameStage;

    public Game() {
        board = new Board();
        board.initialize();
        logicUnit = new GameLogic(board);
        moveList = new ArrayList<>();
    }

    public Board getBoard() {
        return board;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public void changeTurn() {
        whiteTurn = !whiteTurn;
    }

    public boolean isPlayerWhite() {
        return playerIsWhite;
    }

    public void makeMove(Move move) {
        moveList.add(move);
        board.move(move);
        /* UNCOMMENT WHEN GAMELOGIC MERGED
        logicUnit.analyzeGameState();
        if (logicUnit.isGameFinished()) {
            // TODO: implement game teardown code
        }
         */
    }

    public List<Move> getLegalMoves(int rank, int file) {
        Piece mover = board.getSquareByRankFile(rank, file).getOccupant();
        //return logicUnit.getLegalMoves(mover);
        return null; // switch out when we merge GameLogic legal moves
    }
}
