package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label; 
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.*;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;

public class GameController {
    @FXML private BorderPane mainLayout;
    @FXML private GridPane board;
    @FXML private VBox topContainer;
    @FXML private VBox leftContainer;
    @FXML private VBox rightContainer;
    @FXML private VBox bottomContainer;

    private final int rows = 5;
    private final int cols = 8;
    private double sceneWidth = 800;
    private double sceneHeight = 550;
    private String difficulty;
    private int numberOfPlayers;
    private List<PlayerBoard> playerBoards = new ArrayList<>();
    private int currentPlayer = 0;
    private List<Color> colors = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
    private List<ShapeType> shapes = Arrays.asList(ShapeType.values());
    private int maxRetries = 100;

    public GameController() {
        sceneWidth = 1024;
        sceneHeight = 768;
    }

    public void initializeGame(String difficulty, int playerCount) {
        this.difficulty = difficulty;
        this.numberOfPlayers = playerCount;
        
        // Configure main layout
        mainLayout.setPadding(new Insets(20));
        
        // Calculate optimal board size based on screen dimensions
        double minDimension = Math.min(sceneWidth * 0.7, sceneHeight * 0.7);
        double cellSize = Math.min(85, minDimension / Math.max(rows, cols));
        
        // Configure board
        board.setAlignment(Pos.CENTER);
        board.setHgap(2);
        board.setVgap(2);
        board.setPadding(new Insets(10));
        
        // Set up column constraints
        board.getColumnConstraints().clear();
        for (int i = 0; i < cols; i++) {
            ColumnConstraints cc = new ColumnConstraints(cellSize);
            cc.setHalignment(HPos.CENTER);
            board.getColumnConstraints().add(cc);
        }
        
        // Set up row constraints
        board.getRowConstraints().clear();
        for (int i = 0; i < rows; i++) {
            RowConstraints rc = new RowConstraints(cellSize);
            rc.setValignment(VPos.CENTER);
            board.getRowConstraints().add(rc);
        }
        
        // Configure containers with proportional sizes
        double sideContainerWidth = sceneWidth * 0.15;
        double verticalContainerHeight = sceneHeight * 0.15; 
        
        // Set container sizes
        topContainer.setPrefHeight(verticalContainerHeight);
        bottomContainer.setPrefHeight(verticalContainerHeight);
        leftContainer.setPrefWidth(sideContainerWidth);
        rightContainer.setPrefWidth(sideContainerWidth);
        
        // Add padding to containers
        topContainer.setPadding(new Insets(10));
        bottomContainer.setPadding(new Insets(10));
        leftContainer.setPadding(new Insets(10));
        rightContainer.setPadding(new Insets(10));
        
        generateGameBoard();
        initializePlayerBoards();
        distributePieces();
    }

    private void initializePlayerBoards() {
        topContainer.getChildren().clear();
        leftContainer.getChildren().clear();
        rightContainer.getChildren().clear();
        bottomContainer.getChildren().clear();
        
        // Player 1 (Bottom)
        PlayerBoard player1Board = new PlayerBoard(this);
        player1Board.customizeOrientation(Orientation.HORIZONTAL);
        player1Board.setPrefSize(sceneWidth * 0.4, 120);
        player1Board.setMaxSize(sceneWidth * 0.4, 120);
        VBox player1Container = createPlayerContainer(player1Board, "PLAYER 1", false);
        bottomContainer.getChildren().add(player1Container);
        playerBoards.add(player1Board);

        if (numberOfPlayers >= 2) {
            // Player 2 (Left)
            PlayerBoard player2Board = new PlayerBoard(this);
            player2Board.customizeOrientation(Orientation.VERTICAL);
            player2Board.setPrefSize(120, sceneHeight * 0.4);
            player2Board.setMaxSize(120, sceneHeight * 0.4);
            VBox player2Container = createPlayerContainer(player2Board, "PLAYER 2", false);
            leftContainer.getChildren().add(player2Container);
            playerBoards.add(player2Board);
        }

        if (numberOfPlayers >= 3) {
            // Player 3 (Top)
            PlayerBoard player3Board = new PlayerBoard(this);
            player3Board.customizeOrientation(Orientation.HORIZONTAL);
            player3Board.setPrefSize(sceneWidth * 0.4, 120);
            player3Board.setMaxSize(sceneWidth * 0.4, 120);
            VBox player3Container = createPlayerContainer(player3Board, "PLAYER 3", true);
            topContainer.getChildren().add(player3Container);
            playerBoards.add(player3Board);
        }

        if (numberOfPlayers >= 4) {
            // Player 4 (Right)
            PlayerBoard player4Board = new PlayerBoard(this);
            player4Board.customizeOrientation(Orientation.VERTICAL);
            player4Board.setPrefSize(120, sceneHeight * 0.4);
            player4Board.setMaxSize(120, sceneHeight * 0.4);
            VBox player4Container = createPlayerContainer(player4Board, "PLAYER 4", false);
            rightContainer.getChildren().add(player4Container);
            playerBoards.add(player4Board);
        }

        leftContainer.setPadding(new Insets(0, 20, 0, 20));  // Horizontal
        rightContainer.setPadding(new Insets(0, 20, 0, 20)); // Horizontal 
        topContainer.setPadding(new Insets(20, 0, 20, 0));   // Vertical 
        bottomContainer.setPadding(new Insets(20, 0, 20, 0)); // Vertical
        
        leftContainer.setPrefWidth(sceneWidth * 0.2);   
        rightContainer.setPrefWidth(sceneWidth * 0.2);  
        topContainer.setPrefHeight(sceneHeight * 0.2);  
        bottomContainer.setPrefHeight(sceneHeight * 0.2); 

        // Set initial turn
        playerBoards.get(0).setCurrentTurn(true);
    }

    private VBox createPlayerContainer(PlayerBoard board, String playerLabel, boolean isTop) {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        
        Label label = new Label(playerLabel);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        if (isTop) {
            container.getChildren().addAll(board, label);
        } else {
            container.getChildren().addAll(label, board);
        }
        
        return container;
    }
    
    private void distributePieces() {
        List<GamePiece> allPieces = new ArrayList<>();
        
        int piecesPerCombo = switch(difficulty) {
            case "LEVEL 1" -> 2;
            case "LEVEL 2" -> 3;
            case "LEVEL 3" -> 4;
            default -> 2;
        };

        for (Color color : colors) {
            for (ShapeType shape : shapes) {
                for (int i = 0; i < piecesPerCombo; i++) {
                    allPieces.add(new GamePiece(color, shape));
                }
            }
        }

        Collections.shuffle(allPieces);

        int totalPieces = allPieces.size();
        int piecesPerPlayer = totalPieces / numberOfPlayers;

        for (int i = 0; i < numberOfPlayers; i++) {
            PlayerBoard board = playerBoards.get(i);
            int startIndex = i * piecesPerPlayer;
            int endIndex = Math.min(startIndex + piecesPerPlayer, totalPieces);
            
            for (int j = startIndex; j < endIndex; j++) {
                GamePiece piece = allPieces.get(j);
                board.addPiece(piece.getColor(), piece.getShape());
            }
        }
    }

    private void generateGameBoard() {
        board.getChildren().clear();
        
        // First set a fixed size for the board
        board.setPrefSize(cols * 65, rows * 65);
        board.setMinSize(cols * 65, rows * 65);
        
        List<CellContent> boardCells = generateValidBoard();
        if (boardCells == null || boardCells.size() != rows * cols) {
            showErrorAlert("Board Generation Error", "Failed to generate a valid board. Please try again.");
            return;
        }
    
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                CellContent content = boardCells.get(index++);
                GameBoardCell cell = new GameBoardCell(content.color, content.shape);
                cell.setPrefSize(60, 60);
                cell.setMinSize(60, 60);
                cell.setMaxSize(60, 60);
                board.add(cell, col, row);
            }
        }
    
        // Print debug info
        System.out.println("Generated board with " + board.getChildren().size() + " cells");
        System.out.println("Expected cells: " + (rows * cols));
    }

    private List<CellContent> generateValidBoard() {
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                return generateUniqueBoardCells();
            } catch (IllegalStateException e) {
                continue;
            }
        }
        return null;
    }

    private List<CellContent> generateUniqueBoardCells() {
        List<CellContent> allCells = new ArrayList<>();
        CellContent[][] boardGrid = new CellContent[rows][cols];
        
        List<CellContent> possibleCombinations = new ArrayList<>();
        for (Color color : colors) {
            for (ShapeType shape : shapes) {
                possibleCombinations.add(new CellContent(color, shape));
            }
        }
        
        int duplicates = switch(difficulty) {
            case "LEVEL 1" -> 1;
            case "LEVEL 2" -> 2;
            case "LEVEL 3" -> 3;
            default -> 1;
        };
        
        for (int i = 0; i < duplicates; i++) {
            possibleCombinations.addAll(new ArrayList<>(possibleCombinations));
        }
        
        Random random = new Random();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                List<CellContent> validChoices = new ArrayList<>(possibleCombinations);
                final int currentRow = row;
                final int currentCol = col;
                validChoices.removeIf(cell -> !isValidPlacement(cell, currentRow, currentCol, boardGrid));
                
                if (validChoices.isEmpty()) {
                    throw new IllegalStateException("No valid placement found");
                }
                
                int index = random.nextInt(validChoices.size());
                CellContent chosen = validChoices.get(index);
                boardGrid[row][col] = chosen;
                possibleCombinations.remove(chosen);
                allCells.add(chosen);
            }
        }
        
        return allCells;
    }
    
    private boolean isValidPlacement(CellContent cell, int row, int col, CellContent[][] grid) {
        // Check cell above
        if (row > 0 && grid[row-1][col] != null) {
            if (grid[row-1][col].color.equals(cell.color) || 
                grid[row-1][col].shape == cell.shape) {
                return false;
            }
        }
        
        // Check cell below
        if (row < rows-1 && grid[row+1][col] != null) {
            if (grid[row+1][col].color.equals(cell.color) || 
                grid[row+1][col].shape == cell.shape) {
                return false;
            }
        }
        
        // Check cell left
        if (col > 0 && grid[row][col-1] != null) {
            if (grid[row][col-1].color.equals(cell.color) || 
                grid[row][col-1].shape == cell.shape) {
                return false;
            }
        }
        
        // Check cell right
        if (col < cols-1 && grid[row][col+1] != null) {
            if (grid[row][col+1].color.equals(cell.color) || 
                grid[row][col+1].shape == cell.shape) {
                return false;
            }
        }
        
        return true;
    }

    public boolean tryPlacePiece(double sceneX, double sceneY, Color pieceColor, ShapeType pieceShape) {
        if (!playerBoards.get(currentPlayer).isCurrentTurn()) {
            return false;
        }
    
        // Convert scene coordinates to board coordinates
        Node boardNode = board;
        double localX = boardNode.sceneToLocal(sceneX, sceneY).getX();
        double localY = boardNode.sceneToLocal(sceneX, sceneY).getY();
    
        // Check if the drop location is within the board bounds
        if (!board.getBoundsInLocal().contains(localX, localY)) {
            return false;
        }
    
        // Find the cell at drop location
        for (Node node : board.getChildren()) {
            if (node instanceof GameBoardCell cell) {
                if (cell.getBoundsInParent().contains(localX, localY)) {
                    if (cell.isEmpty() && cell.matches(new GamePiece(pieceColor, pieceShape))) {
                        cell.placePiece(new GamePiece(pieceColor, pieceShape));
                        nextTurn();
                        checkGameEnd();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void nextTurn() {
        playerBoards.get(currentPlayer).setCurrentTurn(false);
        currentPlayer = (currentPlayer + 1) % numberOfPlayers;
        playerBoards.get(currentPlayer).setCurrentTurn(true);
        
        for (int i = 0; i < playerBoards.size(); i++) {
            PlayerBoard board = playerBoards.get(i);
            board.setStyle("-fx-border-color: " + (i == currentPlayer ? "gold" : "#cccccc") + 
                          "; -fx-border-width: " + (i == currentPlayer ? "3" : "2") + ";");
        }
    }

    private void checkGameEnd() {
        boolean boardFull = true;
        for (Node node : board.getChildren()) {
            if (node instanceof GameBoardCell cell && cell.isEmpty()) {
                boardFull = false;
                break;
            }
        }

        if (boardFull) {
            showGameWonDialog();
        }
    }

    private void showGameWonDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText("All pieces have been matched correctly!");
        alert.showAndWait();
    }

    @FXML
    private void handleNewGame() {
        Scene currentScene = board.getScene();
        if (currentScene != null) {
            sceneWidth = currentScene.getWidth();
            sceneHeight = currentScene.getHeight();
        }
        
        currentPlayer = 0;
        generateGameBoard();
        playerBoards.clear();
        initializePlayerBoards();
        distributePieces();
    }

    @FXML
    private void handleBackToDifficulty(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/Difficulty.fxml"));
        Parent difficultyScreen = loader.load();
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        Scene scene = new Scene(difficultyScreen, stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static class CellContent {
        Color color;
        ShapeType shape;

        CellContent(Color color, ShapeType shape) {
            this.color = color;
            this.shape = shape;
        }
    }
}