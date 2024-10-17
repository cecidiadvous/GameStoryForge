package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.model.ChatResponse;
import com.group10.gamestoryforge.service.ChatService;
import com.group10.gamestoryforge.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private StoryService storyService;

    @PostMapping("/create")
    public ResponseEntity<?> createStory(@RequestBody Map<String, Object> payload) {
        Long chapterId = Long.parseLong(payload.get("chapterId").toString());
        String storyDescription = payload.get("storyDescription").toString();
        String responseText = storyService.createStory(chapterId, storyDescription);
        return ResponseEntity.ok(responseText);
    }

    @GetMapping("/{chapterId}")
    public ResponseEntity<?> getStory(@PathVariable Long chapterId) {
        String responseText = storyService.getStoryByChapterId(chapterId);
        return ResponseEntity.ok(responseText);
    }
}
