package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.model.Chapter;
import com.group10.gamestoryforge.dao.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    public Optional<Chapter> getChapterById(Integer id) {
        return chapterRepository.findById(id);
    }

    public Chapter createChapter(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    public Chapter updateChapter(Integer id, Chapter chapterDetails) {
        Chapter chapter = chapterRepository.findById(id).orElseThrow();
        chapter.setName(chapterDetails.getName());
        chapter.setDescription(chapterDetails.getDescription());
        chapter.setGameId(chapterDetails.getGameId());
        chapter.setUserText(chapterDetails.getUserText());
        chapter.setSystemText(chapterDetails.getSystemText());
        chapter.setUpdatedAt(LocalDateTime.now());
        return chapterRepository.save(chapter);
    }

    public void deleteChapter(Integer id) {
        chapterRepository.deleteById(id);
    }
}

