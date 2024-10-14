package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.dao.ChapterRepository;
import com.group10.gamestoryforge.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.group10.gamestoryforge.model.Message;
import com.group10.gamestoryforge.dao.CharacterRepository;
import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.service.ChatService;
import com.group10.gamestoryforge.model.Chapter;
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

        List<Character> characters = characterRepository.findByChapters_ChapterId(chapterId);

        // Convert characters list to a string
        String charactersString = characters.stream()
                .map(Character::toString)
                .collect(Collectors.joining(", "));

        // Concatenate characters string with storyDescription
        String fullStoryDescription = storyDescription + " Characters: " + charactersString;


        List<Message> messagesList = new ArrayList<>();

        messagesList.add(new Message("system", "You are an AI capable of generating detailed game storylines(exclude dialogue) and character dialogues(include dialogue) " +
                "based on the game characters and general plot that I provide. The storyline should include the game's background, world-building, " +
                "key events, quests, and character development. I will provide input chapter by chapter, " +
                "and for each chapter, you will generate around 1500 words of engaging and immersive storyline, written in third-person perspective, " +
                "with distinctive character personalities. The character dialogues will Reflect the character characteristics. The output language should match " +
                "the input language provided by the user, and all punctuation should follow English punctuation rules."));


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

    public String getStoryByChapterId(Integer chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        return chapter.getSystemText();
    }
}