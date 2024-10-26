package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.dao.ChapterRepository;
import com.group10.gamestoryforge.dao.CharacterRepository;
import com.group10.gamestoryforge.model.ChatResponse;
import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.model.Chapter;
import com.group10.gamestoryforge.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class StoryService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChapterRepository chapterRepository;

    public CompletableFuture<String> createStory(Long chapterId, String storyDescription) {

        List<Chapter> chapters = getChaptersCreatedBefore(chapterId);

        String previousStorylines = "";

        for (Chapter chapter : chapters) {
            previousStorylines += chapter.getSystemText() + "\n";
        }

        List<Character> characters = characterRepository.findByChapters_ChapterId(chapterId);

        // Convert characters list to a string
        String charactersString = characters.stream()
                .map(Character::toString)
                .collect(Collectors.joining(", "));

        // Concatenate characters string with storyDescription
        String fullStoryDescription = "previously generated dialogues and storyline:"
                + previousStorylines
                + " Brief game chapter story summary: " + storyDescription
                + " Characters: " + charactersString;

        List<Message> messagesList = new ArrayList<>();
        messagesList.add(new Message("system",
                "You are an AI capable of generating detailed game storyline(1,000 to 1,200 words) and including character dialogues based on the game characters and brief chapter summaries I provide." +
                        "The storyline should include the game's background, world-building, key events, quests, and character development." +
                        "In each chapter, you will generate an engaging, immersive third-person storyline of 1,300 to 1,700 words, with characters that have distinctive personalities." +
                        "The characters' dialogues should be naturally integrated into the story and reflect their traits." +
                        "The output language should be consistent with the input language provided by the user, and all punctuation should follow the rules of that language." +
                        "If this is not the first chapter, I will provide the previously generated dialogues and storyline, and you need to maintain the continuity and consistency of the story based on these."
        ));

        messagesList.add(new Message("user", fullStoryDescription));

        return chatService.getChatResponse(messagesList).thenApply(response -> {
            String responseText = response.getChoices().get(0).getMessage().getContent();
            responseText = responseText.replace("\\n", "");

            // 更新数据库中的章节信息
            Chapter chapter = chapterRepository.findById(chapterId)
                    .orElseThrow(() -> new RuntimeException("Chapter not found"));
            chapter.setSystemText(responseText);
            chapterRepository.save(chapter);

            return responseText;
        });
    }

    public List<Chapter> getChaptersCreatedBefore(Long chapterId) {
        Chapter currentChapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
        List<Chapter> allChapters = chapterRepository.findByGame(currentChapter.getGame());
        return allChapters.stream()
                .filter(chapter -> chapter.getCreatedAt().isBefore(currentChapter.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public CompletableFuture<String> getStoryByChapterId(Long chapterId) {
        return CompletableFuture.supplyAsync(() -> {
            Chapter chapter = chapterRepository.findById(chapterId)
                    .orElseThrow(() -> new RuntimeException("Chapter not found"));
            return chapter.getSystemText();
        });
    }
}
