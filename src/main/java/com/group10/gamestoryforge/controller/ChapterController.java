package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.model.Chapter;
import com.group10.gamestoryforge.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Chapter> getChaptersByGameId(@RequestParam Integer gameId) {


        return chapterService.getChaptersByGameId(gameId);
    }

    @PostMapping
    public Chapter createChapter(@RequestBody Chapter chapter) {

        return chapterService.createChapter(chapter);
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

