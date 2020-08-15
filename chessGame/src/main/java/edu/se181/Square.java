package edu.se181;

public class Square {
    private int rank;
    private int file;
    private Piece occupant;

    public Square(int r, int f, Piece o) {
        rank = r;
        file = f;
        occupant = o;
    }

    public boolean isWhite() {
        return (rank + file % 2) == 0;
    }

    public Piece getOccupant() {
        return occupant;
    }

    public void setOccupant(Piece p) {
        occupant = p;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int r) {
        rank = r;
    }

    public int getFile() {
        return file;
    }

    public void setFile(int f) {
        file = f;
    }
}
