package org.example;

import org.example.model.Amoba;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AmobaTest {
    private Amoba jatek;

    @BeforeEach
    void setUp() {
        // Itt most már nincs paraméter, így nem lesz hiba
        jatek = new Amoba();
    }

    @Test
    void testTablaUresKezdeskor() {
        // Ellenőrizzük, hogy minden mező 0 (üres)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(0, jatek.getElem(i, j), "A táblának üresnek kell lennie!");
            }
        }
    }

    @Test
    void testVizszintesGyozelem() {
        jatek.setElem(0, 0, 1);
        jatek.setElem(0, 1, 1);
        jatek.setElem(0, 2, 1);
        assertEquals(1, jatek.win(), "Az 1-es játékosnak nyernie kellene a sorban.");
    }

    @Test
    void testFuggolegesGyozelem() {
        jatek.setElem(0, 1, 2);
        jatek.setElem(1, 1, 2);
        jatek.setElem(2, 1, 2);
        assertEquals(2, jatek.win(), "A 2-es játékosnak nyernie kellene az oszlopban.");
    }

    @Test
    void testAtlosGyozelem() {
        jatek.setElem(0, 0, 1);
        jatek.setElem(1, 1, 1);
        jatek.setElem(2, 2, 1);
        assertEquals(1, jatek.win(), "Az átlós sorozatnak is győzelmet kell érnie.");
    }

    @Test
    void testDontetlen() {
        // Tábla feltöltése döntetlen állásra
        int[][] tabla = {
                {1, 2, 1},
                {1, 1, 2},
                {2, 1, 2}
        };
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                jatek.setElem(i, j, tabla[i][j]);
            }
        }
        assertEquals(0, jatek.win(), "Itt senki sem nyerhet.");
        assertEquals(1, jatek.dontetlen(), "A döntetlen metódusnak 1-et kell visszaadnia.");
    }
}