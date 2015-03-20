package com.cinimex.learn.mdb;

/**
 * Created by nXs on 17.03.2015.
 */

import com.cinimex.learn.service.ChatService;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.logging.Logger;

@MessageDriven(name = "Chat2QueueMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/Chat2"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class Chat2QueueMDB implements MessageListener {

    private final static Logger LOGGER = Logger.getLogger(Chat2QueueMDB.class.toString());
    private final static String qName = "Chat2";

    @EJB
    private ChatService chatService;

    /**
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(Message rcvMessage) {
        TextMessage msg = null;
        try {
            if (rcvMessage instanceof TextMessage) {
                msg = (TextMessage) rcvMessage;
                chatService.putMessage(qName, msg.getText());

                LOGGER.info("Received Message from Chat2 queue: " + msg.getText());
            } else {
                LOGGER.warning("Message of wrong type in Chat2 queue: " + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}