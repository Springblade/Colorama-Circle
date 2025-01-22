package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class DifficultyController {
    
    @FXML
    private void handleEasy(ActionEvent event) throws Exception {
        int playerCount = promptForPlayerCount("LEVEL 1");
        if (playerCount > 0) {
            loadGame(event, "LEVEL 1", playerCount);
        }
    }
    
    @FXML
    private void handleMedium(ActionEvent event) throws Exception {
        int playerCount = promptForPlayerCount("LEVEL 2");
        if (playerCount >= 2) {
            loadGame(event, "LEVEL 2", playerCount);
        } else if (playerCount > 0) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invalid Player Count");
            alert.setHeaderText(null);
            alert.setContentText("Level 2 requires at least 2 players. Please select 2-6 players.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleHard(ActionEvent event) throws Exception {
        // Not available yet
        showLevelNotAvailableAlert("LEVEL 3");
    }
    
    private int promptForPlayerCount(String difficulty) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Number of Players");

        VBox content = new VBox(40);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        content.setMinWidth(400);
        content.setMinHeight(250);

        Label titleLabel = new Label("SELECT PLAYERS");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");
        
        Label instructionsLabel = new Label("Enter number of players (1-6)");
        instructionsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666666;");
        
        // Set default player count based on difficulty level
        String defaultPlayers = difficulty.equals("LEVEL 2") ? "2" : "1";
        TextField playerInput = new TextField(defaultPlayers);
        playerInput.setMaxWidth(200);
        playerInput.setAlignment(Pos.CENTER);
        playerInput.setStyle("-fx-font-size: 24px; -fx-padding: 10; " +
                           "-fx-background-radius: 10; -fx-border-radius: 10; " +
                           "-fx-border-color: #cccccc; -fx-border-width: 2;");

        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button confirmButton = new Button("CONFIRM");
        Button cancelButton = new Button("CANCEL");
        
        String baseButtonStyle = "-fx-min-width: 120; -fx-max-width: 120; " +
                                "-fx-min-height: 40; -fx-max-height: 40; " +  
                                "-fx-font-size: 14px; -fx-font-weight: bold; " +
                                "-fx-background-radius: 20; -fx-cursor: hand; ";
        
        confirmButton.setStyle(baseButtonStyle +
                             "-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        cancelButton.setStyle(baseButtonStyle +
                            "-fx-background-color: #E74C3C; -fx-text-fill: white;");
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5);
        dropShadow.setSpread(0.1);
        dropShadow.setOffsetX(2);
        dropShadow.setOffsetY(2);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        
        confirmButton.setEffect(dropShadow);
        cancelButton.setEffect(dropShadow);
        
        buttonBox.getChildren().addAll(confirmButton, cancelButton);
        
        confirmButton.setOnAction(e -> dialog.setResult("OK"));
        cancelButton.setOnAction(e -> dialog.setResult("CANCEL"));

        content.getChildren().addAll(titleLabel, instructionsLabel, playerInput, buttonBox);

        // Hover effects
        confirmButton.setOnMouseEntered(e -> 
            confirmButton.setStyle(baseButtonStyle + "-fx-background-color: #45a049; -fx-text-fill: white;"));
        confirmButton.setOnMouseExited(e -> 
            confirmButton.setStyle(baseButtonStyle + "-fx-background-color: #4CAF50; -fx-text-fill: white;"));
        
        cancelButton.setOnMouseEntered(e -> 
            cancelButton.setStyle(baseButtonStyle + "-fx-background-color: #d32f2f; -fx-text-fill: white;"));
        cancelButton.setOnMouseExited(e -> 
            cancelButton.setStyle(baseButtonStyle + "-fx-background-color: #E74C3C; -fx-text-fill: white;"));

        playerInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                playerInput.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (!newValue.isEmpty()) {
                int value = Integer.parseInt(newValue);
                if (value > 6) playerInput.setText("6");
                else if (value < 1) playerInput.setText("1");
            }
        });

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #FFFFFF, #FFF8E7);");
        dialogPane.getButtonTypes().clear();

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && result.get().equals("OK")) {
            try {
                int count = Integer.parseInt(playerInput.getText().trim());
                if (count >= 1 && count <= 6) {
                    return count;
                } else {
                    showInvalidPlayerCountAlert();
                }
            } catch (NumberFormatException e) {
                showInvalidPlayerCountAlert();
            }
        }
        return -1;
    }
    
    private class PlayerInfo {
        int playerNumber;
        int age;
    
        PlayerInfo(int playerNumber, int age) {
            this.playerNumber = playerNumber;
            this.age = age;
        }
    }

    private List<PlayerInfo> promptForPlayerAges(int playerCount) {
        List<PlayerInfo> players = new ArrayList<>();
        
        for (int i = 1; i <= playerCount; i++) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Player " + i + " Age");
    
            VBox content = new VBox(40);
            content.setAlignment(Pos.CENTER);
            content.setPadding(new Insets(20));
            content.setMinWidth(400);
            content.setMinHeight(250);
    
            Label titleLabel = new Label("PLAYER " + i + " AGE");
            titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");
            
            Label instructionsLabel = new Label("Enter age for Player " + i);
            instructionsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666666;");
            
            TextField ageInput = new TextField();
            ageInput.setMaxWidth(200);
            ageInput.setAlignment(Pos.CENTER);
            ageInput.setStyle("-fx-font-size: 24px; -fx-padding: 10; " +
                             "-fx-background-radius: 10; -fx-border-radius: 10; " +
                             "-fx-border-color: #cccccc; -fx-border-width: 2;");
    
            HBox buttonBox = new HBox(30);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setPadding(new Insets(20, 0, 0, 0));
            
            Button confirmButton = new Button("CONFIRM");
            Button cancelButton = new Button("CANCEL");
            
            String baseButtonStyle = "-fx-min-width: 120; -fx-max-width: 120; " +
                                   "-fx-min-height: 40; -fx-max-height: 40; " +  
                                   "-fx-font-size: 14px; -fx-font-weight: bold; " +
                                   "-fx-background-radius: 20; -fx-cursor: hand; ";
            
            confirmButton.setStyle(baseButtonStyle + 
                                 "-fx-background-color: #4CAF50; -fx-text-fill: white;");
            cancelButton.setStyle(baseButtonStyle + 
                                "-fx-background-color: #E74C3C; -fx-text-fill: white;");
            
            buttonBox.getChildren().addAll(confirmButton, cancelButton);
            
            confirmButton.setOnAction(e -> dialog.setResult("OK"));
            cancelButton.setOnAction(e -> dialog.setResult("CANCEL"));
    
            // Add validation to TextField
            ageInput.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    ageInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
    
            content.getChildren().addAll(titleLabel, instructionsLabel, ageInput, buttonBox);
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().clear();
    
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals("OK")) {
                try {
                    int age = Integer.parseInt(ageInput.getText().trim());
                    if (age > 0) {
                        players.add(new PlayerInfo(i, age));
                        continue;
                    }
                } catch (NumberFormatException ignored) {}
                
                // Show error for invalid age
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Age");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid age.");
                alert.showAndWait();
                i--; // Retry this player
            } else {
                return null; // User cancelled
            }
        }
        
        return players;
    }

    private void loadGame(ActionEvent event, String difficulty, int playerCount) throws Exception {
        List<PlayerInfo> players = promptForPlayerAges(playerCount);
        if (players == null) return;
        
        // Find all players with the youngest age
        int youngestAge = players.stream()
            .mapToInt(p -> p.age)
            .min()
            .orElseThrow();
        
        List<PlayerInfo> youngestPlayers = players.stream()
            .filter(p -> p.age == youngestAge)
            .collect(Collectors.toList());
        
        PlayerInfo startingPlayer;
        if (youngestPlayers.size() > 1) {
            // If there's a tie, show dialog to randomly select among youngest players
            Alert tieAlert = new Alert(Alert.AlertType.INFORMATION);
            tieAlert.setTitle("Age Tie");
            tieAlert.setHeaderText("Multiple players are " + youngestAge + " years old!");
            tieAlert.setContentText("A random selection will be made among the youngest players.");
            tieAlert.showAndWait();
        
            // Randomly select one of the youngest players
            Random random = new Random();
            startingPlayer = youngestPlayers.get(random.nextInt(youngestPlayers.size()));
            
            // Show who was selected
            Alert selectedAlert = new Alert(Alert.AlertType.INFORMATION);
            selectedAlert.setTitle("Starting Player Selected");
            selectedAlert.setHeaderText(null);
            selectedAlert.setContentText("Player " + startingPlayer.playerNumber + " will start the game!");
            selectedAlert.showAndWait();
        } else {
            startingPlayer = youngestPlayers.get(0);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/Game.fxml"));
            Parent gameScreen = loader.load();
            
            GameController controller = loader.getController();
            controller.initializeGame(difficulty, playerCount, startingPlayer.playerNumber - 1);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene currentScene = stage.getScene();
            double width = currentScene.getWidth();
            double height = currentScene.getHeight();
            
            Scene scene = new Scene(gameScreen, width, height);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        
        } catch (Exception e) {
            showErrorAlert("Error Loading Game", "Failed to start the game. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/StartScreen.fxml"));
            Parent startScreen = loader.load();
            
            // Get the current window size
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene currentScene = stage.getScene();
            double width = currentScene.getWidth();
            double height = currentScene.getHeight();
            
            // Create new scene with current dimensions
            Scene scene = new Scene(startScreen, width, height);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
            
        } catch (Exception e) {
            showErrorAlert("Error Loading Start Screen", "Failed to return to start screen. Please try again.");
            e.printStackTrace();
        }
    }

    private void showInvalidPlayerCountAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Player Count");
        alert.setHeaderText(null);
        alert.setContentText("Please enter a number between 1 and 6.");
        alert.showAndWait();
    }
    
    private void showLevelNotAvailableAlert(String level) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(level);
        alert.setHeaderText(null);
        alert.setContentText(level + " is coming soon!");
        alert.showAndWait();
    }
    
    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
