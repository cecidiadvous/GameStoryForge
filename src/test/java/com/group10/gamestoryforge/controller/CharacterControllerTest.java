package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.service.CharacterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CharacterControllerTest {

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private CharacterController characterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCharactersByGameId() {
        Long gameId = 1L;
        List<Character> characters = List.of(new Character(), new Character());
        when(characterService.getCharactersByGameId(eq(gameId))).thenReturn(characters);

        ResponseEntity<List<Character>> response = characterController.getCharactersByGameId(gameId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testAddCharacter() throws Exception {
        String characterJson = "{\"name\":\"testCharacter\"}";
        MockMultipartFile image = new MockMultipartFile("image", "image.png", MediaType.IMAGE_PNG_VALUE, "imageContent".getBytes());
        Character newCharacter = new Character();
        when(characterService.addCharacter(eq(characterJson), any(MultipartFile.class))).thenReturn(newCharacter);

        ResponseEntity<Character> response = characterController.addCharacter(characterJson, image);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newCharacter, response.getBody());
    }

    @Test
    void testUpdateCharacter() throws Exception {
        Long characterId = 1L;
        String characterJson = "{\"name\":\"updatedCharacter\"}";
        MockMultipartFile image = new MockMultipartFile("image", "image.png", MediaType.IMAGE_PNG_VALUE, "imageContent".getBytes());
        Character updatedCharacter = new Character();
        when(characterService.updateCharacter(eq(characterId), eq(characterJson), any(MultipartFile.class))).thenReturn(updatedCharacter);

        ResponseEntity<Character> response = characterController.updateCharacter(characterId, characterJson, image);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCharacter, response.getBody());
    }

    @Test
    void testDeleteCharacter() {
        Long characterId = 1L;

        ResponseEntity<Void> response = characterController.deleteCharacter(characterId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

