package com.group10.gamestoryforge.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatResponseTest {

    private ChatResponse chatResponse;
    private ChatResponse.Choice choice;
    private ChatResponse.Choice.Message message;

    @BeforeEach
    void setUp() {
        chatResponse = new ChatResponse();
        choice = new ChatResponse.Choice();
        message = new ChatResponse.Choice.Message();

        message.setRole("user");
        message.setContent("Hello, how are you?");
        choice.setIndex(0);
        choice.setMessage(message);

        List<ChatResponse.Choice> choices = new ArrayList<>();
        choices.add(choice);
        chatResponse.setChoices(choices);
    }

    @Test
    void testChatResponseCreation() {
        assertNotNull(chatResponse);
        assertNotNull(chatResponse.getChoices());
        assertEquals(1, chatResponse.getChoices().size());
    }

    @Test
    void testChoiceProperties() {
        ChatResponse.Choice retrievedChoice = chatResponse.getChoices().get(0);
        assertEquals(0, retrievedChoice.getIndex());
        assertNotNull(retrievedChoice.getMessage());
    }

    @Test
    void testMessageProperties() {
        ChatResponse.Choice.Message retrievedMessage = chatResponse.getChoices().get(0).getMessage();
        assertEquals("user", retrievedMessage.getRole());
        assertEquals("Hello, how are you?", retrievedMessage.getContent());
    }

    @Test
    void testSetChoices() {
        ChatResponse.Choice newChoice = new ChatResponse.Choice();
        ChatResponse.Choice.Message newMessage = new ChatResponse.Choice.Message();
        newMessage.setRole("assistant");
        newMessage.setContent("I am fine, thank you!");
        newChoice.setIndex(1);
        newChoice.setMessage(newMessage);

        List<ChatResponse.Choice> newChoices = new ArrayList<>();
        newChoices.add(newChoice);
        chatResponse.setChoices(newChoices);

        assertEquals(1, chatResponse.getChoices().size());
        assertEquals("assistant", chatResponse.getChoices().get(0).getMessage().getRole());
        assertEquals("I am fine, thank you!", chatResponse.getChoices().get(0).getMessage().getContent());
    }
}
