package org.example.controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.model.*;
import org.example.database.DatabaseManager;
import java.io.IOException;

public class Amoba10Controller {
    private int[][] tabla = new int[10][10];
    private int aktualisJatekos = 1;
    private int korSzamlalo = 1;
    private User player1, player2;
    private boolean isSinglePlayer = false;

    @FXML private GridPane gridPane;
    @FXML private Label p1DisplayLabel, p2DisplayLabel, p1ScoreLabel, p2ScoreLabel, korLabel, turnLabel;

    @FXML
    public void initialize() {
        generalPalyat();
    }

    private void generalPalyat() {
        gridPane.getChildren().clear();
        for (int s = 0; s < 10; s++) {
            for (int o = 0; o < 10; o++) {
                Button b = new Button("");
                b.setPrefSize(40, 40);
                b.setMinSize(40, 40);
                b.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
                b.setOnAction(this::handleButtonClick);
                gridPane.add(b, o, s);
            }
        }
    }

    public void setPlayers(String n1, String n2, int s1, int s2, boolean single) {
        this.isSinglePlayer = single;
        player1 = new User(n1); player1.setScore(s1);
        player2 = new User(n2); player2.setScore(s2);
        p1DisplayLabel.setText(n1 + ": X"); p2DisplayLabel.setText(n2 + ": O");
        updateUI();
    }

    private void handleButtonClick(ActionEvent event) {
        if (gridPane.isDisable()) return;
        Button b = (Button) event.getSource();
        if (vegrehajtLepes(b)) {
            if (isSinglePlayer && aktualisJatekos == 2) botLepes();
        }
    }

    private boolean vegrehajtLepes(Button gomb) {
        Integer s = GridPane.getRowIndex(gomb), o = GridPane.getColumnIndex(gomb);
        if (s == null) s = 0; if (o == null) o = 0;

        if (tabla[s][o] == 0) {
            tabla[s][o] = aktualisJatekos;
            gomb.setText(aktualisJatekos == 1 ? "X" : "O");
            gomb.setTextFill(aktualisJatekos == 1 ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.BLUE);

            if (ellenorizGyozelem(s, o)) {
                if (aktualisJatekos == 1) player1.addScore(1); else player2.addScore(1);
                DatabaseManager.saveMatchup(player1.getName(), player1.getScore(), player2.getName(), player2.getScore());
                updateUI();
                Platform.runLater(() -> {
                    showMessage("Győzelem!", "Nyert: " + (aktualisJatekos == 1 ? player1.getName() : player2.getName()));
                    ujKor();
                });
                return false;
            }
            aktualisJatekos = (aktualisJatekos == 1) ? 2 : 1;
            updateUI(); return true;
        }
        return false;
    }

    private boolean ellenorizGyozelem(int s, int o) {
        int id = tabla[s][o];
        int[][] iranyok = {{0,1}, {1,0}, {1,1}, {1,-1}};
        for (int[] irany : iranyok) {
            int db = 1;
            db += szamol(s, o, irany[0], irany[1], id);
            db += szamol(s, o, -irany[0], -irany[1], id);
            if (db >= 4) return true; // Itt a 4-es nyerő feltétel
        }
        return false;
    }

    private int szamol(int s, int o, int ds, int do1, int id) {
        int db = 0;
        int r = s + ds, c = o + do1;
        while (r >= 0 && r < 10 && c >= 0 && c < 10 && tabla[r][c] == id) {
            db++; r += ds; c += do1;
            if (db == 4) break;
        }
        return db;
    }

    private void botLepes() {
        gridPane.setDisable(true);
        PauseTransition p = new PauseTransition(Duration.seconds(0.6));
        p.setOnFinished(e -> {
            // Egyszerű bot: keres egy üres helyet (véletlenszerűen a közép közelében)
            for (int k = 0; k < 100; k++) {
                int rs = (int)(Math.random() * 10), ro = (int)(Math.random() * 10);
                if (tabla[rs][ro] == 0) {
                    vegrehajtLepes(getBtn(rs, ro));
                    break;
                }
            }
            gridPane.setDisable(false);
        });
        p.play();
    }

    private void ujKor() {
        tabla = new int[10][10];
        korSzamlalo++; korLabel.setText(String.valueOf(korSzamlalo));
        aktualisJatekos = (korSzamlalo % 2 != 0) ? 1 : 2;
        for (Node n : gridPane.getChildren()) if (n instanceof Button b) b.setText("");
        updateUI();
        if (isSinglePlayer && aktualisJatekos == 2) botLepes();
    }

    private void updateUI() {
        if (player1 == null) return;
        p1ScoreLabel.setText("Győzelmek: " + player1.getScore());
        p2ScoreLabel.setText("Győzelmek: " + player2.getScore());
        turnLabel.setText(((aktualisJatekos == 1) ? player1.getName() : player2.getName()) + " jön");
    }

    private Button getBtn(int s, int o) {
        for (Node n : gridPane.getChildren()) {
            if (n instanceof Button b && GridPane.getRowIndex(b) == s && GridPane.getColumnIndex(b) == o) return b;
        }
        return null;
    }

    @FXML private void handleBack(ActionEvent e) throws IOException {
        Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Main.fxml")), 300, 400));
    }

    private void showMessage(String c, String t) {
        Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(c); a.setHeaderText(null); a.setContentText(t); a.showAndWait();
    }
}