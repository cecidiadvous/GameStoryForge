package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.dao.ChapterRepository;
import com.group10.gamestoryforge.dao.CharacterRepository;
import com.group10.gamestoryforge.dao.GameRepository;
import com.group10.gamestoryforge.model.Chapter;
import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChapterServiceTest {

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CharacterRepository characterRepository;

    @InjectMocks
    private ChapterService chapterService;

    private Game game;
    private Chapter chapter;
    private Character character;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        game = new Game();
        game.setGameId(1L);
        game.setName("Test Game");

        chapter = new Chapter();
        chapter.setChapterId(1L);
        chapter.setName("Test Chapter");
        chapter.setGame(game);
        chapter.setCreatedAt(LocalDateTime.now());

        character = new Character();
        character.setCharacterId(1L);
        character.setName("Test Character");
    }

    @Test
    void testGetAllChapters() {
        List<Chapter> chapters = new ArrayList<>();
        chapters.add(chapter);
        when(chapterRepository.findAll()).thenReturn(chapters);

        List<Chapter> result = chapterService.getAllChapters();

        assertEquals(1, result.size());
        assertEquals("Test Chapter", result.get(0).getName());
    }

    @Test
    void testGetChapterById() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        Optional<Chapter> result = chapterService.getChapterById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Chapter", result.get().getName());
    }

    @Test
    void testGetChaptersByGameId() {
        List<Chapter> chapters = new ArrayList<>();
        chapters.add(chapter);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(chapterRepository.findByGame(game)).thenReturn(chapters);

        List<Chapter> result = chapterService.getChaptersByGameId(1L);

        assertEquals(1, result.size());
        assertEquals("Test Chapter", result.get(0).getName());
    }

    @Test
    void testAddCharacterToChapter() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));

        chapterService.addCharacterToChapter(1L, 1L);

        verify(chapterRepository, times(1)).save(chapter);
        assertTrue(chapter.getCharacters().contains(character));
    }

    @Test
    void testGetCharactersByChapterId() {
        List<Character> characters = new ArrayList<>();
        characters.add(character);
        when(characterRepository.findByChapters_ChapterId(1L)).thenReturn(characters);

        List<Character> result = chapterService.getCharactersByChapterId(1L);

        assertEquals(1, result.size());
        assertEquals("Test Character", result.get(0).getName());
    }

    @Test
    void testRemoveCharacterFromChapter() {
        chapter.getCharacters().add(character);
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));

        chapterService.removeCharacterFromChapter(1L, 1L);

        verify(chapterRepository, times(1)).save(chapter);
        assertFalse(chapter.getCharacters().contains(character));
    }

    @Test
    void testCreateChapterForGame() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(chapterRepository.save(any(Chapter.class))).thenReturn(chapter);

        Chapter result = chapterService.createChapterForGame(1L, chapter);

        assertNotNull(result);
        assertEquals("Test Chapter", result.getName());
        assertEquals(game, result.getGame());
    }

    @Test
    void testUpdateChapter() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(chapterRepository.save(any(Chapter.class))).thenReturn(chapter);

        chapter.setName("Updated Chapter");
        Chapter result = chapterService.updateChapter(1L, chapter);

        assertNotNull(result);
        assertEquals("Updated Chapter", result.getName());
    }

    @Test
    void testDeleteChapter() {
        chapterService.deleteChapter(1L);
        verify(chapterRepository, times(1)).deleteById(1L);
    }
}

