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

    public String createStory(Long chapterId, String storyDescription) {

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
                "You are an AI capable of generating detailed game storyline(1,300 to 1,700 words) and including character dialogues based on the game characters and brief chapter summaries I provide." +
                        "The storyline should include the game's background, world-building, key events, quests, and character development." +
                        "In each chapter, you will generate an engaging, immersive third-person storyline of 1,300 to 1,700 words, with characters that have distinctive personalities." +
                        "The characters' dialogues should be naturally integrated into the story and reflect their traits." +
                        "The output language should be consistent with the input language provided by the user, and all punctuation should follow the rules of that language." +
                        "If this is not the first chapter, I will provide the previously generated dialogues and storyline, and you need to maintain the continuity and consistency of the story based on these."
        ));

//        messagesList.add(new Message("system",
//                "我是一个需要为自己制定一周锻炼计划的人。请为我提供一个详细的健身计划，分配到每天，涵盖不同的肌肉群和运动类型，适合我的体能水平。计划应包括以下方面：\n" +
//                        "\n" +
//                        "每天的具体锻炼内容，例如有氧训练（如跑步、骑自行车）、力量训练（如深蹲、卧推、硬拉）和柔韧性训练（如瑜伽或拉伸）。\n" +
//                        "每个动作的建议组数和重复次数，适合中等强度的锻炼水平。\n" +
//                        "每天的锻炼时间建议，以及休息日安排。"
//        ));




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

    public List<Chapter> getChaptersCreatedBefore(Long chapterId) {
        Chapter currentChapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
        Game game = currentChapter.getGame();
        List<Chapter> allChapters = chapterRepository.findByGame(game);
        return allChapters.stream()
                .filter(chapter -> chapter.getCreatedAt().isBefore(currentChapter.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public String getStoryByChapterId(Long chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        return chapter.getSystemText();
    }
}