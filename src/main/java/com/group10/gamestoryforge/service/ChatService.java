package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.model.ChatResponse;
import com.group10.gamestoryforge.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public ChatService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public ChatResponse getChatResponse( List<Message> messagesList) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String model = "\"model\": \"gpt-4o\"";
        int  max_tokens = 3000;

        // 动态生成消息列表My Workspace
        //
        //Create a collection for your requests
        //A collection lets you group related requests and easily set common authorization, tests, scripts, and variables for all requests in it.
//        List<Message> messagesList = new ArrayList<>();
//        messagesList.add(new Message("system", "你是一个可以根据我输入的游戏人物以及大致剧情生成对应完整游戏剧情（不包括对话，（Storyline）包括游戏的背景、世界观、主要事件、任务和角色发展），以及人物对话dialogue的ai，" +
//                "我每次都会输入一章节，然后你完成这一章节内容，每章故事剧情大概两千字。游戏剧情引人入胜，人物性格突出。大致剧情gameStory不包括人物对话，剧情以第三人称描述。"));
//
//        messagesList.add(new Message("user", prompt));  // 使用传入的 prompt 作为用户的消息

        try {
            // 使用 Jackson 将 messagesList 序列化为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String messagesJson = objectMapper.writeValueAsString(messagesList);
            String messages = "\"messages\": " + messagesJson;

            String responseFormat = """
                        
                    "response_format": {
                        "type": "json_schema",
                        "json_schema": {
                          "name": "gameStory",
                          "schema": {
                            "type": "object",
                            "properties": {
                              "storyline": {
                                "type": "string"
                              },
                              "dialogue": {
                                "type": "array",
                                "items": {
                                  "type": "object",
                                  "properties": {
                                    "character": {
                                      "type": "string",
                                      "description": "The name of the character speaking."
                                    },
                                    "text": {
                                      "type": "string",
                                      "description": "The dialogue content of the character."
                                    }
                                  },
                                  "required": ["character", "text"],
                                  "additionalProperties": false
                                }
                              }
                            },
                            "required": ["storyline", "dialogue"],
                            "additionalProperties": false
                          },
                          "strict": true
                        }
                      }    
                        """;

//            String requestBody = "{"
//                    + model + ","
//                    + messages + ","
//                    + "\"max_tokens\": " + max_tokens + ","
//                    + responseFormat
//                    + "}";

            String requestBody = "{"
                    + model + ","
                    + messages + ","
                    + responseFormat
                    + "}";

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<ChatResponse> response = restTemplate.postForEntity(apiUrl, entity, ChatResponse.class);
            String responseText = response.getBody().getChoices().get(0).getMessage().getContent();
            System.out.println(responseText);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
