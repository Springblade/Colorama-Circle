package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;

public class StartScreenController {
    @FXML
    private void handlePlayButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/Difficulty.fxml"));
            Parent difficultyScreen = loader.load();
            
            if (difficultyScreen == null) {
                throw new RuntimeException("Failed to load Difficulty.fxml");
            }
    
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene currentScene = stage.getScene();
            double width = currentScene.getWidth();
            double height = currentScene.getHeight();
    
            Scene scene = new Scene(difficultyScreen, width, height);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            showErrorAlert("Error", "Failed to load difficulty screen", e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    @FXML
    private void handleQuit() {
        try {
            System.exit(0);
        } catch (Exception e) {
            showErrorAlert("Error", "Failed to quit application", e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleHowToPlay(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/HowToPlay.fxml"));
            Parent howToPlayScreen = loader.load();
        
            // Get stage correctly for both Buttons and MenuItems
            Stage stage;
            if (event.getSource() instanceof MenuItem) {
                stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            } else {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            }
            
            Scene currentScene = stage.getScene();
            Scene scene = new Scene(howToPlayScreen, currentScene.getWidth(), currentScene.getHeight());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Error", "Failed to load How To Play screen", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
