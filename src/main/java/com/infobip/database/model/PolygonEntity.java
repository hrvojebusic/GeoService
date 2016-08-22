package com.infobip.database.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Data
public class PolygonEntity {

    @Setter(AccessLevel.PRIVATE)
    @Id
    private String id;

    private String description;

    private Location[] coordinates;

    public Location getCoordinateAt(Integer index) {
        if (index < 0 || index >= coordinates.length) {
            throw new IndexOutOfBoundsException();
        }
        return coordinates[index];
    }

    public static Polygon to(final PolygonEntity polygonEntity) {

        int numberOfCoordinates = polygonEntity.getCoordinates().length;

        Coordinate[] coordinates = new Coordinate[numberOfCoordinates];
        for (int i = 0; i < numberOfCoordinates; i++) {
            Location location = polygonEntity.getCoordinateAt(i);
            coordinates[i] = new Coordinate(location.getX(), location.getY());
        }

        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing linearRing = new GeometryFactory().createLinearRing(coordinates);
        Polygon polygon = new Polygon(linearRing, null, geometryFactory);

        return polygon;
    }
}
