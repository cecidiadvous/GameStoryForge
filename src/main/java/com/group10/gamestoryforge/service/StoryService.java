package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.dao.ChapterRepository;
import com.group10.gamestoryforge.model.*;
import com.group10.gamestoryforge.model.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.group10.gamestoryforge.dao.CharacterRepository;
import com.group10.gamestoryforge.service.ChatService;
import com.group10.gamestoryforge.dao.ChapterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoryService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChapterRepository chapterRepository;

    public String createStory(Integer chapterId, String storyDescription) {

        List<Chapter> chapters = getChaptersCreatedBefore(chapterId);

        String previousStorylines = "";

        for (Chapter chapter : chapters) {
            previousStorylines += chapter.getSystemText() + "\n";
            System.out.println("-----------------------------");
            System.out.println("SystemText: " + chapter.getSystemText());
            System.out.println("-----------------------------");
        }


        List<Character> characters = characterRepository.findByChapters_ChapterId(chapterId);

        // Convert characters list to a string
        String charactersString = characters.stream()
                .map(Character::toString)
                .collect(Collectors.joining(", "));

        // Concatenate characters string with storyDescription
        String fullStoryDescription = "previously generated dialogues and storyline:"+ previousStorylines  + " Brief game chapter story summary: " + storyDescription + " Characters: " + charactersString;

        System.out.println("fullStoryDescription: " + fullStoryDescription);




        List<Message> messagesList = new ArrayList<>();

        messagesList.add(new Message("system",
                "You are an AI capable of generating detailed game storylines and including character dialogues based on the game characters and brief chapter summaries I provide." +
                        "The stories should include the game's background, world-building, key events, quests, and character development." +
                        "In each chapter, you will generate an engaging, immersive third-person narrative of 1,300 to 1,600 words, with characters that have distinctive personalities." +
                        "The characters' dialogues should be naturally integrated into the story and reflect their traits." +
                        "The output language should be consistent with the input language provided by the user, and all punctuation should follow the rules of that language." +
                        "If this is not the first chapter, I will provide the previously generated dialogues and storyline, and you need to maintain the continuity and consistency of the story based on these."
        ));


        messagesList.add(new Message("user", fullStoryDescription));

        ChatResponse response = chatService.getChatResponse(messagesList);


        String responseText = response.getChoices().get(0).getMessage().getContent();
        responseText = responseText.replace("\\n", "");
        System.out.println(responseText);

        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        // Update the systemText field
        chapter.setSystemText(responseText);

        // Save the updated Chapter entity
        chapterRepository.save(chapter);

        return responseText;
    }

    public List<Chapter> getChaptersCreatedBefore(Integer chapterId) {
        Chapter currentChapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
        Game game = currentChapter.getGame();
        List<Chapter> allChapters = chapterRepository.findByGame(game);
        return allChapters.stream()
                .filter(chapter -> chapter.getCreatedAt().isBefore(currentChapter.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public String getStoryByChapterId(Integer chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        return chapter.getSystemText();
    }
}