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

public class AmobaController {
    private Amoba jatek = new Amoba();
    private Bot bot = new Bot(2, 1);
    private int aktualisJatekos = 1;
    private int korSzamlalo = 1;
    private User player1, player2;
    private boolean isSinglePlayer = false;

    @FXML private GridPane gridPane;
    @FXML private Label p1DisplayLabel, p2DisplayLabel, p1ScoreLabel, p2ScoreLabel, korLabel, turnLabel;

    public void setSinglePlayer(boolean v) { this.isSinglePlayer = v; }

    public void setPlayers(String n1, String n2, int s1, int s2) {
        player1 = new User(n1); player1.setScore(s1);
        player2 = new User(n2); player2.setScore(s2);
        p1DisplayLabel.setText(n1 + ": X"); p2DisplayLabel.setText(n2 + ": O");
        updateUI();
    }

    @FXML
    private void handleButtonClick(ActionEvent event) {
        if (!gridPane.isDisable()) {
            if (vegrehajtLepes((Button) event.getSource())) {
                if (isSinglePlayer && aktualisJatekos == 2) {
                    botLepes();
                }
            }
        }
    }

    private boolean vegrehajtLepes(Button gomb) {
        if (gomb == null) return false;

        Integer s = GridPane.getRowIndex(gomb), o = GridPane.getColumnIndex(gomb);
        int row = (s == null) ? 0 : s;
        int col = (o == null) ? 0 : o;

        if (jatek.getElem(row, col) == 0) {
            jatek.setElem(row, col, aktualisJatekos);
            gomb.setText(aktualisJatekos == 1 ? "X" : "O");

            int win = jatek.win();
            if (win != 0) {
                if (win == 1) player1.addScore(1); else player2.addScore(1);
                DatabaseManager.saveMatchup(player1.getName(), player1.getScore(), player2.getName(), player2.getScore());
                updateUI();

                // JAVÍTÁS: Platform.runLater használata az animációs hiba elkerülésére
                final int gyoztesID = win;
                Platform.runLater(() -> {
                    showMessage("Győzelem!", "Nyert: " + (gyoztesID == 1 ? player1.getName() : player2.getName()));
                    ujKor();
                });
                return false;
            } else if (jatek.dontetlen() == 1) {
                updateUI();
                Platform.runLater(() -> {
                    showMessage("Döntetlen!", "Vége a körnek.");
                    ujKor();
                });
                return false;
            }

            aktualisJatekos = (aktualisJatekos == 1) ? 2 : 1;
            updateUI();
            return true;
        }
        return false;
    }

    private void botLepes() {
        gridPane.setDisable(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
        pause.setOnFinished(e -> {
            // A try-catch és a null check megvédi a programot az összeomlástól
            try {
                int[] move = bot.calculateMove(jatek);
                if (move != null) {
                    Button celGomb = getBtn(move[0], move[1]);
                    if (celGomb != null) {
                        vegrehajtLepes(celGomb);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                // Csak akkor oldjuk fel, ha még tart a játék
                if (jatek.win() == 0 && jatek.dontetlen() == 0) {
                    gridPane.setDisable(false);
                }
            }
        });
        pause.play();
    }

    private void ujKor() {
        korSzamlalo++;
        korLabel.setText(String.valueOf(korSzamlalo));
        aktualisJatekos = (korSzamlalo % 2 != 0) ? 1 : 2;

        jatek = new Amoba();
        for (Node n : gridPane.getChildren()) {
            if (n instanceof Button b) b.setText("");
        }

        gridPane.setDisable(false);
        updateUI();

        if (isSinglePlayer && aktualisJatekos == 2) {
            // Új kör indításakor is várunk egy kicsit, hogy ne ütközzön az ablak bezárásával
            PauseTransition startDelay = new PauseTransition(Duration.seconds(0.5));
            startDelay.setOnFinished(ev -> botLepes());
            startDelay.play();
        }
    }

    private void updateUI() {
        if (player1 == null || player2 == null) return;
        p1ScoreLabel.setText("Győzelmek: " + player1.getScore());
        p2ScoreLabel.setText("Győzelmek: " + player2.getScore());
        String n = (aktualisJatekos == 1) ? player1.getName() : player2.getName();
        turnLabel.setText(n + " (" + (aktualisJatekos == 1 ? "X" : "O") + ") jön");
    }

    private Button getBtn(int s, int o) {
        for (Node n : gridPane.getChildren()) {
            if (n instanceof Button b) {
                Integer r = GridPane.getRowIndex(b), c = GridPane.getColumnIndex(b);
                int row = (r == null) ? 0 : r;
                int col = (c == null) ? 0 : c;
                if (row == s && col == o) return b;
            }
        }
        return null;
    }

    @FXML private void handleBack(ActionEvent e) throws IOException {
        Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
        s.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Main.fxml")), 300, 350));
    }

    private void showMessage(String c, String t) {
        // Most már biztonságosan hívható, mert a Platform.runLater miatt
        // kikerült az animációs szálból.
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(c);
        a.setHeaderText(null);
        a.setContentText(t);
        a.showAndWait();
    }
}