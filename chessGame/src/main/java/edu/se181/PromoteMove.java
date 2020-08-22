package edu.se181;

public class PromoteMove extends Move{
	Piece promotePiece;

	public PromoteMove(Pawn piece, int rankDest, int fileDest, Piece promotePiece) {
		super(piece, rankDest, fileDest, false);
		this.promotePiece = promotePiece;
	}

	public Piece getPromotePiece() {
		return promotePiece;
	}
}