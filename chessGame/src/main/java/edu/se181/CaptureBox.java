package edu.se181;

import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CaptureBox extends VBox {
    public ArrayList<Sprite> capturedPieces = new ArrayList<>();

    public ArrayList<Sprite> getCapturedPieces(){
        return this.capturedPieces;
    }
    public void setCapturedPieces(ArrayList<Sprite> pieces){
        this.capturedPieces = pieces;
    }
    public void addCapturedPiece(Sprite piece){
        this.capturedPieces.add(piece);
    }

    public void showPiece(Sprite piece){
        getChildren().add(piece);
    }
}
