package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.service.PdfService;
import com.pdfcrowd.Pdfcrowd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private PdfController pdfController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateGamePdf_Success() throws Exception {
        Long gameID = 1L;
        File pdfFile = File.createTempFile("test", ".pdf");
        try (FileOutputStream out = new FileOutputStream(pdfFile)) {
            out.write("Test PDF Content".getBytes());
        }
        when(pdfService.generatePdfFromGameID(eq(gameID))).thenReturn(pdfFile);

        ResponseEntity<InputStreamResource> response = pdfController.generateGamePdf(gameID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGenerateGamePdf_Exception() throws Exception {
        Long gameID = 1L;
        when(pdfService.generatePdfFromGameID(eq(gameID))).thenThrow(new Pdfcrowd.Error("Error generating PDF"));

        ResponseEntity<InputStreamResource> response = pdfController.generateGamePdf(gameID);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}
