package com.infobip.location;

import com.infobip.database.model.PhoneLocation;
import com.infobip.database.model.PolygonalArea;
import com.infobip.database.repository.PhoneLocationRepository;
import com.infobip.database.repository.PolygonalAreaRepository;
import com.infobip.gateway.sms.SMSGateway;
import com.infobip.gateway.sms.request.GatewayRequest;
import com.infobip.gateway.sms.request.GatewayRequestEntity;
import com.infobip.gateway.sms.response.GatewayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PolygonAreaAnalyzer {

    @Autowired
    private PolygonalAreaRepository polygonalAreaRepository;

    @Autowired
    private PhoneLocationRepository phoneLocationRepository;

    @Autowired
    private List<SMSGateway> gateways;

    public void checkPolygonalAreas() {

        polygonalAreaRepository.findAll().addCallback(polygonalAreas -> {
            for (PolygonalArea polygonalArea : polygonalAreas) {

                phoneLocationRepository.findByLocationWithin(new Polygon(
                        polygonalArea.getPolygon().getPoints()
                                .stream().map(coordinate -> new Point(coordinate.getX(), coordinate.getY()))
                                .collect(Collectors.toList())))
                        .addCallback(usersInArea -> {

                            List<PhoneLocation> usersThatEnteredTheArea = new ArrayList<>(usersInArea);
                            List<String> usersThatLeftArea = new ArrayList<>(polygonalArea.getUsers());

                            // Delete all users that were previously in this area
                            usersThatEnteredTheArea.removeIf(user -> polygonalArea.getUsers().contains(user.getId()));
                            // Delete all users that are still in the area
                            final List<PhoneLocation> finalUsersInArea = usersInArea;
                            usersThatLeftArea.removeIf(user -> finalUsersInArea.stream()
                                    .filter(areaUser -> areaUser.getId().equals(user))
                                    .findFirst()
                                    .isPresent());

                            for (SMSGateway gateway : gateways) {
                                if (!usersThatEnteredTheArea.isEmpty()) {
                                    gateway.push(new GatewayRequest(
                                                    usersThatEnteredTheArea
                                                            .stream()
                                                            .map(pls -> new GatewayRequestEntity(
                                                                    polygonalArea.getSender(),
                                                                    Collections.singletonList(String.valueOf(pls.getNumber())),
                                                                    polygonalArea.getInMessage()
                                                            )).collect(Collectors.toList())),
                                            new ListenableFutureCallback<GatewayResponse>() {
                                                @Override
                                                public void onFailure(Throwable throwable) {
                                                    log.info("Something went wrong :(", throwable);
                                                }

                                                @Override
                                                public void onSuccess(GatewayResponse gatewayResponse) {
                                                }
                                            });
                                }
                                List<GatewayRequestEntity> gatewayRequestEntities = new ArrayList<>();
                                usersThatLeftArea.forEach(user -> {
                                    phoneLocationRepository.findById(user).addCallback(phoneLocation1 -> {
                                        if (phoneLocation1 != null) {
                                            gatewayRequestEntities.add(new GatewayRequestEntity(
                                                    polygonalArea.getSender(),
                                                    Collections.singletonList(String.valueOf(phoneLocation1.getNumber())),
                                                    polygonalArea.getOutMessage()));
                                        }
                                        if (!gatewayRequestEntities.isEmpty()) {
                                            gateway.push(new GatewayRequest(gatewayRequestEntities), new ListenableFutureCallback<GatewayResponse>() {
                                                @Override
                                                public void onFailure(Throwable throwable) {
                                                    log.info("Something went wrong :(", throwable);
                                                }

                                                @Override
                                                public void onSuccess(GatewayResponse gatewayResponse) {
                                                }
                                            });
                                        }
                                    }, e -> log.info("Something went wrong :(", e));
                                });
                            }
                            polygonalArea.removeUsers();
                            usersInArea.forEach(user -> polygonalArea.addUser(user.getId()));
                            polygonalAreaRepository.save(polygonalArea);
                        }, throwable -> {
                            log.info("Something went wrong :(", throwable);
                        });
            }
        }, throwable -> {
            log.info("Something went wrong :(", throwable);
        });

    }
}
