package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.model.ChatResponse;
import com.group10.gamestoryforge.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
    }

    @Test
    void testGetChatResponse_Failure() throws Exception {
        // Prepare test data
        Message message = new Message("user", "This will fail");
        List<Message> messagesList = Collections.singletonList(message);

        // Mock restTemplate to throw an exception
        when(restTemplate.postForEntity(any(String.class), any(), eq(ChatResponse.class))).thenThrow(new RuntimeException("API error"));

        // Execute the service method
        CompletableFuture<ChatResponse> futureResponse = chatService.getChatResponse(messagesList);
        ChatResponse chatResponse = futureResponse.get();

        // Verify the response is null due to the failure
        assertNull(chatResponse, "The response should be null when an exception occurs");
    }


    @Test
    void testGetChatResponse_NullResponseFromApi() throws Exception {
        // Prepare test data
        Message message = new Message("user", "Hello?");
        List<Message> messagesList = Collections.singletonList(message);

        // Mock restTemplate behavior to return a null response
        ResponseEntity<ChatResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.postForEntity(any(String.class), any(), eq(ChatResponse.class))).thenReturn(responseEntity);

        // Execute the service method
        CompletableFuture<ChatResponse> futureResponse = chatService.getChatResponse(messagesList);
        ChatResponse chatResponse = futureResponse.get();

        // Verify the response is null
        assertNull(chatResponse, "The response should be null if the API returns a null body");
    }

    @Test
    void testGetChatResponse_ApiReturnsErrorStatus() throws Exception {
        // Prepare test data
        Message message = new Message("user", "Hello, is anyone there?");
        List<Message> messagesList = Collections.singletonList(message);

        // Mock restTemplate behavior to return an error status
        ResponseEntity<ChatResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.postForEntity(any(String.class), any(), eq(ChatResponse.class))).thenReturn(responseEntity);

        // Execute the service method
        CompletableFuture<ChatResponse> futureResponse = chatService.getChatResponse(messagesList);
        ChatResponse chatResponse = futureResponse.get();

        // Verify the response is null due to the error status
        assertNull(chatResponse, "The response should be null if the API returns an error status");
    }
}
