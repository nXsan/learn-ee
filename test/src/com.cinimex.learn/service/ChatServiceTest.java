package com.cinimex.learn.service;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ChatServiceTest  {

    private ChatService chatService;

    final String testQueue = "TestChat";

    @Before
    public void setUpData() {
        chatService = new ChatService();
        chatService.clear();
    }

    @Test
    public void testGetAndPutMessages() throws Exception {
        final String testMessage = "Test message";

        chatService.putMessage(testQueue, testMessage);
        ArrayList<String> messages = chatService.getMessages(testQueue);
        String message = messages.get(0);

        assertEquals(testMessage, message);
    }

}