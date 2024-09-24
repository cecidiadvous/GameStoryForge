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

//    @GetMapping
//    public String chat(@RequestParam String prompt) {
//        ChatResponse response = chatService.getChatResponse(prompt);
//        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
//            return "No response";
//        }
//
//        String responseText = response.getChoices().get(0).getMessage().getContent();
//        responseText = responseText.replace("\\n", "");
//        return responseText;
//    }

    @PostMapping("/create")
    public ResponseEntity<?> createStory(@RequestBody Map<String, Object> payload) {
        int chapterId = Integer.parseInt(payload.get("chapterId").toString());
        String storyDescription = payload.get("storyDescription").toString();
        String responseText = storyService.createStory(chapterId, storyDescription);
        return ResponseEntity.ok(responseText);
    }


}
