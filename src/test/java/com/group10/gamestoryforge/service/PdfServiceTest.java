package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.dao.ChapterRepository;
import com.group10.gamestoryforge.dao.GameRepository;
import com.group10.gamestoryforge.model.Chapter;
import com.group10.gamestoryforge.model.Game;
import com.pdfcrowd.Pdfcrowd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @InjectMocks
    private PdfService pdfService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGeneratePdfFromGameID_Success() throws IOException, Pdfcrowd.Error {
        Long gameId = 1L;
        Game game = new Game();
        game.setGameId(gameId);
        game.setName("Test Game");

        Chapter chapter = new Chapter();
        chapter.setName("Chapter 1");
        chapter.setSystemText("This is the system text for chapter 1.");

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(chapterRepository.findByGame(game)).thenReturn(Collections.singletonList(chapter));

        File pdfFile = pdfService.generatePdfFromGameID(gameId);

        assertNotNull(pdfFile, "The generated PDF file should not be null");
        assertTrue(pdfFile.exists(), "The generated PDF file should exist");
        assertTrue(pdfFile.length() > 0, "The generated PDF file should not be empty");

        // Clean up the generated file
        if (pdfFile.exists()) {
            assertTrue(pdfFile.delete(), "The generated PDF file should be deleted after the test");
        }
    }

    @Test
    void testGeneratePdfFromGameID_GameNotFound() {
        Long gameId = 1L;

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pdfService.generatePdfFromGameID(gameId), "Expected exception for non-existent game");
    }

    @Test
    void testGeneratePdfFromGameID_EmptyChapters() throws IOException, Pdfcrowd.Error {
        Long gameId = 1L;
        Game game = new Game();
        game.setGameId(gameId);
        game.setName("Test Game");

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(chapterRepository.findByGame(game)).thenReturn(Collections.emptyList());

        File pdfFile = pdfService.generatePdfFromGameID(gameId);

        assertNotNull(pdfFile, "The generated PDF file should not be null");
        assertTrue(pdfFile.exists(), "The generated PDF file should exist");
        assertTrue(pdfFile.length() > 0, "The generated PDF file should not be empty");

        // Clean up the generated file
        if (pdfFile.exists()) {
            assertTrue(pdfFile.delete(), "The generated PDF file should be deleted after the test");
        }
    }

    @Test
    void testGeneratePdfFromGameID_PdfcrowdError() throws IOException, Pdfcrowd.Error {
        Long gameId = 1L;
        Game game = new Game();
        game.setGameId(gameId);
        game.setName("Test Game");

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(chapterRepository.findByGame(game)).thenReturn(Collections.emptyList());

        PdfService pdfServiceSpy = spy(pdfService);
        doThrow(new Pdfcrowd.Error("API error", 500)).when(pdfServiceSpy).generatePdfFromGameID(anyLong());

        assertThrows(Pdfcrowd.Error.class, () -> pdfServiceSpy.generatePdfFromGameID(gameId), "Expected Pdfcrowd error");
    }
}
