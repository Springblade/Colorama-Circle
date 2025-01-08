package main;

import javafx.scene.paint.Color;

public class GamePiece {
    private Color color;
    private ShapeType shape;
    
    public GamePiece(Color color, ShapeType shape) {
        this.color = color;
        this.shape = shape;
    }
    
    public Color getColor() {
        return color;
    }
    
    public ShapeType getShape() {
        return shape;
    }
    
    public boolean matches(GamePiece other) {
        return this.color.equals(other.color) && this.shape == other.shape;
    }
}