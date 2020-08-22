package edu.se181;

public abstract class Move {
	private Piece piece;
	private int rankDest;
	private int fileDest;

	public Move(Piece piece, int rankDest, int fileDest, boolean isCapture){
		this.piece = piece;
		this.rankDest = rankDest;
		this.fileDest = fileDest;
	}

	public Piece getPiece() {
		return piece;
	}

	public int getRankDest() {
		return rankDest;
	}

	public int getFileDest() {
		return fileDest;
	}
}
