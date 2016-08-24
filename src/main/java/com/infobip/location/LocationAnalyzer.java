package com.infobip.location;

import com.infobip.controllers.model.MessageRequest;
import com.infobip.controllers.model.MessageRequestReport;
import com.infobip.controllers.model.PhoneLocationResource;
import com.infobip.controllers.model.PolygonResource;
import com.infobip.database.repository.PhoneLocationRepository;
import com.infobip.gateway.Gateway;
import com.infobip.gateway.GatewayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationAnalyzer {

    @Autowired
    private PhoneLocationRepository phoneLocationRepository;

    @Autowired
    private List<Gateway> gateways;

    public List<PhoneLocationResource> getPersonsForPolygon(PolygonResource polygonResource) {
        return phoneLocationRepository
                .findByLocationWithin(PolygonResource.to(polygonResource))
                .stream()
                .map(phoneLocation -> PhoneLocationResource.from(phoneLocation))
                .collect(Collectors.toList());
    }

    public Collection<MessageRequestReport> notifyPersonsForPolygon(MessageRequest messageRequest) {
        List<PhoneLocationResource> result = getPersonsForPolygon(messageRequest.getPolygon());
        List<MessageRequestReport> collection = new ArrayList<>();

        for(Gateway gateway : gateways) {
                collection.add(gateway.push(
                        result  .stream()
                                .map(plr -> new GatewayRequest(
                                        messageRequest.getSender(),
                                        String.valueOf(plr.getNumber()),
                                        messageRequest.getMessage()
                                ))
                                .collect(Collectors.toList()))
            );
        }

        return collection;
    }
}
