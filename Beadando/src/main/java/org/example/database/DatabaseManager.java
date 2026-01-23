package org.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:amoba_stats.db";

    static {
        // Tábla létrehozása, ha még nem létezik
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // Matchups tábla: p1_name és p2_name együtt az elsődleges kulcs
            String sql = "CREATE TABLE IF NOT EXISTS matchups (" +
                    "p1_name TEXT, " +
                    "p2_name TEXT, " +
                    "p1_score INTEGER DEFAULT 0, " +
                    "p2_score INTEGER DEFAULT 0, " +
                    "PRIMARY KEY (p1_name, p2_name))";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Adatbázis hiba az inicializáláskor: " + e.getMessage());
        }
    }

    /**
     * Lekéri két játékos egymás elleni aktuális állását.
     * A neveket ABC sorrendbe rendezi, hogy a Szabi-Laci és Laci-Szabi ugyanaz legyen.
     */
    public static int[] getMatchupScores(String n1, String n2) {
        String first = n1.compareTo(n2) < 0 ? n1 : n2;
        String second = first.equals(n1) ? n2 : n1;

        String sql = "SELECT p1_score, p2_score FROM matchups WHERE p1_name = ? AND p2_name = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, first);
            pstmt.setString(2, second);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Vissza kell fordítanunk az eredeti sorrendbe az eredményt
                if (n1.equals(first)) {
                    return new int[]{rs.getInt("p1_score"), rs.getInt("p2_score")};
                } else {
                    return new int[]{rs.getInt("p2_score"), rs.getInt("p1_score")};
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new int[]{0, 0}; // Ha még sosem játszottak
    }

    /**
     * Elmenti vagy frissíti a két játékos közötti eredményt.
     */
    public static void saveMatchup(String n1, int s1, String n2, int s2) {
        String first = n1.compareTo(n2) < 0 ? n1 : n2;
        String second = first.equals(n1) ? n2 : n1;
        int scoreForFirst = first.equals(n1) ? s1 : s2;
        int scoreForSecond = second.equals(n2) ? s2 : s1;

        String sql = "INSERT INTO matchups(p1_name, p2_name, p1_score, p2_score) VALUES(?,?,?,?) " +
                "ON CONFLICT(p1_name, p2_name) DO UPDATE SET p1_score = ?, p2_score = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, first);
            pstmt.setString(2, second);
            pstmt.setInt(3, scoreForFirst);
            pstmt.setInt(4, scoreForSecond);
            pstmt.setInt(5, scoreForFirst);
            pstmt.setInt(6, scoreForSecond);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lekérdezi a Top 10 játékost, aki a legtöbbször győzte le a Botot.
     */
    public static List<ScoreEntry> getBotLeaderboard() {
        List<ScoreEntry> scores = new ArrayList<>();
        // Lekérdezzük azokat a sorokat, ahol az egyik fél a "Gép (Bot)"
        String sql = "SELECT " +
                "  CASE WHEN p1_name = 'Gép (Bot)' THEN p2_name ELSE p1_name END AS playerName, " +
                "  CASE WHEN p1_name = 'Gép (Bot)' THEN p2_score ELSE p1_score END AS score " +
                "FROM matchups " +
                "WHERE p1_name = 'Gép (Bot)' OR p2_name = 'Gép (Bot)' " +
                "ORDER BY score DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                scores.add(new ScoreEntry(rs.getString("playerName"), rs.getInt("score")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }

    /**
     * Segédosztály a ranglista rekordokhoz (Scoreboardhoz kell)
     */
    public static class ScoreEntry {
        private final String playerName;
        private final int score;

        public ScoreEntry(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
        }

        public String getPlayerName() { return playerName; }
        public int getScore() { return score; }
    }
}