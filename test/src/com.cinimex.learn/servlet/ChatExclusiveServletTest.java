package com.cinimex.learn.servlet;

import com.cinimex.learn.service.ChatService;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ChatExclusiveServletTest {

    final String testQueue = "TestChatExclusive";

    private ChatService chatService;

    @Before
    public void setUpData() {
        chatService = new ChatService();
        chatService.clear();
    }

    @Test
    public void testDoGet() throws Exception {
        final String testMessage = "test1";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ChatServlet servlet = spy(new ChatServlet());

        when(response.getWriter()).thenReturn(new PrintWriter(System.out));
        chatService.putMessage(testQueue, testMessage);

        servlet.doGet(request, response);

        assertEquals(chatService.getMessages(testQueue).size(), 1);
    }
}