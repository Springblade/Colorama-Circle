public class GamePiece {
    // Constructor that accepts a cell
    private String color;
    private String shape;

    public GamePiece(Broad.Cell cell) {
        this.color = cell.color;
        this.shape = cell.shape.toString();
    }

    public void displayPiece() {
        System.out.println("Piece - Color: " + color + ", Shape: " + shape);
    }

    public static void main(String[] args) {
        // Get the grid from Broad
        Broad.Cell[][] grid = Broad.getGrid();

        // Create GamePieces based on the grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                // Create GamePiece for each cell in the grid
                GamePiece piece = new GamePiece(grid[i][j]);
                piece.displayPiece(); // Display piece details
            }
        }
    }
}
