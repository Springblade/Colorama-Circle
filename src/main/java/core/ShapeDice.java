package core;

import java.util.Random;

public class ShapeDice {
    private String shape;
    private Random random;

    // Define the shapes
    private static final String[] SHAPES = {"Circle", "Square", "Triangle", "Hexagon", "Trapezoid"};

    // Constructor
    public ShapeDice() {
        random = new Random();
        roll(); // Initialize with a random shape
    }

    // Method to roll the dice
    public void roll() {
        int index = random.nextInt(SHAPES.length);
        shape = SHAPES[index];
    }

    // Getter for the shape
    public String getShape() {
        return shape;
    }

    @Override
    public String toString() {
        return "ShapeDice{" +
                "shape='" + shape + '\'' +
                '}';
    }
}
