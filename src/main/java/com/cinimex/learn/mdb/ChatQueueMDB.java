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

@MessageDriven(name = "ChatQueueMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/Chat"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class ChatQueueMDB implements MessageListener {

    private final static Logger LOGGER = Logger.getLogger(ChatQueueMDB.class.toString());
    private final static String qName = "Chat";

    @EJB
    private ChatService chatService;

    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message rcvMessage) {
        TextMessage msg = null;
        try {
            if (rcvMessage instanceof TextMessage) {
                msg = (TextMessage) rcvMessage;

                chatService.putMessage(qName, msg.getText());

                LOGGER.info("Received Message from Chat queue: " + msg.getText());
            } else {
                LOGGER.warning("Message of wrong type in Chat queue: " + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}