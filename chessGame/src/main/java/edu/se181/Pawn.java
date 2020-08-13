package edu.se181;

public class Pawn extends Piece {
    private boolean enPassantable;

    public Pawn(int r, int f, boolean w) {
        super(r, f, w);
    }

    public boolean isEnPassantable () {
        return enPassantable;
    }

    public void setEnPassantable (boolean ep) {
        enPassantable = ep;
    }
}
