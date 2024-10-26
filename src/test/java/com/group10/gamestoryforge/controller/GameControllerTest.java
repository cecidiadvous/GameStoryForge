package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.model.Game;
import com.group10.gamestoryforge.service.GameService;
import com.group10.gamestoryforge.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GameControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private UserService userService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGame_Success() {
        String username = "testUser";
        String name = "testGame";
        String description = "testDescription";
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "imageContent".getBytes());

        Game game = new Game();
        game.setName(name);
        game.setDescription(description);

        when(gameService.createGame(eq(username), any(Game.class))).thenReturn(game);

        ResponseEntity<Game> response = gameController.createGame(username, name, description, image);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(game, response.getBody());
    }

    @Test
    void testCreateGame_WithImage() {
        String username = "testUser";
        String name = "testGame";
        String description = "testDescription";
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "imageContent".getBytes());

        Game game = new Game();
        game.setName(name);
        game.setDescription(description);

        when(gameService.saveGameImage(image)).thenReturn("imagePath");
        when(gameService.createGame(eq(username), any(Game.class))).thenReturn(game);

        ResponseEntity<Game> response = gameController.createGame(username, name, description, image);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(game, response.getBody());
    }

    @Test
    void testCreateGame_WithoutImage() {
        String username = "testUser";
        String name = "testGame";
        String description = "testDescription";

        Game game = new Game();
        game.setName(name);
        game.setDescription(description);

        when(gameService.createGame(eq(username), any(Game.class))).thenReturn(game);

        ResponseEntity<Game> response = gameController.createGame(username, name, description, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(game, response.getBody());
    }

    @Test
    void testCreateGame_EmptyImage() {
        String username = "testUser";
        String name = "testGame";
        String description = "testDescription";
        MockMultipartFile emptyImage = new MockMultipartFile("image", "", "image/png", new byte[0]);

        Game game = new Game();
        game.setName(name);
        game.setDescription(description);

        when(gameService.createGame(eq(username), any(Game.class))).thenReturn(game);

        ResponseEntity<Game> response = gameController.createGame(username, name, description, emptyImage);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(game, response.getBody());
    }


    @Test
    void testCreateGame_BadRequest() {
        String username = "testUser";
        String name = "testGame";
        String description = "testDescription";
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "imageContent".getBytes());

        when(gameService.createGame(eq(username), any(Game.class))).thenReturn(null);

        ResponseEntity<Game> response = gameController.createGame(username, name, description, image);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetGamesByUsername_Success() {
        String username = "testUser";
        List<Game> games = List.of(new Game(), new Game());

        when(gameService.getGamesByUsername(eq(username))).thenReturn(games);

        ResponseEntity<List<Game>> response = gameController.getGamesByUsername(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetGameById_Success() {
        Long gameId = 1L;
        Game game = new Game();
        game.setName("testGame");

        when(gameService.getGameById(eq(gameId))).thenReturn(game);

        ResponseEntity<Game> response = gameController.getGameById(gameId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(game, response.getBody());
    }

    @Test
    void testGetGameById_NotFound() {
        Long gameId = 1L;

        when(gameService.getGameById(eq(gameId))).thenReturn(null);

        ResponseEntity<Game> response = gameController.getGameById(gameId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateGame_Success() {
        Long gameId = 1L;
        String name = "updatedGame";
        String description = "updatedDescription";
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "imageContent".getBytes());

        Game updatedGame = new Game();
        updatedGame.setName(name);
        updatedGame.setDescription(description);

        when(gameService.updateGame(eq(gameId), any(Game.class), any(MultipartFile.class))).thenReturn(updatedGame);

        ResponseEntity<Game> response = gameController.updateGame(gameId, name, description, image, "testUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedGame, response.getBody());
    }

    @Test
    void testUpdateGame_NotFound() {
        Long gameId = 1L;
        String name = "updatedGame";
        String description = "updatedDescription";
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "imageContent".getBytes());

        when(gameService.updateGame(eq(gameId), any(Game.class), any(MultipartFile.class))).thenReturn(null);

        ResponseEntity<Game> response = gameController.updateGame(gameId, name, description, image, "testUser");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteGame() {
        Long gameId = 1L;

        doNothing().when(gameService).deleteGame(gameId);

        ResponseEntity<Void> response = gameController.deleteGame(gameId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}