package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image; // Fontos import a képkezeléshez
import javafx.stage.Stage;

import java.util.Objects;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // FXML betöltése
        Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));

        // --- IKON BEÁLLÍTÁSA ---
        // Megpróbáljuk betölteni az icon.png-t a resources mappából
        try {
            // A getClass().getResourceAsStream() a legbiztosabb módja a fájl elérésének
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.jpg")));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            // Ha nincs meg a kép, a program ne szálljon el, csak írja ki a hibát
            System.err.println("Figyelem: Az ikon fájl (icon.png) nem található a resources mappában!");
        }
        // -----------------------

        primaryStage.setTitle("Amoba");

        // Mivel a főmenübe több gombot is raktunk (1v1, Single, Ranglista),
        // a 300x300 helyett javaslom a 350x450-es méretet, hogy kényelmesen elférjenek.
        primaryStage.setScene(new Scene(root, 350, 450));

        // Megakadályozzuk, hogy az ablakot tetszőlegesen átméretezzék (opcionális)
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}