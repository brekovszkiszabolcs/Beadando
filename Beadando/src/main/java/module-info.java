module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    // Engedélyezzük a JavaFX-nek és a Tesztelésnek, hogy belássanak
    // Ha ezeket így hagyod, minden alatta lévő osztály elérhető lesz
    opens org.example to javafx.fxml, javafx.graphics, javafx.base, org.junit.platform.commons;

    // Ha külön mappáid (package-id) vannak, azokat így sorold fel:
    opens org.example.controller to javafx.fxml, org.junit.platform.commons;
    opens org.example.model to org.junit.platform.commons;
    opens org.example.database to javafx.base, org.junit.platform.commons;

    exports org.example;
    exports org.example.model;
    exports org.example.controller;
}