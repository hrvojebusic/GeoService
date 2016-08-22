package com.infobip.location;

import com.infobip.database.model.Location;
import com.infobip.database.model.PersonCoordinate;
import com.infobip.database.model.PolygonalArea;
import com.infobip.database.repository.PersonCoordinateRepository;
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
    PersonCoordinateRepository personCoordinateRepository;

    @Autowired
    PolygonRepository polygonRepository;

    public List<PersonCoordinate> getPersonsForPolygon(String polygonId) {

        PolygonalArea polygonalArea = null;
        try {
            polygonalArea = polygonRepository.findOne(polygonId);
        } catch (Exception e) {
            throw new PolygonNotFoundException("PolygonalArea under id " + polygonId + " does not exist.");
        }

        Polygon polygon = PolygonalArea.to(polygonalArea);
        List<PersonCoordinate> persons = personCoordinateRepository.findAll();
        List<PersonCoordinate> result = new ArrayList<PersonCoordinate>();

        for (PersonCoordinate person : persons) {
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
