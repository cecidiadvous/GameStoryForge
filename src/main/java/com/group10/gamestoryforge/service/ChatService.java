package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.model.ChatResponse;
import com.group10.gamestoryforge.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public ChatService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async  // 将此方法标记为异步
    public CompletableFuture<ChatResponse> getChatResponse(List<Message> messagesList) {

        logger.info("Executing getChatResponse in thread: " + Thread.currentThread().getName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String model = "\"model\": \"gpt-4o\"";
        int max_tokens = 3000;

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

            String requestBody = "{" + model + "," + messages + "," + responseFormat + "}";

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<ChatResponse> response = restTemplate.postForEntity(apiUrl, entity, ChatResponse.class);

            // 返回异步结果
            return CompletableFuture.completedFuture(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null); // 处理错误时返回空
        }
    }
}
