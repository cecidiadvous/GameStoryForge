package com.group10.gamestoryforge.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChapterTest {

    private Chapter chapter;
    private Game game;
    private Character character1;
    private Character character2;

    @BeforeEach
    void setUp() {
        chapter = new Chapter();
        chapter.setName("Test Chapter");
        chapter.setDescription("Test Description");
        chapter.setUserText("User generated text");
        chapter.setSystemText("System generated text");
        chapter.setCreatedAt(LocalDateTime.now());
        chapter.setUpdatedAt(LocalDateTime.now());

        game = new Game();
        game.setName("Test Game");
        chapter.setGame(game);

        character1 = new Character();
        character1.setName("Character 1");

        character2 = new Character();
        character2.setName("Character 2");
    }

    @Test
    void testChapterCreation() {
        assertNotNull(chapter);
        assertEquals("Test Chapter", chapter.getName());
        assertEquals("Test Description", chapter.getDescription());
        assertEquals("User generated text", chapter.getUserText());
        assertEquals("System generated text", chapter.getSystemText());
        assertNotNull(chapter.getCreatedAt());
    }

    @Test
    void testSetGame() {
        assertEquals(game, chapter.getGame());
    }

    @Test
    void testAddCharacters() {
        Set<Character> characters = new HashSet<>();
        characters.add(character1);
        characters.add(character2);
        chapter.setCharacters(characters);

        assertEquals(2, chapter.getCharacters().size());
        assertTrue(chapter.getCharacters().contains(character1));
        assertTrue(chapter.getCharacters().contains(character2));
    }

    @Test
    void testUpdateChapter() {
        chapter.setName("Updated Chapter Name");
        chapter.setDescription("Updated Description");
        chapter.setUpdatedAt(LocalDateTime.now());

        assertEquals("Updated Chapter Name", chapter.getName());
        assertEquals("Updated Description", chapter.getDescription());
        assertNotNull(chapter.getUpdatedAt());
    }
}

