package com.model;



import com.service.ScheduleRESTService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ApplicationScoped
public class TimeTable {

    private List<Schedule> scheduleList;
    private String stationName;
    private Map<Integer,String> endStationName = new HashMap<>();

    @Inject
    private ScheduleRESTService scheduleRESTService;


    public void init()  {
        // logger.info("Timetable INIT started");
        scheduleList = scheduleRESTService.getTrainsByStationName(stationName);
        if (!scheduleList.isEmpty()) {
            for (int i=0; i<scheduleList.size();i++) {
                List<Schedule> allStationsByTrain = scheduleRESTService.getStationByTrainId(scheduleList.get(i).getIdTrain());

                List<Schedule> sortedSchedule = allStationsByTrain.stream()
                        .sorted(Comparator.comparing((Schedule::getDays))
                                .thenComparing(Schedule::getDepartureTime))
                        .collect(Collectors.toList());

                endStationName.put(sortedSchedule.get(sortedSchedule.size()-1).getIdTrain(),
                        sortedSchedule.get(sortedSchedule.size()-1).getNameStation());
                System.out.println();
            }
        }
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Map<Integer,String>   getEndStationName() {
        return endStationName;
    }

    public void setEndStationName( Map<Integer,String>   endStationName) {
        this.endStationName = endStationName;
    }
}
