package com.websocket;


import com.google.gson.Gson;
import com.model.Schedule;
import com.service.ScheduleRESTService;
import org.apache.log4j.Logger;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@ServerEndpoint(value = "/websocket")
public class TimetableWebsocket {

    @Inject
    private ScheduleRESTService scheduleRESTService;

    private static final Logger logger = Logger.getLogger(TimetableWebsocket.class);

    private static Set<Session> clients =
            Collections.synchronizedSet(new HashSet<Session>());


    public TimetableWebsocket() {
    }

    public void sendMessageToBrowser(String message) {
        try {
            synchronized (clients) {
                for (Session client : clients) {
                    client.getBasicRemote().sendText(message);
                }
            }
            logger.info("send message to browser :" + message);

        } catch (IOException e) {
            logger.warn("Exception : " + e);
            e.printStackTrace();
        }
    }


    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        logger.info("new session opened, session = " + session + ", session id = " + session.getId());

        clients.add(session);
        logger.info("Count of clients = " + clients.size());

        // UPDATE GUI
        try {

            List<Schedule> schedules = scheduleRESTService.getAllTrains();
            String strTimetable = new Gson().toJson(schedules);
            logger.info("Mapped object :" + strTimetable);
            this.sendMessageToBrowser(strTimetable);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("message received : " + message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.warn("Error occurred: " + throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.warn("Session : " + session + " - closed: " + closeReason);
        clients.remove(session);
    }


}
