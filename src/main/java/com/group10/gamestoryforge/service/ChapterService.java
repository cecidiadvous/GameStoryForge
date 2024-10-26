package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.dao.GameRepository;
import com.group10.gamestoryforge.model.Chapter;
import com.group10.gamestoryforge.dao.ChapterRepository;
import com.group10.gamestoryforge.dao.CharacterRepository;
import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.model.Game;
import com.group10.gamestoryforge.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CharacterRepository characterRepository;
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    public Optional<Chapter> getChapterById(Long id) {
        return chapterRepository.findById(id);
    }

    public List<Chapter> getChaptersByGameId(Long gameId) {
        // Fetch the Game entity based on the gameId
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));

        // Use the Game entity to find all chapters related to this game
        List<Chapter> chapters = chapterRepository.findByGame(game);
        chapters.sort(Comparator.comparing(Chapter::getCreatedAt));
        return chapters;
    }

    public void addCharacterToChapter(Long chapterId, Long characterId) {

        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow();
        Character character = characterRepository.findById(characterId).orElseThrow();

        // 将角色添加到章节
        chapter.getCharacters().add(character);
        chapterRepository.save(chapter);
    }

    public List<Character> getCharactersByChapterId(Long chapterId) {
        return characterRepository.findByChapters_ChapterId(chapterId);
    }

    public void removeCharacterFromChapter(Long chapterId, Long characterId) {
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow();
        Character character = characterRepository.findById(characterId).orElseThrow();

        // 将角色从章节中移除
        chapter.getCharacters().remove(character);
        chapterRepository.save(chapter);
    }

    public Chapter createChapterForGame(Long gameId, Chapter chapterDetails) {
        // 根据 gameId 查找 Game 对象
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));

        // 关联 Game 到 Chapter
        chapterDetails.setGame(game);
        chapterDetails.setCreatedAt(LocalDateTime.now());

        // 保存并返回 Chapter 对象
        return chapterRepository.save(chapterDetails);
    }

    public Chapter updateChapter(Long id, Chapter chapterDetails) {
        Chapter chapter = chapterRepository.findById(id).orElseThrow();
        chapter.setName(chapterDetails.getName());
        chapter.setUpdatedAt(LocalDateTime.now());
        return chapterRepository.save(chapter);
    }

    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }
}

