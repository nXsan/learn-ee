package com.cinimex.learn.servlet;

import com.cinimex.learn.service.ChatService;

import javax.annotation.Resource;
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

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:/jms/queue/Chat")
    private Queue chatQueue;

    @EJB
    private ChatService chatService;
    
    final private Integer autoRefresh = 10;

    /**
     * Get messages and show them
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.write("<h1>Example demonstrates the use of <strong>Servlet</strong>, <strong>JMS 2.0</strong> and <strong>EJB 3.2 Message-Driven Bean</strong> in WildFly 8.</h1>");
        if (autoRefresh > 0) {
            response.addHeader("Refresh", autoRefresh.toString());
            out.write("<p>Auto refresh every " + autoRefresh + " seconds</p>");
        }
        try {
            String qName = "default";
            if (chatQueue != null) {
                final Destination destination = chatQueue;
                qName = chatQueue.getQueueName();

                if (destination != null) {
                    out.write("<p>Sending messages to <em>" + destination + "</em></p>");
                }
                out.write("<h3>Following messages in query: " + qName + "</h3>");
            }

            // Check on null for testing purpose
            if (chatService != null) {
                chatService.printMessages(out, qName);
            }
            else {
                new ChatService().printMessages(out, qName);
            }

            // Show form for sending new message
            out.write("<h3>Form for sending message:</h3>");
            out.write("<form method='post'>");
            out.write("<label for='msg'>Message</label><input id='msg' type='text' name='message'>");
            out.write("<input type='submit' value='Send' /></form>");

            // Buttons for refreshing messages (do this method)
            out.write("<a href='chat'><button>Refresh</button></a>");
        }
        catch (Exception e) {
            out.write("Runtime exception");
            e.printStackTrace();
        }

    }

    /**
     * Sending JMS message in query and after that return in chat page
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Destination destination = chatQueue;
        String msg = request.getParameter("message");

        sendMessage(destination, msg);

        response.sendRedirect("chat");
    }

    protected void sendMessage(Destination destination, String msg) {
        if (msg != null && !msg.equals("") && destination != null) {
            context.createProducer().send(destination, msg);
        }
    }


}