package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.service.StoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class StoryControllerTest {

    @Mock
    private StoryService storyService;

    @InjectMocks
    private StoryController storyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStory_Success() throws Exception {
        Long chapterId = 1L;
        String storyDescription = "Test Story Description";
        Map<String, Object> payload = Map.of(
                "chapterId", chapterId,
                "storyDescription", storyDescription
        );

        when(storyService.createStory(eq(chapterId), eq(storyDescription))).thenReturn(CompletableFuture.completedFuture("Story created successfully"));

        CompletableFuture<ResponseEntity<String>> responseFuture = storyController.createStory(payload);
        ResponseEntity<String> response = responseFuture.get();

        assertEquals(ResponseEntity.ok("Story created successfully"), response);
    }

    @Test
    void testGetStory_Success() throws Exception {
        Long chapterId = 1L;
        String storyContent = "Test Story Content";

        when(storyService.getStoryByChapterId(eq(chapterId))).thenReturn(CompletableFuture.completedFuture(storyContent));

        CompletableFuture<ResponseEntity<String>> responseFuture = storyController.getStory(chapterId);
        ResponseEntity<String> response = responseFuture.get();

        assertEquals(ResponseEntity.ok(storyContent), response);
    }
}

