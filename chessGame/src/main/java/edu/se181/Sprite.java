package edu.se181;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends ImageView {
    public Image sprite;

    Sprite(Image sprite){
        super(sprite);
        setPreserveRatio(true);
    }

    public void setSize(int width, int height){
        setFitWidth(width);
        setFitHeight(height);
    }
}
