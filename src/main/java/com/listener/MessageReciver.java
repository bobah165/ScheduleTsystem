package com.listener;

import com.google.gson.Gson;
import com.model.Schedule;
import com.service.ScheduleRESTService;
import com.websocket.TimetableWebsocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;


@Singleton
@MessageDriven(name = "JCG_QUEUE", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "JCG_QUEUE"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class MessageReciver implements MessageListener {

    @Inject
    private ScheduleRESTService scheduleRESTService;

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageReciver.class.toString());

    /**
     * @see MessageListener#onMessage(Message)
     */
    @Override
    public void onMessage(Message rcvMessage) {
        TextMessage msg = null;
        try {
            if (rcvMessage instanceof TextMessage) {
                msg = (TextMessage) rcvMessage;
                LOGGER.info("Received Message from queue: " + msg.getText());
                if (msg.getText().equals("schedule")) {
                    try {
                        List<Schedule> schedules = scheduleRESTService.getAllTrains();
                        TimetableWebsocket timetableWebsocket = new TimetableWebsocket();
                        timetableWebsocket.sendMessageToBrowser(new Gson().toJson(schedules));
                    } catch (Exception e) {
                        System.out.println("wrong");
                    }

                }
            } else {
                LOGGER.error("Message of wrong type: "
                        + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
