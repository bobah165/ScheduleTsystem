package com.util;

import com.model.SchedulDTO;
import com.model.Schedule;

import javax.inject.Named;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;


public class ScheduleMapper {

    public Schedule mapDtoToEntity (SchedulDTO schedulDTO) {
        Schedule schedule = new Schedule();
        schedule.setId(schedulDTO.getId());
        schedule.setNameStation(schedulDTO.getNameStation());
        schedule.setIdTrain(schedulDTO.getIdTrain());
        schedule.setDepartureTime(LocalTime.parse(schedulDTO.getDepartureTime()));
        schedule.setArrivalTime(LocalTime.parse(schedulDTO.getArrivalTime()));

        return schedule;
    }

    public SchedulDTO mapEntityToDto (Schedule schedule) {
        SchedulDTO schedulDTO = new SchedulDTO();
        schedulDTO.setId(schedule.getId());
        schedulDTO.setNameStation(schedule.getNameStation());
        schedulDTO.setIdTrain(schedule.getIdTrain());
        schedulDTO.setArrivalTime(schedule.getArrivalTime().toString());
        schedulDTO.setDepartureTime(schedule.getDepartureTime().toString());

        return schedulDTO;
    }
}
