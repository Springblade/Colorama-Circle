public class BroadTest {
    public static void main(String[] args) {
        // Fetch the grid
        Broad.Cell[][] grid = Broad.getGrid();

        // Verify the grid dimensions
        assert grid.length == 5 : "Row count should be 5";
        assert grid[0].length == 8 : "Column count should be 8";

        // Verify each cell has valid color and shape
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Broad.Cell cell = grid[i][j];
                assert cell != null : "Cell should not be null";
                assert cell.color != null : "Cell color should not be null";
                assert cell.shape != null : "Cell shape should not be null";

                // Check if color is valid
                boolean validColor = false;
                for (String color : new String[]{"RED", "BLUE", "GREEN", "YELLOW"}) {
                    if (color.equals(cell.color)) {
                        validColor = true;
                        break;
                    }
                }
                assert validColor : "Invalid color: " + cell.color;

                // Check if shape is valid
                boolean validShape = false;
                for (Broad.ShapeType shape : Broad.ShapeType.values()) {
                    if (shape == cell.shape) {
                        validShape = true;
                        break;
                    }
                }
                assert validShape : "Invalid shape: " + cell.shape;
            }
        }

        System.out.println("BroadTest passed!");
    }
}
