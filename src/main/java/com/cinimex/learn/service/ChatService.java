package com.cinimex.learn.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.jms.Queue;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static javax.ejb.ConcurrencyManagementType.BEAN;

/**
 * Created by nXs on 18.03.2015.
 */
@Startup
@Singleton
@ConcurrencyManagement(BEAN)
public class ChatService {

    private static Map<String, ArrayList<String>> mapMessages = new HashMap<>();

    @Resource(lookup = "java:/jms/queue/Chat")
    private Queue chatQueue;

    @Resource(lookup = "java:/jms/queue/Chat2")
    private Queue chatQueue2;

    private static Map<String, Queue> sessionPull = new HashMap<>(); // User to Query map
    private static ArrayList<Queue> availableQueue = new ArrayList(); // List available query
    private static ArrayList<Queue> allQueue = new ArrayList(); // List all query

    public ArrayList<Queue> getAllQueues() {
        return allQueue;
    }

    @Lock(LockType.READ)
    public ArrayList<String> getMessages(String destination) {
        if (mapMessages.containsKey(destination)) {
            return mapMessages.get(destination);
        }
        else {
            return new ArrayList<>();
        }
    }

    @Lock(LockType.WRITE)
    public void putMessage(String destination, String msg) {
        ArrayList<String> messages;
        if (!mapMessages.containsKey(destination)) {
            messages = new ArrayList();
        }
        else {
            messages = mapMessages.get(destination);
        }
        messages.add(msg);
        mapMessages.put(destination, messages);
    }

    @Lock(LockType.WRITE)
    public void clear() {
        mapMessages = new HashMap<>();
    }

    @Lock(LockType.READ)
    public void printMessages(PrintWriter out, String queue) {
        int numMsgs = 0;

        ArrayList<String> messages;
        messages = getMessages(queue);

        // Show messages from memory
        for(String msg : messages) {
            numMsgs++;
            out.write("<p>Message " + numMsgs + ": " + msg + "</p>");
        }
        out.write("<p>Count of messages = " + numMsgs + "</p>");
        out.write("<p><i>Go to your WildFly Server console or Server log to see the result of messages processing</i></p>");
    }

    /**
     * Get Queue from pool (if user already have queue) or availableQueue (if user not have queue)
     * @param sessionId
     * @return
     * @throws RuntimeException if not have queue for user
     */
    public Queue getQueueBySessionId(String sessionId) throws RuntimeException {
        Queue queue;
        if (sessionPull.containsKey(sessionId)) {
            queue = sessionPull.get(sessionId);
        }
        else {
            if (availableQueue.size() > 0) {
                queue = availableQueue.remove(0);
                sessionPull.put(sessionId, queue);
            }
            else {
                throw new RuntimeException();
            }
        }
        return queue;
    }

    @PostConstruct
    protected void init() {
        allQueue.add(chatQueue);
        allQueue.add(chatQueue2);

        // In start server all queue available
        for (Queue q : allQueue) {
            availableQueue.add(q);
        }
    }

}
