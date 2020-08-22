package edu.se181;

public class CastleMove extends Move{
	boolean isKingSide;

	public CastleMove(King piece, int fileDest, boolean isKingSide){
		super(piece, piece.getRank(), fileDest, false);
		this.isKingSide = isKingSide;
	}

	public boolean isKingSide() {
		return isKingSide;
	}
}