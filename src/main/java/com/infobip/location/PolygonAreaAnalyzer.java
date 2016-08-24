package com.infobip.location;

import com.infobip.database.model.PhoneLocation;
import com.infobip.database.model.PolygonalArea;
import com.infobip.database.repository.PhoneLocationRepository;
import com.infobip.database.repository.PolygonalAreaRepository;
import com.infobip.sms.gateway.SMSGateway;
import com.infobip.sms.gateway.request.GatewayRequest;
import com.infobip.sms.gateway.request.GatewayRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PolygonAreaAnalyzer {

    @Autowired
    private PolygonalAreaRepository polygonalAreaRepository;

    @Autowired
    private PhoneLocationRepository phoneLocationRepository;

    @Autowired
    private List<SMSGateway> gateways;

    @Scheduled(fixedRate = 60000)
    public void checkPolygonalAreas() {
        for (PolygonalArea polygonalArea : polygonalAreaRepository.findAll()) {

            List<PhoneLocation> usersInArea = phoneLocationRepository.findByLocationWithin(new Polygon(
                    polygonalArea.getPolygon().getPoints()
                            .stream().map(coordinate -> new Point(coordinate.getX(), coordinate.getY()))
                            .collect(Collectors.toList())));

            List<PhoneLocation> usersThatEnteredTheArea = new ArrayList<>(usersInArea);
            List<String> usersThatLeftArea = new ArrayList<>(polygonalArea.getUsers());

            // Delete all users that were previously in this area
            usersThatEnteredTheArea.removeIf(user -> polygonalArea.getUsers().contains(user.getId()));
            // Delete all users that are still in the area
            usersThatLeftArea.removeIf(user -> usersInArea.stream()
                    .filter(areaUser -> areaUser.getId().equals(user))
                    .findFirst()
                    .isPresent());

            for (SMSGateway gateway : gateways) {
                if(!usersThatEnteredTheArea.isEmpty()) {
                    gateway.push(new GatewayRequest(
                            usersThatEnteredTheArea.stream()
                                    .map(pls -> new GatewayRequestEntity(
                                            polygonalArea.getSender(),
                                            Collections.singletonList(String.valueOf(pls.getNumber())),
                                            polygonalArea.getInMessage()
                                    )).collect(Collectors.toList())));
                }

                if(!usersThatLeftArea.isEmpty()) {
                    gateway.push(new GatewayRequest(
                            usersThatLeftArea.stream()
                                    .map(pls -> new GatewayRequestEntity(
                                            polygonalArea.getSender(),
                                            Collections.singletonList(String.valueOf(phoneLocationRepository.findOne(pls).getNumber())),
                                            polygonalArea.getOutMessage())
                                    ).collect(Collectors.toList())));
                }

                polygonalArea.removeUsers();
                usersInArea.forEach(user -> polygonalArea.addUser(user.getId()));
                polygonalAreaRepository.save(polygonalArea);
            }
        }
    }
}