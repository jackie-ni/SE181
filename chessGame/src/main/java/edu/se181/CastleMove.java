package edu.se181;

public class CastleMove extends Move{
	boolean isKingSide;

	public CastleMove(King piece, boolean isKingSide){
		super(piece, piece.getRank(), isKingSide ? piece.getFile()+2: piece.getFile()-2, false);
		this.isKingSide = isKingSide;
	}

	public boolean isKingSide() {
		return isKingSide;
	}
}