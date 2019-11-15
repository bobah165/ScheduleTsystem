package com.listener;

import com.google.gson.Gson;
import com.model.SchedulDTO;
import com.model.Schedule;
import com.model.TimeTable;
import com.service.ScheduleRESTService;
import com.util.ScheduleMapper;
import com.websocket.TimetableWebsocket;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.*;
import java.util.stream.Collectors;


@Singleton
@MessageDriven(name = "JCG_QUEUE", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "JCG_QUEUE"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class MessageReciver implements MessageListener {

    @Inject
    private TimeTable timeTable;

    @Inject
    private ScheduleRESTService scheduleRESTService;

 //   private final static Logger LOGGER = LoggerFactory.getLogger(MessageReciver.class.toString());

    /**
     * @see MessageListener#onMessage(Message)
     */
    @Override
    public void onMessage(Message rcvMessage) {
        TextMessage msg = null;
        try {
            if (rcvMessage instanceof TextMessage) {
                msg = (TextMessage) rcvMessage;
           //     LOGGER.info("Received Message from queue: " + msg.getText());
                if (msg.getText().equals("schedule")) {
                    try {
                        List<Schedule> scheduleList = scheduleRESTService.getTrainsByStationName(timeTable.getStationName());
                        Map<Integer,String> endStationName = new HashMap<>();
                        List<SchedulDTO> schedulDTOS = new ArrayList<>();
                        if (!scheduleList.isEmpty()) {
                            for (int i = 0; i < scheduleList.size(); i++) {
                                List<Schedule> allStationsByTrain = scheduleRESTService.getStationByTrainId(scheduleList.get(i).getIdTrain());

                                List<Schedule> sortedSchedule = allStationsByTrain.stream()
                                        .sorted(Comparator.comparing((Schedule::getDays))
                                                .thenComparing(Schedule::getDepartureTime))
                                        .collect(Collectors.toList());

                                endStationName.put(sortedSchedule.get(sortedSchedule.size() - 1).getIdTrain(),
                                        sortedSchedule.get(sortedSchedule.size() - 1).getNameStation());
                                System.out.println();
                            }
                        }

//                        List<Schedule> schedules = scheduleRESTService.getTrainsByStationName(timeTable.getStationName());
//                        List<SchedulDTO> schedulDTOS = new ArrayList<>();
                          ScheduleMapper scheduleMapper = new ScheduleMapper();
                                for (Schedule schedule : scheduleList) {
                                    schedule.setNameStation(endStationName.get(schedule.getIdTrain()));
                                    schedulDTOS.add(scheduleMapper.mapEntityToDto(schedule));
                                }
                                TimetableWebsocket timetableWebsocket = new TimetableWebsocket();
                                timetableWebsocket.sendMessageToBrowser(new Gson().toJson(schedulDTOS));

                    } catch (Exception e) {
                        System.out.println("wrong");
                    }

                }
            } else {
           //     LOGGER.error("Message of wrong type: "
             //           + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
