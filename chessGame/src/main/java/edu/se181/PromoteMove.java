package edu.se181;

public class PromoteMove extends Move{
	Piece promotePiece;

	public PromoteMove(Pawn piece, int rankDest, int fileDest, boolean isCapture, Piece promotePiece) {
		super(piece, rankDest, fileDest, isCapture);
		this.promotePiece = promotePiece;
	}

	public void setPromotePiece(Piece piece) {
		promotePiece = piece;
	}

	public Piece getPromotePiece() {
		return promotePiece;
	}
}