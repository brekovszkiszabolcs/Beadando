package org.example;

import org.example.database.DatabaseManager;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    @Test
    void testSaveAndGetScores() {
        // Tesztadatok mentése
        String p1 = "Teszt_Elek_" + System.currentTimeMillis();
        String p2 = "Bot_Ellenfel";
        int s1 = 5;
        int s2 = 2;

        // Mentés az adatbázisba
        DatabaseManager.saveMatchup(p1, s1, p2, s2);

        // Visszakeresés: ellenőrizzük, hogy az adatbázis visszaadja-e a mentett pontokat
        int[] scores = DatabaseManager.getMatchupScores(p1, p2);

        assertNotNull(scores, "Az eredmény nem lehet null.");
        assertEquals(s1, scores[0], "A mentett pontszámnak (P1) egyeznie kell.");
        assertEquals(s2, scores[1], "A mentett pontszámnak (P2) egyeznie kell.");
    }

    @Test
    void testScoreEntryInnerClass() {
        // Sokszor a DatabaseManager-ben van egy ScoreEntry belső osztály a ranglistához.
        // Ezt is le kell tesztelni a lefedettséghez.
        DatabaseManager.ScoreEntry entry = new DatabaseManager.ScoreEntry("Bajnok", 100);

        assertEquals("Bajnok", entry.getPlayerName());
        assertEquals(100, entry.getScore());
    }

    @Test
    void testGetLeaderboard() {
        // Ellenőrizzük, hogy a ranglista lekérdezés nem dob hibát és ad vissza listát
        List<DatabaseManager.ScoreEntry> list = DatabaseManager.getBotLeaderboard();

        assertNotNull(list, "A ranglistának nem szabad nullnak lennie, még ha üres is.");
    }
}