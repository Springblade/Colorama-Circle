package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class HowToPlayController {
    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/StartScreen.fxml"));
            Parent startScreen = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            boolean wasMaximized = stage.isMaximized(); // Check current state

            // Create scene with current dimensions
            Scene scene = new Scene(startScreen, stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
            stage.setMaximized(wasMaximized);
            stage.show();

        } catch (Exception e) {
            showErrorAlert("Error", "Failed to load start screen", e.getMessage());
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