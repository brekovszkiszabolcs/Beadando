package org.example;

import org.example.model.Amoba;
import org.example.model.Bot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BotTest {
    private Amoba jatek;
    private Bot bot;

    @BeforeEach
    void setUp() {
        // Alapértelmezett (3x3) játék és egy bot példányosítása
        // A Bot(2, 1) azt jelenti: a gép az 'O' (2), a játékos az 'X' (1)
        jatek = new Amoba();
        bot = new Bot(2, 1);
    }

    @Test
    void testBotValasztMezot() {
        // Ellenőrizzük, hogy a bot egyáltalán tud-e lépni egy üres táblán
        int[] lepes = bot.calculateMove(jatek);

        assertNotNull(lepes, "A botnak vissza kell adnia egy koordinátát.");
        assertEquals(2, lepes.length, "A lepesnek két koordinátából (sor, oszlop) kell állnia.");
    }

    @Test
    void testBotErvenyesHelyreLep() {
        int[] lepes = bot.calculateMove(jatek);
        int sor = lepes[0];
        int oszlop = lepes[1];

        // Ellenőrizzük, hogy a táblán belül maradt-e (3x3-as tábla esetén)
        assertTrue(sor >= 0 && sor < 3, "A sor indexnek 0 és 2 között kell lennie.");
        assertTrue(oszlop >= 0 && oszlop < 3, "Az oszlop indexnek 0 és 2 között kell lennie.");

        // Ellenőrizzük, hogy az adott mező üres volt-e
        assertEquals(0, jatek.getElem(sor, oszlop), "A bot csak üres mezőre léphet.");
    }

    @Test
    void testBotNemLepFoglaltMezore() {
        // Szimuláljuk, hogy majdnem tele van a tábla
        // Csak egyetlen helyet hagyunk szabadon: (2, 2)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!(i == 2 && j == 2)) {
                    jatek.setElem(i, j, 1);
                }
            }
        }

        int[] lepes = bot.calculateMove(jatek);
        assertEquals(2, lepes[0], "A botnak a maradék szabad sorba kell lépnie.");
        assertEquals(2, lepes[1], "A botnak a maradék szabad oszlopba kell lépnie.");
    }
}