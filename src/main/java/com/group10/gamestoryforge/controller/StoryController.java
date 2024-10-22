package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<String>> createStory(@RequestBody Map<String, Object> payload) {
        Long chapterId = Long.parseLong(payload.get("chapterId").toString());
        String storyDescription = payload.get("storyDescription").toString();
        return storyService.createStory(chapterId, storyDescription)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{chapterId}")
    public CompletableFuture<ResponseEntity<String>> getStory(@PathVariable Long chapterId) {
        return storyService.getStoryByChapterId(chapterId)
                .thenApply(ResponseEntity::ok);
    }
}
