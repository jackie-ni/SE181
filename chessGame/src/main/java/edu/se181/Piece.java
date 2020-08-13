package edu.se181;

public abstract class Piece {
    private boolean white;
    private boolean moved;
    private Piece pinnedBy;
    private int rank;
    private int file;

    public Piece(int r, int f, boolean w) {
        rank = r;
        file = f;
        white = w;
        moved = false;
    }

    public boolean isWhite() {
        return white;
    }

    public boolean hasMoved () {
        return moved;
    }

    public void firstMovePerformed () {
        moved = true;
    }

    public Piece getPinnedBy () {
        return pinnedBy;
    }

    public void setPinnedBy (Piece piece) {
        pinnedBy = piece;
    }

    public int getRank () {
        return rank;
    }

    public void setRank (int r) {
        rank = r;
    }

    public int getFile () {
        return file;
    }

    public void setFile (int f) {
        file = f;
    }
}
