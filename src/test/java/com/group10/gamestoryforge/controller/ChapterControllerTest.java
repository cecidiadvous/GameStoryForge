package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.model.Chapter;
import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.service.ChapterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ChapterControllerTest {

    @Mock
    private ChapterService chapterService;

    @InjectMocks
    private ChapterController chapterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetChaptersByGameId() {
        Long gameId = 1L;
        List<Chapter> chapters = List.of(new Chapter(), new Chapter());
        when(chapterService.getChaptersByGameId(eq(gameId))).thenReturn(chapters);

        List<Chapter> result = chapterController.getChaptersByGameId(gameId);

        assertEquals(2, result.size());
    }

    @Test
    void testAddCharacterToChapter() {
        Long chapterId = 1L;
        Long characterId = 2L;
        Map<String, Long> payload = Map.of("characterId", characterId);

        ResponseEntity<?> response = chapterController.addCharacterToChapter(chapterId, payload);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Character added to chapter.", response.getBody());
    }

    @Test
    void testGetCharactersByChapterId() {
        Long chapterId = 1L;
        List<Character> characters = List.of(new Character(), new Character());
        when(chapterService.getCharactersByChapterId(eq(chapterId))).thenReturn(characters);

        List<Character> result = chapterController.getCharactersByChapterId(chapterId);

        assertEquals(2, result.size());
    }

    @Test
    void testRemoveCharacterFromChapter() {
        Long chapterId = 1L;
        Long characterId = 2L;
        Map<String, Long> payload = Map.of("characterId", characterId);

        ResponseEntity<?> response = chapterController.removeCharacterFromChapter(chapterId, payload);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Character removed from chapter.", response.getBody());
    }

    @Test
    void testCreateChapter() {
        Long gameId = 1L;
        Chapter chapterDetails = new Chapter();
        Chapter createdChapter = new Chapter();
        when(chapterService.createChapterForGame(eq(gameId), any(Chapter.class))).thenReturn(createdChapter);

        ResponseEntity<Chapter> response = chapterController.createChapter(gameId, chapterDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdChapter, response.getBody());
    }

    @Test
    void testUpdateChapter() {
        Long chapterId = 1L;
        Chapter chapterDetails = new Chapter();
        Chapter updatedChapter = new Chapter();
        when(chapterService.updateChapter(eq(chapterId), any(Chapter.class))).thenReturn(updatedChapter);

        ResponseEntity<Chapter> response = chapterController.updateChapter(chapterId, chapterDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedChapter, response.getBody());
    }

    @Test
    void testDeleteChapter() {
        Long chapterId = 1L;

        ResponseEntity<Void> response = chapterController.deleteChapter(chapterId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

