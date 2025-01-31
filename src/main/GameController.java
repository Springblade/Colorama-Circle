package main;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label; 
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.*;

import javafx.util.Duration;
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
    @FXML private VBox farLeftContainer; 
    @FXML private VBox farRightContainer;
    @FXML private Label diceRollResultLabel;
    @FXML private Button rollButton;

    private final int rows = 5;
    private final int cols = 8;
    private double sceneWidth = 800;
    private double sceneHeight = 550;
    private String difficulty;
    private int numberOfPlayers;
    private List<PlayerBoard> playerBoards = new ArrayList<>();
    private int currentPlayer = 0;
    private List<ShapeType> shapes = Arrays.asList(ShapeType.values());
    private int maxRetries = 100;
    private Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    private Random random = new Random();
    private Color rolledColor;
    private boolean isJoker = false;
    private boolean initialized = false;
    //private Button rollButton;

    private Map<Color, String> colorNames = Map.of(
        Color.RED, "Red",
        Color.GREEN, "Green",
        Color.BLUE, "Blue",
        Color.YELLOW, "Yellow"
    );

    public GameController() {
        sceneWidth = 1024;
        sceneHeight = 768;
    }

    public void initializeGame(String difficulty, int playerCount, int startingPlayerIndex) {
        this.difficulty = difficulty;
        this.numberOfPlayers = playerCount;
        this.currentPlayer = startingPlayerIndex;

        mainLayout.setPadding(new Insets(20));
        
        double minDimension = Math.min(sceneWidth * 0.7, sceneHeight * 0.7);
        double cellSize = Math.min(85, minDimension / Math.max(rows, cols));
        
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

        if (rollButton != null) { 
            if ("LEVEL 2".equals(difficulty)) {
                rollButton.setVisible(true);
                rollButton.setDisable(false);
                
                // Add hover effects
                rollButton.setOnMouseEntered(e -> {
                    if (!rollButton.isDisabled()) {
                        rollButton.setStyle(rollButton.getStyle().replace("#4CAF50", "#45a049"));
                    }
                });
                
                rollButton.setOnMouseExited(e -> {
                    if (!rollButton.isDisabled()) {
                        rollButton.setStyle(rollButton.getStyle().replace("#45a049", "#4CAF50"));
                    }
                });
            } else {
                rollButton.setVisible(false);
            }
        }
        
        if ("LEVEL 1".equals(difficulty)) {
            generateEmptyGameBoard();
            initializePlayerBoards();
            distributePieces();
        } else if ("LEVEL 2".equals(difficulty)) {
            generateFilledGameBoard();
            initializePlayerBoards();
            disableBoardCells();
        }
        initialized = true;
        playerBoards.get(currentPlayer).setCurrentTurn(true);
    }

    private void disableBoardCells() {
        for (GameBoardCell cell : getAllCells()) {
            cell.setDisable(true);
        }
    }

    private void initializePlayerBoards() {
        topContainer.getChildren().clear();
        leftContainer.getChildren().clear();
        rightContainer.getChildren().clear();
        bottomContainer.getChildren().clear();
        farLeftContainer.getChildren().clear();
        farRightContainer.getChildren().clear();
        
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
            player2Board.setPrefSize(150, sceneHeight * 0.4);
            player2Board.setMaxSize(150, sceneHeight * 0.4);
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
            player4Board.setPrefSize(150, sceneHeight * 0.4);
            player4Board.setMaxSize(150, sceneHeight * 0.4);
            VBox player4Container = createPlayerContainer(player4Board, "PLAYER 4", false);
            rightContainer.getChildren().add(player4Container);
            playerBoards.add(player4Board);
        }
    
        if (numberOfPlayers >= 5) {
            // Player 5 (Far left)
            PlayerBoard player5Board = new PlayerBoard(this);
            player5Board.customizeOrientation(Orientation.VERTICAL);
            player5Board.setPrefSize(150, sceneHeight * 0.4);
            player5Board.setMaxSize(150, sceneHeight * 0.4);
            VBox player5Container = createPlayerContainer(player5Board, "PLAYER 5", false);
            farLeftContainer.getChildren().add(player5Container);
            playerBoards.add(player5Board);
        }
    
        if (numberOfPlayers >= 6) {
            // Player 6 (Far right)
            PlayerBoard player6Board = new PlayerBoard(this);
            player6Board.customizeOrientation(Orientation.VERTICAL);
            player6Board.setPrefSize(150, sceneHeight * 0.4);
            player6Board.setMaxSize(150, sceneHeight * 0.4);
            VBox player6Container = createPlayerContainer(player6Board, "PLAYER 6", false);
            farRightContainer.getChildren().add(player6Container);
            playerBoards.add(player6Board);
        }
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
        
        // Create all possible piece combinations
        for (Color color : colors) {
            for (ShapeType shape : shapes) {
                for (int i = 0; i < 2; i++) { 
                    allPieces.add(new GamePiece(color, shape));
                }
            }
        }
    
        Collections.shuffle(allPieces);
        
        // Calculate pieces per player and leftover pieces based on number of players
        int totalPieces = allPieces.size(); 
        int piecesPerPlayer;
        int leftoverPieces;
        
        switch (numberOfPlayers) {
            case 1:
                piecesPerPlayer = 40;
                leftoverPieces = 0;
                break;
            case 2:
                piecesPerPlayer = 20;
                leftoverPieces = 0;
                break;
            case 3:
                piecesPerPlayer = 13;
                leftoverPieces = 1;
                break;
            case 4:
                piecesPerPlayer = 10;
                leftoverPieces = 0;
                break;
            case 5:
                piecesPerPlayer = 8;
                leftoverPieces = 0;
                break;
            case 6:
                piecesPerPlayer = 6;
                leftoverPieces = 4;
                break;
            default:
                throw new IllegalStateException("Unsupported number of players: " + numberOfPlayers);
        }
    
        // Distribute pieces to players
        for (int i = 0; i < numberOfPlayers; i++) {
            PlayerBoard board = playerBoards.get(i);
            int startIndex = i * piecesPerPlayer;
            int endIndex = startIndex + piecesPerPlayer;
            
            for (int j = startIndex; j < endIndex; j++) {
                GamePiece piece = allPieces.get(j);
                board.addPiece(piece.getColor(), piece.getShape());
            }
        }
    
        // Place leftover pieces on the board if any
        if (leftoverPieces > 0) {
            int startIndex = numberOfPlayers * piecesPerPlayer;
            for (int i = 0; i < leftoverPieces; i++) {
                GamePiece piece = allPieces.get(startIndex + i);
                // Find an empty cell to place the leftover piece
                for (Node node : board.getChildren()) {
                    if (node instanceof GameBoardCell cell && cell.isEmpty() && 
                        cell.matches(piece)) {
                        cell.placePiece(piece);
                        break;
                    }
                }
            }
        }
    }

    private void generateEmptyGameBoard() {
        board.getChildren().clear();
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
                GameBoardCell cell = new GameBoardCell(content.color, content.shape, "LEVEL 1");
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

    private void generateFilledGameBoard() {
        board.getChildren().clear();
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
                GameBoardCell cell = new GameBoardCell(content.color, content.shape, "LEVEL 2");
                cell.setPrefSize(60, 60);
                cell.setMinSize(60, 60);
                cell.setMaxSize(60, 60);
                board.add(cell, col, row);
            }
        }
    
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
                        checkGameEnd1();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // LEVEL 2
    public boolean tryPickPiece(double sceneX, double sceneY, Color rolledColor) {
        Node boardNode = board;
        double localX = boardNode.sceneToLocal(sceneX, sceneY).getX();
        double localY = boardNode.sceneToLocal(sceneX, sceneY).getY();
        // Check if the pick location is within the board bounds
        if (!board.getBoundsInLocal().contains(localX, localY)) {
            return false;
        }
    
        // Find the cell at pick location
        for (Node node : board.getChildren()) {
            if (node instanceof GameBoardCell cell) {
                if (cell.getBoundsInParent().contains(localX, localY)) {
                    // If rolledColor is null, allow picking any piece (joker case)
                    if (!cell.isEmpty() && (rolledColor == null || cell.matchesColor(rolledColor))) {
                        cell.removePiece();
                        checkGameEnd2();
                        nextTurn();
                        return true;
                    } else {
                        diceRollResultLabel.setText("Picked wrong color. Lose a turn.");
                        System.out.println("Picked wrong color. Lose a turn.");
                        nextTurn();
                        return false;
                    }
                }
            }
        }
        return false;
    }

    private void nextTurn() {
        // Enable the roll button for the next player
        if (rollButton != null) {
            rollButton.setDisable(false);
        }
        
        // End the current player's turn
        playerBoards.get(currentPlayer).setCurrentTurn(false);
        
        // Advance to the next player
        currentPlayer = (currentPlayer + 1) % numberOfPlayers;
        
        // Start the next player's turn
        playerBoards.get(currentPlayer).setCurrentTurn(true);
        
        for (int i = 0; i < playerBoards.size(); i++) {
            PlayerBoard board = playerBoards.get(i);
            board.setStyle("-fx-border-color: " + (i == currentPlayer ? "gold" : "#cccccc") + 
                          "; -fx-border-width: " + (i == currentPlayer ? "3" : "2") + ";");
        }
        
        // Reset the dice roll result label
        if (diceRollResultLabel != null) {
            diceRollResultLabel.setText("");
        }
        removeHighlights();
    }

    private void checkGameEnd1() {
        boolean boardFull = true;
        for (Node node : board.getChildren()) {
            if (node instanceof GameBoardCell cell && cell.isEmpty()) {
                boardFull = false;
                break;
            }
        }

        if (boardFull) {
            showGameWonDialog1();
        }
    }

    private void showGameWonDialog1() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("🎉 Level 1 Complete! 🎉");
    
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);
    
        Label levelCompleteLabel = new Label("LEVEL 1 COMPLETE!");
        levelCompleteLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    
        Label completionMessage = new Label("All Pieces Matched Successfully!");
        completionMessage.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
    
        VBox scoresBox = new VBox(5);
        scoresBox.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 5;");
        scoresBox.setAlignment(Pos.CENTER_LEFT);
    
        Label scoresTitle = new Label("Scores:");
        scoresTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        scoresBox.getChildren().add(scoresTitle);
    
        int matchedPieces = 100; 
        int totalPieces = 100;
        Label scoreDetail = new Label(String.format("Matched Pieces: %d / %d", matchedPieces, totalPieces));
        scoreDetail.setStyle("-fx-font-size: 14px; -fx-text-fill: #2C3E50;");
        scoresBox.getChildren().add(scoreDetail);
    
        contentBox.getChildren().addAll(levelCompleteLabel, completionMessage, scoresBox);
    
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #FFFFFF, #FFF8E7);");
        dialogPane.setContent(contentBox);
        dialogPane.setHeaderText(null);
        dialogPane.setGraphic(null);
    
        alert.showAndWait();
    }
    

    private void checkGameEnd2() {
        boolean boardEmpty = true;
        for (Node node : board.getChildren()) {
            if (node instanceof GameBoardCell cell && !cell.isEmpty()) {
                boardEmpty = false;
                break;
            }
        }
    
        if (boardEmpty) {
            showGameWonDialog2();
        }
    }

    private void showGameWonDialog2() {
        int maxPieces = -1;
        int winnerIndex = -1;
        
        for (int i = 0; i < playerBoards.size(); i++) {
            int pieceCount = playerBoards.get(i).getChildren().size();
            if (pieceCount > maxPieces) {
                maxPieces = pieceCount;
                winnerIndex = i;
            }
        }
        
        List<Integer> winners = new ArrayList<>();
        for (int i = 0; i < playerBoards.size(); i++) {
            if (playerBoards.get(i).getChildren().size() == maxPieces) {
                winners.add(i + 1);
            }
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("🎮 Game Over! 🎮");
        
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);
        
        Label gameOverLabel = new Label("LEVEL 2 COMPLETE!");
        gameOverLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    
        Label winnerLabel = new Label();
        if (winners.size() > 1) {
            winnerLabel.setText("DRAW!");
        } else {
            winnerLabel.setText("PLAYER " + (winnerIndex + 1) + " WINS!");
        }
        winnerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
        
        VBox scoresBox = new VBox(5);
        scoresBox.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 5;");
        scoresBox.setAlignment(Pos.CENTER_LEFT);
        
        Label scoresTitle = new Label("Final Scores:");
        scoresTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        scoresBox.getChildren().add(scoresTitle);
        
        // Score bars
        for (int i = 0; i < playerBoards.size(); i++) {
            int pieceCount = playerBoards.get(i).getChildren().size();
            
            HBox playerScoreBox = new HBox(10);
            playerScoreBox.setAlignment(Pos.CENTER_LEFT);
            
            Label playerLabel = new Label(String.format("Player %d:", i + 1));
            Label scoreLabel = new Label(pieceCount + " pieces");
            
            if (pieceCount == maxPieces) {
                playerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4CAF50;");
                scoreLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4CAF50;");
            }
            
            playerScoreBox.getChildren().addAll(playerLabel, scoreLabel);
            scoresBox.getChildren().add(playerScoreBox);
        }
        
        contentBox.getChildren().addAll(gameOverLabel, winnerLabel, scoresBox);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #FFFFFF, #FFF8E7);");
        dialogPane.setContent(contentBox);
        dialogPane.setHeaderText(null);
        dialogPane.setGraphic(null);
        
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
        playerBoards.clear();

        // Reinitialize the game with the same difficulty and number of players
        initializeGame(difficulty, numberOfPlayers, currentPlayer);
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

    private boolean checkColorAvailable(Color color) {
        for (Node node : board.getChildren()) {
            if (node instanceof GameBoardCell cell) {
                if (!cell.isEmpty() && cell.matchesColor(color)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void handleDiceRoll() {
        rollButton.setDisable(true);
        
        int roll = random.nextInt(6);
        rolledColor = null;
        isJoker = false;
       
        diceRollResultLabel.setVisible(true);
        diceRollResultLabel.setManaged(true);
        diceRollResultLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        VBox topBox = (VBox) mainLayout.getTop();
        if (!topBox.getChildren().contains(diceRollResultLabel)) {
            topBox.getChildren().add(0, diceRollResultLabel);
        }
        BorderPane.setAlignment(topBox, Pos.CENTER);
        BorderPane.setMargin(topBox, new Insets(20, 0, 0, 0));
    
        if (roll < 4) {
            rolledColor = colors[roll];
            String colorName = colorNames.get(rolledColor);
            
            // Check if the rolled color is available
            if (!checkColorAvailable(rolledColor)) {
                diceRollResultLabel.setText("No " + colorName + " pieces available. Skipping turn.");
                System.out.println("No " + colorName + " pieces available. Skipping turn.");
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> nextTurn());
                pause.play();
                return;
            }
            
            diceRollResultLabel.setText("Rolled color: " + colorName);
            System.out.println("Rolled color: " + colorName);
        } else if (roll == 4) {
            diceRollResultLabel.setText("Rolled blank. Lose a turn.");
            System.out.println("Rolled blank. Lose a turn.");
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> nextTurn());
            pause.play();
            return;
        } else if (roll == 5) {
            // Check if there are any pieces left at all
            boolean anyPiecesLeft = false;
            for (Color color : colors) {
                if (checkColorAvailable(color)) {
                    anyPiecesLeft = true;
                    break;
                }
            }
            
            if (!anyPiecesLeft) {
                diceRollResultLabel.setText("No pieces left to pick. Game Over!");
                System.out.println("No pieces left to pick. Game Over!");
                showGameWonDialog2();
                return;
            }
            
            isJoker = true;
            diceRollResultLabel.setText("Rolled lucky joker. Pick any piece.");
            System.out.println("Rolled lucky joker. Pick any piece.");
        }
    
        for (GameBoardCell cell : getAllCells()) {
            cell.setDisable(false);
            cell.setOnMouseClicked(null);
            
            if (isJoker || cell.matchesColor(rolledColor)) {
                cell.highlightAsValidTarget();
                cell.setOnMouseClicked(e -> {
                    if (!cell.isEmpty() && (isJoker || cell.matchesColor(rolledColor))) {
                        Color pieceColor = cell.getColor();
                        ShapeType pieceShape = cell.getShapeType();
                        cell.removePiece();
                        PlayerBoard currentPlayerBoard = playerBoards.get(currentPlayer);
                        currentPlayerBoard.addPiece(pieceColor, pieceShape);
                        
                        if (isJoker) {
                            System.out.println("Picked correct piece (joker).");
                        } else {
                            String colorName = colorNames.get(rolledColor);
                            System.out.println("Picked correct color: " + colorName);
                        }
                        
                        checkGameEnd2();
                        removeHighlights();
                        nextTurn();
                    } else {
                        diceRollResultLabel.setText("Picked wrong color. Lose a turn.");
                        System.out.println("Picked wrong color. Lose a turn.");
                        removeHighlights();
                        nextTurn();
                    }
                });
            }
        }
    }

    private List<GameBoardCell> getAllCells() {
        List<GameBoardCell> cells = new ArrayList<>();
        for (Node node : board.getChildren()) {
            if (node instanceof GameBoardCell) {
                cells.add((GameBoardCell) node);
            }
        }
        return cells;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Color getRolledColor() {
        return rolledColor;
    }

    public void removeHighlights() {
        for (GameBoardCell cell : getAllCells()) {
            cell.removeHighlight();
        }
    }

    public boolean isInitialized() {
        return initialized;
    }
    public void TestWinner() {
        showGameWonDialog1();
        showGameWonDialog2();
    }
}
