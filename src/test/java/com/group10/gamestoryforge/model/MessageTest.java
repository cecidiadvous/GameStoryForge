package com.group10.gamestoryforge.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testDefaultConstructor() {
        Message message = new Message();
        assertNull(message.getRole());
        assertNull(message.getContent());
    }

    @Test
    void testParameterizedConstructor() {
        Message message = new Message("user", "Hello, this is a test message.");
        assertEquals("user", message.getRole());
        assertEquals("Hello, this is a test message.", message.getContent());
    }

    @Test
    void testSetRole() {
        Message message = new Message();
        message.setRole("assistant");
        assertEquals("assistant", message.getRole());
    }

    @Test
    void testSetContent() {
        Message message = new Message();
        message.setContent("This is the content.");
        assertEquals("This is the content.", message.getContent());
    }
}

