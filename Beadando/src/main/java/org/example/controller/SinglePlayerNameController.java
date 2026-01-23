package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.database.DatabaseManager;

import java.io.IOException;

public class SinglePlayerNameController {

    @FXML private TextField p1NameField;
    @FXML private ComboBox<String> sizeSelector;

    @FXML
    public void initialize() {
        // Választási lehetőségek hozzáadása
        sizeSelector.getItems().addAll("3x3 (Klasszikus)", "10x10 (4-es nyerő)");
        sizeSelector.setValue("3x3 (Klasszikus)"); // Alapértelmezett érték
    }

    @FXML
    private void handleStart(ActionEvent event) throws IOException {
        String name1 = p1NameField.getText().isEmpty() ? "Játékos 1" : p1NameField.getText();
        String name2 = "Gép (Bot)";

        // Aktuális állás lekérése az adatbázisból
        int[] scores = DatabaseManager.getMatchupScores(name1, name2);
        boolean is10x10 = sizeSelector.getValue().contains("10x10");

        String fxmlPath = is10x10 ? "/Amoba10.fxml" : "/Amoba.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        // Adatok átadása a megfelelő kontrollernek
        if (is10x10) {
            Amoba10Controller controller = loader.getController();
            controller.setPlayers(name1, name2, scores[0], scores[1], true);
        } else {
            AmobaController controller = loader.getController();
            controller.setSinglePlayer(true);
            controller.setPlayers(name1, name2, scores[0], scores[1]);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 300, 400));
    }
}