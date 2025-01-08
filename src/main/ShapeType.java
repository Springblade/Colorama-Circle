package main;

public enum ShapeType {
    CIRCLE,
    SQUARE,
    TRIANGLE,
    HEXAGON,
    TRAPEZOID;
    
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}