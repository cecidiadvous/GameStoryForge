package com.group10.gamestoryforge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group10.gamestoryforge.dao.CharacterRepository;
import com.group10.gamestoryforge.model.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CharacterServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private GameService gameService;

    @InjectMocks
    private CharacterService characterService;

    @Mock
    private MultipartFile image;

    private Character character;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        character = new Character();
        character.setCharacterId(1L);
        character.setName("Test Character");
        character.setRole("Warrior");
        character.setAbility("Fireball");
        character.setGameId(1L);
    }

    @Test
    void testGetCharactersByGameId() {
        List<Character> characters = new ArrayList<>();
        characters.add(character);
        when(characterRepository.findByGameId(1L)).thenReturn(characters);

        List<Character> result = characterService.getCharactersByGameId(1L);

        assertEquals(1, result.size());
        assertEquals("Test Character", result.get(0).getName());
    }

    @Test
    void testGetAllCharacters() {
        List<Character> characters = new ArrayList<>();
        characters.add(character);
        when(characterRepository.findAll()).thenReturn(characters);

        List<Character> result = characterService.getAllCharacters();

        assertEquals(1, result.size());
        assertEquals("Test Character", result.get(0).getName());
    }

    @Test
    void testGetCharacterById() {
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));

        Optional<Character> result = characterService.getCharacterById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Character", result.get().getName());
    }

    @Test
    void testAddCharacter() throws Exception {
        String characterJson = new ObjectMapper().writeValueAsString(character);
        when(characterRepository.save(any(Character.class))).thenReturn(character);
        when(gameService.saveGameImage(any(MultipartFile.class))).thenReturn("/images/test_character.png");

        Character result = characterService.addCharacter(characterJson, image);

        assertNotNull(result);
        assertEquals("Test Character", result.getName());
        verify(characterRepository, times(1)).save(any(Character.class));
    }

    @Test
    void testUpdateCharacter() throws Exception {
        String updatedName = "Updated Character";
        character.setName(updatedName);
        String characterJson = new ObjectMapper().writeValueAsString(character);

        when(characterRepository.findById(1L)).thenReturn(Optional.of(new Character()));
        when(characterRepository.save(any(Character.class))).thenReturn(character);
        when(gameService.saveGameImage(any(MultipartFile.class))).thenReturn("/images/updated_character.png");

        Character result = characterService.updateCharacter(1L, characterJson, image);

        assertNotNull(result);
        assertEquals(updatedName, result.getName());
        verify(characterRepository, times(1)).save(any(Character.class));
    }

    @Test
    void testDeleteCharacter() {
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));

        characterService.deleteCharacter(1L);

        verify(gameService, times(1)).deleteGameImage(character.getImage());
        verify(characterRepository, times(1)).deleteById(1L);
    }
}

