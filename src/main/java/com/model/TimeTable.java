package com.model;


import com.google.gson.Gson;
import com.service.ScheduleRESTService;
import com.websocket.TimetableWebsocket;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;

@Named
@ApplicationScoped
public class TimeTable {

    private List<Schedule> scheduleList;
    private String stationNamme;

    @Inject
    private ScheduleRESTService scheduleRESTService;


    public void init()  {
        // logger.info("Timetable INIT started");
           // scheduleList = scheduleRESTService.getAllTrains();
        scheduleList = scheduleRESTService.getTrainsByStationName(stationNamme);
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public String getStationNamme() {
        return stationNamme;
    }

    public void setStationNamme(String stationNamme) {
        this.stationNamme = stationNamme;
    }
}
