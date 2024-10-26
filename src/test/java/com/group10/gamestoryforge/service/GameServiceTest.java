package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.dao.GameRepository;
import com.group10.gamestoryforge.dao.UserDataRepository;
import com.group10.gamestoryforge.model.Game;
import com.group10.gamestoryforge.model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserDataRepository userDataRepository;

    @Mock
    private MultipartFile image;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGame_Success() {
        String username = "testUser";
        Game game = new Game();
        game.setName("Test Game");
        UserData user = new UserData();
        user.setUsername(username);

        when(userDataRepository.findByUsername(username)).thenReturn(user);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Game createdGame = gameService.createGame(username, game);

        assertNotNull(createdGame);
        assertEquals("Test Game", createdGame.getName());
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    void testCreateGame_UserNotFound() {
        String username = "nonExistentUser";
        Game game = new Game();

        when(userDataRepository.findByUsername(username)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> gameService.createGame(username, game));
    }

    @Test
    void testSaveGameImage_Success() throws IOException {
        when(image.getOriginalFilename()).thenReturn("image.png");
        when(image.getBytes()).thenReturn("image content".getBytes());

        String imagePath = gameService.saveGameImage(image);

        assertNotNull(imagePath);
        assertTrue(imagePath.contains("uploads/images/"));
    }

    @Test
    void testSaveGameImage_IOException() throws IOException {
        when(image.getOriginalFilename()).thenReturn("image.png");
        when(image.getBytes()).thenThrow(new IOException("Failed to read image bytes"));

        assertThrows(RuntimeException.class, () -> gameService.saveGameImage(image));
    }

    @Test
    void testDeleteGameImage_Success() throws IOException {
        Path path = Paths.get(System.getProperty("user.dir") + "/uploads/images/test.png");
        Files.createDirectories(path.getParent());
        Files.createFile(path);

        assertTrue(Files.exists(path));

        gameService.deleteGameImage("/uploads/images/test.png");

        assertFalse(Files.exists(path));
    }

    @Test
    void testDeleteGameImage_FileDoesNotExist() {
        assertDoesNotThrow(() -> gameService.deleteGameImage("/uploads/images/non_existent.png"));
    }

    @Test
    void testDeleteGameImage_DeleteFails() throws IOException {
        Path path = Paths.get(System.getProperty("user.dir") + "/uploads/images/protected.png");
        Files.createDirectories(path.getParent());
        Files.createFile(path);
        path.toFile().setWritable(false);

        assertThrows(RuntimeException.class, () -> gameService.deleteGameImage("/uploads/images/protected.png"));

        path.toFile().setWritable(true);
        Files.deleteIfExists(path);
    }

    @Test
    void testUpdateGame_SuccessWithImage() throws IOException {
        Long gameId = 1L;
        Game existingGame = new Game();
        existingGame.setGameId(gameId);
        existingGame.setName("Old Name");
        existingGame.setImage("/uploads/images/old_image.png");

        Game updatedGame = new Game();
        updatedGame.setName("New Name");

        when(image.getOriginalFilename()).thenReturn("new_image.png");
        when(image.getBytes()).thenReturn("image content".getBytes());
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(existingGame));
        when(gameRepository.save(any(Game.class))).thenReturn(existingGame);

        Game result = gameService.updateGame(gameId, updatedGame, image);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(gameRepository, times(1)).save(existingGame);
    }

    @Test
    void testUpdateGame_NoImageUpdate() {
        Long gameId = 1L;
        Game existingGame = new Game();
        existingGame.setGameId(gameId);
        existingGame.setName("Old Name");

        Game updatedGame = new Game();
        updatedGame.setName("New Name");

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(existingGame));
        when(gameRepository.save(any(Game.class))).thenReturn(existingGame);

        Game result = gameService.updateGame(gameId, updatedGame, null);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(gameRepository, times(1)).save(existingGame);
    }

    @Test
    void testDeleteGame_Success() {
        Long gameId = 1L;
        Game game = new Game();
        game.setGameId(gameId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        gameService.deleteGame(gameId);

        verify(gameRepository, times(1)).delete(game);
    }

    @Test
    void testDeleteGame_GameNotFound() {
        Long gameId = 1L;

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        gameService.deleteGame(gameId);

        verify(gameRepository, never()).delete(any(Game.class));
    }

    @Test
    void testGetGamesByUsername_Success() {
        String username = "testUser";
        UserData user = new UserData();
        user.setUsername(username);
        Game game = new Game();
        game.setName("Test Game");

        when(userDataRepository.findByUsername(username)).thenReturn(user);
        when(gameRepository.findByUser(user)).thenReturn(Collections.singletonList(game));

        List<Game> games = gameService.getGamesByUsername(username);

        assertNotNull(games);
        assertEquals(1, games.size());
        assertEquals("Test Game", games.get(0).getName());
    }

    @Test
    void testGetGamesByUsername_UserNotFound() {
        String username = "nonExistentUser";

        when(userDataRepository.findByUsername(username)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> gameService.getGamesByUsername(username));
    }
}


