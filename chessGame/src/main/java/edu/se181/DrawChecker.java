package edu.se181;

import java.util.List;

public class DrawChecker {

    public DrawChecker(){}

    public boolean isStalemate(Board board){
		return false;
	}

	public boolean isDeadPosition(Board board){
    	//going under the assumption that the pieces always contain the king
    	List<Piece> whitePieces = board.getWhitePieces();
    	List<Piece> blackPieces = board.getBlackPieces();

    	int whitePiecesSize = whitePieces.size();
    	int blackPiecesSize = blackPieces.size();

    	Piece whiteBishop = null;
		Piece blackBishop= null;
		Piece whiteKnight = null;
		Piece blackKnight = null;

		if (whitePiecesSize > 2 || blackPiecesSize > 2){
			return false;
		}

		for(Piece piece : whitePieces){
			if (piece instanceof Bishop){
				whiteBishop = piece;
			}
			else if (piece instanceof Knight){
				whiteKnight = piece;
			}
		}

		for(Piece piece : blackPieces){
			if (piece instanceof Bishop){
				blackBishop = piece;
			}
			else if (piece instanceof Knight){
				blackKnight = piece;
			}
		}

    	if (whitePiecesSize == 1 && whitePieces.get(0) instanceof King){
			if (blackPiecesSize == 1 && blackPieces.get(0) instanceof King){
				//king vs king
				return true;
			}
			else if (blackPiecesSize == 2){
				if (blackKnight != null || blackBishop != null){
					//king vs king and bishop
					//king vs king and knight
					return true;
				}
			}
		}
    	else if (whitePiecesSize == 2 && blackPiecesSize == 2){
			if (whiteBishop != null && blackBishop != null){
				boolean whiteBishopTileColor = (whiteBishop.getRank() + whiteBishop.getFile()) % 2 == 0;
				boolean blackBishopTileColor = (whiteBishop.getRank() + whiteBishop.getFile()) % 2 == 0;

				if (whiteBishopTileColor == blackBishopTileColor){
					//king and bishop vs king and bishop with same tile color bishops
					return true;
				}
			}
		}

    	if (blackPiecesSize == 1  && whitePiecesSize == 2){
			if (whiteBishop != null || whiteKnight != null){
				return true;
			}
		}
		return false;
	}
}