package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;

public class DifficultyController {
    
    @FXML
    private void handleEasy(ActionEvent event) throws Exception {
        // For Level 1, prompt for player count
        int playerCount = promptForPlayerCount();
        if (playerCount > 0) {
            loadGame(event, "LEVEL 1", playerCount);
        }
    }
    
    @FXML
    private void handleMedium(ActionEvent event) throws Exception {
        // For Level 2,  prompt for player count
        int playerCount = promptForPlayerCount();
        if (playerCount > 0) {
            loadGame(event, "LEVEL 2", playerCount);
        }
    }
    
    @FXML
    private void handleHard(ActionEvent event) throws Exception {
        // Show alert that Level 3 is not yet implemented
        showLevelNotAvailableAlert("Level 3");
    }
    
    private int promptForPlayerCount() {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Number of Players");
        dialog.setHeaderText("Enter the number of players (1-6)");
        dialog.setContentText("Players:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int count = Integer.parseInt(result.get().trim());
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
    
    private void loadGame(ActionEvent event, String difficulty, int playerCount) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/Game.fxml"));
            Parent gameScreen = loader.load();
            
            GameController controller = loader.getController();
            controller.initializeGame(difficulty, playerCount);
            
            // Get the current window size
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene currentScene = stage.getScene();
            double width = currentScene.getWidth();
            double height = currentScene.getHeight();
            
            // Create new scene with current dimensions
            Scene scene = new Scene(gameScreen, width, height);
            stage.setScene(scene);
            
            // Ensure maximized state is maintained
            stage.setMaximized(true);
            stage.show();
            
        } catch (Exception e) {
            showErrorAlert("Error Loading Game", "Failed to start the game. Please try again.");
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