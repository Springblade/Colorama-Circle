import java.util.Random;

public class Broad {
    // Enum for shapes
    enum ShapeType {
        CIRCLE, SQUARE, TRIANGLE, HEXAGON, TRAPEZOID
    }

    // Class to represent a cell
    static class Cell {
        String color;
        ShapeType shape;

        Cell(String color, ShapeType shape) {
            this.color = color;
            this.shape = shape;
        }

        @Override
        public String toString() {
            return color + ", " + shape;
        }
    }
    private static final int rows = 5; // 5 rows
    private static final int columns = 8; // 8 columns
    private static Random random = new Random();

    // Colors array
    private static String[] colors = {"RED", "BLUE", "GREEN", "YELLOW"};

    // Grid
    private static Cell[][] grid = new Cell[rows][columns];

    static {
        // Populate the grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // Random color and shape
                String color = colors[random.nextInt(colors.length)];
                ShapeType shapeType = ShapeType.values()[random.nextInt(ShapeType.values().length)];

                // Store in grid
                grid[i][j] = new Cell(color, shapeType);
            }
        }
    }

    // Getter for the grid
    public static Cell[][] getGrid() {
        return grid;
    }

    public static void main(String[] args) {
        // Printing the grid to show how it looks (if needed)
        /*System.out.println("Generated Grid:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.printf("(%d, %d): %s\t", i, j, grid[i][j]);
            }
            System.out.println();
        }*/
    }
}
