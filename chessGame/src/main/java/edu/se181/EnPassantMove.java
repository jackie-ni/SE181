package edu.se181;

public class EnPassantMove extends Move{
	public EnPassantMove(Pawn piece, int rankDest, int fileDest){
		super(piece, rankDest, fileDest, true);
	}
}

