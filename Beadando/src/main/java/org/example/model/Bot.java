package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bot {
    private final int botId;     // Általában 2 (O)
    private final int playerId;  // Általában 1 (X)

    public Bot(int botId, int playerId) {
        this.botId = botId;
        this.playerId = playerId;
    }

    /**
     * Meghatározza a bot következő lépését.
     * @return egy int tömb {sor, oszlop}
     */
    public int[] calculateMove(Amoba jatek) {
        // 1. Támadás: Megpróbálunk nyerni
        int[] move = findWinningMove(jatek, botId);
        if (move != null) return move;

        // 2. Védekezés: Blokkoljuk a játékost
        move = findWinningMove(jatek, playerId);
        if (move != null) return move;

        // 3. Stratégia: Középső mező elfoglalása
        if (jatek.getElem(1, 1) == 0) {
            return new int[]{1, 1};
        }

        // 4. Véletlenszerű lépés
        return getRandomMove(jatek);
    }

    private int[] findWinningMove(Amoba jatek, int id) {
        // Sorok, oszlopok és átlók ellenőrzése
        for (int i = 0; i < 3; i++) {
            // Sorok
            int[] res = checkLine(jatek, i, 0, i, 1, i, 2, id);
            if (res != null) return res;
            // Oszlopok
            res = checkLine(jatek, 0, i, 1, i, 2, i, id);
            if (res != null) return res;
        }
        // Átlók
        int[] res = checkLine(jatek, 0, 0, 1, 1, 2, 2, id);
        if (res != null) return res;
        res = checkLine(jatek, 0, 2, 1, 1, 2, 0, id);
        return res;
    }

    private int[] checkLine(Amoba jatek, int s1, int o1, int s2, int o2, int s3, int o3, int id) {
        int v1 = jatek.getElem(s1, o1);
        int v2 = jatek.getElem(s2, o2);
        int v3 = jatek.getElem(s3, o3);

        if (v1 == id && v2 == id && v3 == 0) return new int[]{s3, o3};
        if (v1 == id && v3 == id && v2 == 0) return new int[]{s2, o2};
        if (v2 == id && v3 == id && v1 == 0) return new int[]{s1, o1};
        return null;
    }

    private int[] getRandomMove(Amoba jatek) {
        List<int[]> emptyCells = new ArrayList<>();
        for (int s = 0; s < 3; s++) {
            for (int o = 0; o < 3; o++) {
                if (jatek.getElem(s, o) == 0) {
                    emptyCells.add(new int[]{s, o});
                }
            }
        }
        if (emptyCells.isEmpty()) return null;
        return emptyCells.get(new Random().nextInt(emptyCells.size()));
    }
}
