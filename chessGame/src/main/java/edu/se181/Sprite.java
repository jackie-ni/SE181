package edu.se181;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends ImageView {
    public Image sprite;
    private int x;
    private int y;

    public void setPosition(int x,int y){
        this.x = x;
        this.y = y;
    }
    public int getXPos(){
        return x;
    }
    public int getYPos(){
        return y;
    }

    Sprite(Image sprite){
        super(sprite);
        setPreserveRatio(true);
    }

    public void setSize(int width, int height){
        setFitWidth(width);
        setFitHeight(height);
    }
}
