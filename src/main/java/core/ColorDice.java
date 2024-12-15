package core;

import java.util.Random;

public class ColorDice {
    private String color;
    private final Random random;

    // Define the colors
    private static final String[] COLORS = {"Red", "Blue", "Green", "Yellow"};

    // Constructor
    public ColorDice() {
        random = new Random();
        initialize(); // Initialize with a random color
    }

    // Initialization method
    private void initialize() {
        roll();
    }

    // Method to roll the dice
    public void roll() {
        int index = random.nextInt(COLORS.length);
        color = COLORS[index];
    }

    // Getter for the color
    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "ColorDice{" +
                "color='" + color + '\'' +
                '}';
    }
}