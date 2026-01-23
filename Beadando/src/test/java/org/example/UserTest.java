package org.example;

import org.example.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserCreationAndGetters() {
        // Ellenőrizzük, hogy a név és a kezdőpontszám helyes-e
        User user = new User("TesztElek");

        assertEquals("TesztElek", user.getName(), "A névnek egyeznie kell a megadottal.");
        // Feltételezzük, hogy az alapértelmezett pontszám 0 vagy az adatbázisból jön
        assertTrue(user.getScore() >= 0, "A pontszám nem lehet negatív.");
    }

    @Test
    void testSetAndGetScore() {
        User user = new User("Játékos1");

        user.setScore(10);
        assertEquals(10, user.getScore(), "A beállított pontszámot kell visszakapnunk.");
    }

    @Test
    void testAddScore() {
        User user = new User("ProPlayer");
        user.setScore(5);

        // Növeljük a pontszámot 1-gyel (vagy amennyivel a metódusod engedi)
        user.addScore(1);

        assertEquals(6, user.getScore(), "A pontszámnak 1-gyel növekednie kell.");
    }

    @Test
    void testNameChange() {
        User user = new User("RégiNév");
        user.setName("ÚjNév");

        assertEquals("ÚjNév", user.getName(), "A név módosításának sikeresnek kell lennie.");
    }
}