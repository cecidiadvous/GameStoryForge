package com.group10.gamestoryforge.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private UserData user;
    private Chapter chapter1;
    private Chapter chapter2;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.setName("Test Game");
        game.setDescription("This is a test game.");
        game.setCreatedAt(LocalDateTime.now());
        game.setUpdatedAt(LocalDateTime.now());
        game.setImage("/images/test_game.png");

        user = new UserData();
        user.setUsername("testUser");
        game.setUser(user);

        chapter1 = new Chapter();
        chapter1.setName("Chapter 1");
        chapter1.setGame(game);

        chapter2 = new Chapter();
        chapter2.setName("Chapter 2");
        chapter2.setGame(game);

        Set<Chapter> chapters = new HashSet<>();
        chapters.add(chapter1);
        chapters.add(chapter2);
    }

    @Test
    void testGameCreation() {
        assertNotNull(game);
        assertEquals("Test Game", game.getName());
        assertEquals("This is a test game.", game.getDescription());
        assertEquals("/images/test_game.png", game.getImage());
        assertNotNull(game.getCreatedAt());
        assertNotNull(game.getUpdatedAt());
    }

    @Test
    void testSetUser() {
        assertEquals(user, game.getUser());
    }

    @Test
    void testToString() {
        String expectedString = "Game{" +
                "gameId=" + game.getGameId() +
                ", name='Test Game'" +
                ", description='This is a test game.'" +
                ", user=" + user +
                ", createdAt=" + game.getCreatedAt() +
                ", updatedAt=" + game.getUpdatedAt() +
                '}';
        assertEquals(expectedString, game.toString());
    }
}

