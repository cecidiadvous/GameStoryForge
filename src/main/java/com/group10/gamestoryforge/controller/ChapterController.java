package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.model.Chapter;
import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

//    @GetMapping
//    public List<Chapter> getAllChapters() {
//        return chapterService.getAllChapters();
//    }

    @GetMapping
    public List<Chapter> getChaptersByGameId(@RequestParam Long gameId) {


        return chapterService.getChaptersByGameId(gameId);
    }

    @PostMapping("/{chapterId}/addCharacter")
    public ResponseEntity<?> addCharacterToChapter(@PathVariable Integer chapterId, @RequestBody Map<String, Integer> payload) {
        Integer characterId = payload.get("characterId");
        chapterService.addCharacterToChapter(chapterId, characterId);
        return ResponseEntity.ok("Character added to chapter.");
    }
    @GetMapping("/{chapterId}/characters")
    public List<Character> getCharactersByChapterId(@PathVariable Integer chapterId) {

        return chapterService.getCharactersByChapterId(chapterId);
    }

    @PostMapping("/{chapterId}/removeCharacter")
    public ResponseEntity<?> removeCharacterFromChapter(@PathVariable Integer chapterId, @RequestBody Map<String, Integer> payload) {
        Integer characterId = payload.get("characterId");
        chapterService.removeCharacterFromChapter(chapterId, characterId);
        return ResponseEntity.ok("Character removed from chapter.");
    }

    @PostMapping("/game/{gameId}")
    public ResponseEntity<Chapter> createChapter(@PathVariable Long gameId, @RequestBody Chapter chapterDetails) {
        // 使用 service 创建 chapter，并关联到指定的 game
        Chapter createdChapter = chapterService.createChapterForGame(gameId, chapterDetails);
        return ResponseEntity.ok(createdChapter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chapter> updateChapter(@PathVariable Integer id, @RequestBody Chapter chapterDetails) {
        Chapter updatedChapter = chapterService.updateChapter(id, chapterDetails);
        return ResponseEntity.ok(updatedChapter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Integer id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }
}

