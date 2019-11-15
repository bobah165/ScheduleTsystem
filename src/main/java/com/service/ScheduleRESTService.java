package com.service;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.Schedule;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ScheduleRESTService {

    private static final Logger logger = Logger.getLogger(ScheduleRESTService.class);


    public List<Schedule> getAllTrains() {

        // GET LIST BY REST

        logger.info("Rest-client started..");

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource("http://localhost:8085/api/");

        ClientResponse restResponse = webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        logger.info("restResponse = " + restResponse);

        if (restResponse.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " +
                    restResponse.getStatus());
        }

        String output = restResponse.getEntity(String.class);
        logger.info("String output = " + output);

        Type itemsListType = new TypeToken<List<Schedule>>() {}.getType();
        Gson gson = new Gson();
        List<Schedule> result = gson.fromJson(output,itemsListType);
        System.out.println();

        logger.info("Result = " + result);

        return result;
    }


    public List<Schedule> getTrainsByStationName(String stationName) {

        List<Schedule> stations = getAllTrains();
        List<Schedule> trainsFromStation = new ArrayList<>();

        for ( Schedule schedule : stations ) {
            if (schedule.getNameStation().equals(stationName)){
                trainsFromStation.add(schedule);
            }
        }
        return trainsFromStation;
    }


    public List<Schedule> getStationByTrainId(int trainId) {

        List<Schedule> stations = getAllTrains();
        List<Schedule> trainsFromStation = new ArrayList<>();

        for ( Schedule schedule : stations ) {
            if (schedule.getIdTrain() == trainId){
                trainsFromStation.add(schedule);
            }
        }
        return trainsFromStation;
    }

    public List<Schedule> getScheduleInLastDayOfWay(int trainId, int day){
        List<Schedule> scheduleList = getStationByTrainId(trainId);
        List<Schedule> trainsFromStation = new ArrayList<>();

        for (Schedule schedule: scheduleList) {
            if (schedule.getDays() == day) {
                trainsFromStation.add(schedule);
            }
        }
        return trainsFromStation;
    }

}
