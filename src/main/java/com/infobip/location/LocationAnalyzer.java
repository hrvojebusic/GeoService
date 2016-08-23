package com.infobip.location;

import com.infobip.database.model.PhoneLocation;
import com.infobip.database.repository.PhoneLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationAnalyzer {

    @Autowired
    PhoneLocationRepository phoneLocationRepository;


    public List<PhoneLocation> getPersonsForPolygon(String polygonId) {
        return null;
    }
}
