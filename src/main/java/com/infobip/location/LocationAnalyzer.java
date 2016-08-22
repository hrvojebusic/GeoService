package com.infobip.location;

import com.infobip.database.model.Location;
import com.infobip.database.model.PhoneLocation;
import com.infobip.database.model.PolygonalArea;
import com.infobip.database.repository.PhoneLocationRepository;
import com.infobip.database.repository.PolygonRepository;
import com.infobip.exception.custom.PolygonNotFoundException;
import com.vividsolutions.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LocationAnalyzer {

    @Autowired
    PhoneLocationRepository phoneLocationRepository;

    @Autowired
    PolygonRepository polygonRepository;

    public List<PhoneLocation> getPersonsForPolygon(String polygonId) {

        PolygonalArea polygonalArea = null;
        try {
            polygonalArea = polygonRepository.findOne(polygonId);
        } catch (Exception e) {
            throw new PolygonNotFoundException("PolygonalArea under id " + polygonId + " does not exist.");
        }

        Polygon polygon = PolygonalArea.to(polygonalArea);
        List<PhoneLocation> persons = phoneLocationRepository.findAll();
        List<PhoneLocation> result = new ArrayList<PhoneLocation>();

        for (PhoneLocation person : persons) {
            Location location = person.getLocation();
            Geometry geometry = new GeometryFactory()
                    .createPoint(new Coordinate(location.getXCoordinate(), location.getYCoordinate()));

            if (polygon.contains(geometry)) {
                result.add(person);
            }
        }

        return result;
    }
}
