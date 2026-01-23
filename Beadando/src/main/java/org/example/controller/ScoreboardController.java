package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.database.DatabaseManager;
import java.io.IOException;

public class ScoreboardController {
    @FXML private TableView<DatabaseManager.ScoreEntry> scoreTable;
    @FXML private TableColumn<DatabaseManager.ScoreEntry, String> nameColumn;
    @FXML private TableColumn<DatabaseManager.ScoreEntry, Integer> scoreColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        scoreTable.setItems(FXCollections.observableArrayList(DatabaseManager.getBotLeaderboard()));
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 300, 400));
    }
}