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

        assertNotNull(createdGame, "The created game should not be null");
        assertEquals("Test Game", createdGame.getName(), "The game name should match");
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    void testCreateGame_UserNotFound() {
        String username = "nonExistentUser";
        Game game = new Game();

        when(userDataRepository.findByUsername(username)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> gameService.createGame(username, game), "Expected exception for non-existent user");
    }

    @Test
    void testGetGameById_Success() {
        Long gameId = 1L;
        Game game = new Game();
        game.setGameId(gameId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        Game foundGame = gameService.getGameById(gameId);

        assertNotNull(foundGame, "The found game should not be null");
        assertEquals(gameId, foundGame.getGameId(), "The game ID should match");
    }

    @Test
    void testGetGameById_GameNotFound() {
        Long gameId = 1L;

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gameService.getGameById(gameId), "Expected exception for non-existent game");
    }

    @Test
    void testUpdateGame_Success() {
        Long gameId = 1L;
        Game existingGame = new Game();
        existingGame.setGameId(gameId);
        existingGame.setName("Old Name");
        Game updatedGame = new Game();
        updatedGame.setName("New Name");

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(existingGame));
        when(gameRepository.save(any(Game.class))).thenReturn(existingGame);

        Game result = gameService.updateGame(gameId, updatedGame, null);

        assertNotNull(result, "The updated game should not be null");
        assertEquals("New Name", result.getName(), "The game name should be updated");
        verify(gameRepository, times(1)).save(existingGame);
    }

    @Test
    void testUpdateGame_GameNotFound() {
        Long gameId = 1L;
        Game updatedGame = new Game();

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gameService.updateGame(gameId, updatedGame, null), "Expected exception for non-existent game");
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

        assertNotNull(games, "The games list should not be null");
        assertEquals(1, games.size(), "The games list size should be 1");
        assertEquals("Test Game", games.get(0).getName(), "The game name should match");
    }

    @Test
    void testGetGamesByUsername_UserNotFound() {
        String username = "nonExistentUser";

        when(userDataRepository.findByUsername(username)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> gameService.getGamesByUsername(username), "Expected exception for non-existent user");
    }
}

