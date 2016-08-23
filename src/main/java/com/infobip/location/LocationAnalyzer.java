package com.infobip.location;

import com.infobip.controllers.model.PhoneLocationResource;
import com.infobip.controllers.model.PolygonResource;
import com.infobip.database.repository.PhoneLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationAnalyzer {

    @Autowired
    PhoneLocationRepository phoneLocationRepository;

    public List<PhoneLocationResource> getPersonsForPolygon(PolygonResource polygonResource) {
        return phoneLocationRepository
                .findByLocationWithin(PolygonResource.to(polygonResource))
                .stream()
                .map(phoneLocation -> PhoneLocationResource.from(phoneLocation))
                .collect(Collectors.toList());
    }
}
