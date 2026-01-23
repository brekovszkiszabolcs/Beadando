package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {

    /**
     * Megnyitja a 1v1 névbeíró ablakot.
     */
    @FXML
    private void handleMultiplayer(ActionEvent event) throws IOException {
        loadScene(event, "/PlayerNames.fxml", "1 vs 1 Játék - Nevek");
    }

    /**
     * Megnyitja az egyszemélyes (Bot elleni) névbeíró ablakot.
     */
    @FXML
    private void handleSinglePlayer(ActionEvent event) throws IOException {
        loadScene(event, "/SinglePlayerName.fxml", "Gép ellen - Név megadása");
    }

    /**
     * Megnyitja a Ranglista (Bot elleni eredmények) ablakot.
     */
    @FXML
    private void handleScoreboard(ActionEvent event) throws IOException {
        loadScene(event, "/Scoreboard.fxml", "Bot-gyilkosok Ranglistája");
    }

    /**
     * Kilép a programból.
     */
    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Általános jelenetváltó metódus.
     */
    private void loadScene(ActionEvent event, String fxmlPath, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        // A menüablakoknak fix 350x450-es méretet állítunk be a rendezettségért
        stage.setScene(new Scene(root, 350, 450));
        stage.show();
    }
}