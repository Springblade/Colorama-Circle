public class GamePieceTest {
    public static void main(String[] args) {
        // Fetch the grid from Broad
        Broad.Cell[][] grid = Broad.getGrid();

        // Test the creation of GamePieces and their display
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Broad.Cell cell = grid[i][j];
                GamePiece piece = new GamePiece(cell);

                // Verify the piece's properties match the cell
                assert piece != null : "GamePiece should not be null";
                assert cell.color.equals(piece.getColor()) : 
                    "GamePiece color mismatch. Expected: " + cell.color + ", Found: " + piece.getColor();
                assert cell.shape.toString().equals(piece.getShape()) : 
                    "GamePiece shape mismatch. Expected: " + cell.shape + ", Found: " + piece.getShape();
            }
        }

        System.out.println("GamePieceTest passed!");
    }
}
