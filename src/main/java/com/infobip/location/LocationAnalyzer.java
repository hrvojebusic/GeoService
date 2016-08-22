package com.infobip.location;

import com.infobip.database.model.Location;
import com.infobip.database.model.PersonCoordinate;
import com.infobip.database.model.PolygonEntity;
import com.infobip.database.repository.PersonCoordinateRepository;
import com.infobip.database.repository.PolygonRepository;
import com.infobip.exception.custom.PolygonNotFoundException;
import com.vividsolutions.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Component
public class LocationAnalyzer {

    @Autowired
    PersonCoordinateRepository personCoordinateRepository;

    @Autowired
    PolygonRepository polygonRepository;

    public List<PersonCoordinate> getPersonsForPolygon(Integer polygonId) {

        PolygonEntity polygonEntity = null;
        try {
            polygonEntity = polygonRepository.getOne(polygonId);
        } catch (EntityNotFoundException e) {
            throw new PolygonNotFoundException("PolygonEntity under id " + polygonId + " does not exist.");
        }

        Coordinate[] coordinates = new Coordinate[polygonEntity.getCoordinates().length];
        for (int i = 0; i < coordinates.length; i++) {
            Location location = polygonEntity.getCoordinates()[i];
            coordinates[i] = new Coordinate(location.getX(), location.getY());
        }

        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing linearRing = new GeometryFactory().createLinearRing(coordinates);
        final Polygon polygon = new Polygon(linearRing, null, geometryFactory);

        List<PersonCoordinate> persons = personCoordinateRepository.findAll();
        List<PersonCoordinate> result = new ArrayList<PersonCoordinate>();

        for (PersonCoordinate person : persons) {
            Location location = person.getLocation();
            Geometry geometry = new GeometryFactory()
                    .createPoint(new Coordinate(location.getX(),location.getY()));

            if (polygon.contains(geometry)) {
                result.add(person);
            }
        }

        return result;
    }
}
