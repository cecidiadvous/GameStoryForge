package com.group10.gamestoryforge.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    private Character character;
    private Chapter chapter1;
    private Chapter chapter2;

    @BeforeEach
    void setUp() {
        character = new Character();
        character.setName("Test Character");
        character.setRole("Warrior");
        character.setBackground("A brave warrior from the east");
        character.setAbility("Sword Mastery");
        character.setGameId(1L);
        character.setImage("/images/test_character.png");

        chapter1 = new Chapter();
        chapter1.setName("Chapter 1");

        chapter2 = new Chapter();
        chapter2.setName("Chapter 2");
    }

    @Test
    void testCharacterCreation() {
        assertNotNull(character);
        assertEquals("Test Character", character.getName());
        assertEquals("Warrior", character.getRole());
        assertEquals("A brave warrior from the east", character.getBackground());
        assertEquals("Sword Mastery", character.getAbility());
        assertEquals(1L, character.getGameId());
        assertEquals("/images/test_character.png", character.getImage());
    }

    @Test
    void testSetChapters() {
        Set<Chapter> chapters = new HashSet<>();
        chapters.add(chapter1);
        chapters.add(chapter2);
        character.setChapters(chapters);

        assertEquals(2, character.getChapters().size());
        assertTrue(character.getChapters().contains(chapter1));
        assertTrue(character.getChapters().contains(chapter2));
    }

    @Test
    void testDeleteImage() throws Exception {
        // Create a temporary image file to simulate the delete
        Path tempImagePath = Paths.get(System.getProperty("user.dir") + "/images/test_character.png");
        Files.createDirectories(tempImagePath.getParent());
        Files.createFile(tempImagePath);
        
        character.deleteImage();
        
        assertFalse(Files.exists(tempImagePath));
    }

    @Test
    void testToString() {
        String expectedString = "Character{, name='Test Character', role='Warrior', background='A brave warrior from the east', ability='Sword Mastery'}";
        assertEquals(expectedString, character.toString());
    }
}
