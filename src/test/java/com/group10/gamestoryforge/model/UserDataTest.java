package com.group10.gamestoryforge.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDataTest {

    private UserData user;
    private Game game1;
    private Game game2;

    @BeforeEach
    void setUp() {
        user = new UserData();
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setRole("USER");

        game1 = new Game();
        game1.setName("Game 1");
        game1.setDescription("First game description");
        game1.setUser(user);

        game2 = new Game();
        game2.setName("Game 2");
        game2.setDescription("Second game description");
        game2.setUser(user);

        List<Game> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);
        user.setGames(games);
    }

    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("USER", user.getRole());
    }

    @Test
    void testSetGames() {
        assertNotNull(user.getGames());
        assertEquals(2, user.getGames().size());
        assertTrue(user.getGames().contains(game1));
        assertTrue(user.getGames().contains(game2));
    }

    @Test
    void testAddGame() {
        Game game3 = new Game();
        game3.setName("Game 3");
        game3.setDescription("Third game description");
        game3.setUser(user);

        user.getGames().add(game3);
        assertEquals(3, user.getGames().size());
        assertTrue(user.getGames().contains(game3));
    }

    @Test
    void testRemoveGame() {
        user.getGames().remove(game1);
        assertEquals(1, user.getGames().size());
        assertFalse(user.getGames().contains(game1));
    }
}

