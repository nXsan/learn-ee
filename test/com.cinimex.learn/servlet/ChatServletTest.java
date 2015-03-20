package com.cinimex.learn.servlet;

import com.cinimex.learn.service.ChatService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import javax.jms.Destination;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.not;

public class ChatServletTest extends Mockito {

    final String testQueue = "TestChat";

    private ChatService chatService;

    @Before
    public void setUpData() {
        chatService = new ChatService();
        chatService.clear();
    }

    @Test
    public void testDoPost() throws Exception {
        final String testMessage = "test1";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mock doPost, but we can't put message in JMS because JMS Query on server side
        // TODO: need testing JMS
        when(request.getParameter("message")).thenReturn(testMessage);

        class MockChatServlet extends ChatServlet {
            @Override
            protected void sendMessage(Destination destination, String msg) {
                chatService.putMessage(testQueue, msg);
            }
        };
        ChatServlet servlet = new MockChatServlet();

        // Check redirect after post
        doThrow(new RuntimeException()).when(response).sendRedirect(Matchers.any(String.class));
        doNothing().when(response).sendRedirect(Matchers.eq("chat"));

        servlet.doPost(request, response);
        String message = chatService.getMessages(testQueue).get(0);

        assertEquals(testMessage, message);
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