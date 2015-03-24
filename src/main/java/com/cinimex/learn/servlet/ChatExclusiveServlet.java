package com.cinimex.learn.servlet;

import com.cinimex.learn.service.ChatService;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/chat_exclusive")
public class ChatExclusiveServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    @Inject
    private JMSContext context;

    @EJB
    private ChatService chatService;

    final private Integer autoRefresh = 10;

    /**
     * Get messages and show them
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.write("<h1>Example demonstrates the use of <strong>JMS 2.0</strong> and <strong>EJB 3.2 Message-Driven Bean</strong> in WildFly 8.</h1>");
        if (autoRefresh > 0) {
            response.addHeader("Refresh", autoRefresh.toString());
            out.write("<p>Auto refresh every " + autoRefresh + " seconds</p>");
        }
        try {
            String sessionId = request.getRequestedSessionId();
            Queue queue;

            // Queue from pull or list free queue
            if (sessionId != null) {
                queue = chatService.getQueueBySessionId(sessionId);
            }
            else {
                // If not receive session from browser
                throw new RuntimeException();
            }

            out.write("Session: " + request.getRequestedSessionId());

            final Destination destination = queue;
            String qName = queue.getQueueName();

            if (destination != null) {
                out.write("<p>Sending messages to <em>" + destination + "</em></p>");
            }
            out.write("<h3>Following messages in query: " + qName + "</h3>");

            // Check on null for testing purpose
            if (chatService != null) {
                chatService.printMessages(out, qName);
            }
            else {
                new ChatService().printMessages(out, qName);
            }

            // Show form for sending new message
            out.write("<h3>Form for sending message</h3>");
            out.write("<form method='post'>");
            out.write("<p>Query for sending:</p>");
            int i = 0;
            for (Queue q : chatService.getAllQueues()) {
                out.write("<input type=\"radio\" name=\"queryChose\" value=\"" + i++ + "\">"+ q.getQueueName()+"<br/>");
            }
//            out.write("<input type=\"radio\" name=\"queryChose\" value=\"2\">Chat2<br/><br/>");
            out.write("<label for='msg'>Message: </label><input id='msg' type='text' name='message'>");
            out.write("<input type='submit' value='Send' /></form>");

            // Buttons for refreshing messages (do this method)
            out.write("<a href='chat_exclusive'><button>Refresh</button></a>");
        }
        catch (Exception e) {
            out.write("Runtime exception for " + request.getRequestedSessionId());
            e.printStackTrace();
        }
    }

    /**
     * Sending JMS message in chosen query and after that send GET in chat page
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String choseQueue = request.getParameter("queryChose");

        // Get queue by index in queue list
        Destination destination = null;
        Integer i = 0;
        for (Queue q: chatService.getAllQueues()) {
            if (choseQueue.equals(i.toString())) {
                destination = q;
            }
            i++;
        }

        if (destination != null) {
            String msg = request.getParameter("message");
            sendMessage(destination, msg);
        }
        else {
            throw new RuntimeException();
        }


        response.sendRedirect("chat_exclusive");
    }

    protected void sendMessage(Destination destination, String msg) {
        if (msg != null && !msg.equals("") && destination != null) {
            context.createProducer().send(destination, msg);
        }
    }



}