package edu.se181;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board board;
    private boolean whiteTurn;
    private boolean playerIsWhite;
    private Socket socket;
    protected GameLogic logicUnit;
    private List<Move> moveList;
    protected List<String> boardStates;
    protected int halfMoveClock;
    protected int repetitionIndex;
    private GameStage gameStage;

    public Game() {
        board = new Board();
        board.initialize();
        logicUnit = new GameLogic(board);
        moveList = new ArrayList<>();
        boardStates = new ArrayList<>();
        boardStates.add(board.toBoardState());
        halfMoveClock = 0;
        repetitionIndex = 0;
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

    protected void handleIrreversible(Move move) {
        int irreversible = DrawChecker.isIrreversibleMove(move);
        if (irreversible > 0) {
            halfMoveClock = 0;
        }
        if (irreversible == 2) {
            repetitionIndex = boardStates.size();
        }
    }

    public void handleFinish() {
        if (logicUnit.isGameFinished()) {
            if (logicUnit.getFinishState() == -1) {
                // TODO: black victory
            } else if (logicUnit.getFinishState() == 1) {
                // TODO: white victory
            } else {
                if (logicUnit.getDrawCondition() == 0) {
                    // TODO: stalemate
                } else if (logicUnit.getDrawCondition() == 1) {
                    // TODO: insufficient material
                } else if (logicUnit.getDrawCondition() == 2) {
                    // TODO: threefold repetition
                } else if (logicUnit.getDrawCondition() == 3) {
                    // TODO: 50 move
                } else {
                    // TODO: mutual draw agreement
                }
            }
            // TODO: implement game teardown code
        }
    }

    public void makeMove(Move move) {
        handleIrreversible(move);

        board.makeMove(move);

        halfMoveClock++;
        moveList.add(move);
        boardStates.add(board.toBoardState());

        logicUnit.analyzeGameState(isWhiteTurn(), halfMoveClock, repetitionIndex, boardStates);

        handleFinish();

        if (isPlayerWhite() == isWhiteTurn()) {
            // TODO: socket code to send our move to the server
        }

        changeTurn();
    }

    public List<Move> getLegalMoves(int rank, int file) {
        Piece mover = board.getSquareByRankFile(rank, file).getOccupant();
        return logicUnit.getLegalMoves(mover);
    }
}
