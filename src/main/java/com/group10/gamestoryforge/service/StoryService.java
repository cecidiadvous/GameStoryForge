package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.dao.ChapterRepository;
import com.group10.gamestoryforge.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.group10.gamestoryforge.model.Message;
import com.group10.gamestoryforge.dao.CharacterRepository;
import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.service.ChatService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoryService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private ChatService chatService;

    public String createStory(Integer chapterId, String storyDescription) {

        List<Character> characters = characterRepository.findByChapters_ChapterId(chapterId);

        // Convert characters list to a string
        String charactersString = characters.stream()
                .map(Character::toString)
                .collect(Collectors.joining(", "));

        // Concatenate characters string with storyDescription
        String fullStoryDescription = storyDescription + " Characters: " + charactersString;


        List<Message> messagesList = new ArrayList<>();

        messagesList.add(new Message("system", "你是一个可以根据我输入的游戏人物以及大致剧情生成对应完整游戏剧情（不包括对话，（Storyline）包括游戏的背景、世界观、主要事件、任务和角色发展），以及人物对话dialogue的ai，" +
                "我每次都会输入一章节，然后你完成这一章节内容，每章故事剧情大概两千字。游戏剧情引人入胜，人物性格突出。大致剧情gameStory不包括人物对话，剧情以第三人称描述。"));

        messagesList.add(new Message("user", fullStoryDescription));

        ChatResponse response = chatService.getChatResponse(messagesList);


        String responseText = response.getChoices().get(0).getMessage().getContent();
        responseText = responseText.replace("\\n", "");
        System.out.println(responseText);

        return responseText;
    }
}