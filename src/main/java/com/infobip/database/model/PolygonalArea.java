package com.infobip.database.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class PolygonalArea {

    @Setter(AccessLevel.PRIVATE)
    @Id
    private String id;

    private String description;

    private int userId;

    private Location[] coordinates;

    public PolygonalArea(String description, int userId, Location[] coordinates) {
        this.description = description;
        this.userId = userId;
        this.coordinates = coordinates;
    }

    protected PolygonalArea() {}

    public Location getCoordinateAt(Integer index) {
        if (index < 0 || index >= coordinates.length) {
            throw new IndexOutOfBoundsException();
        }
        return coordinates[index];
    }

    public static Polygon to(final PolygonalArea polygonalArea) {

        int numberOfCoordinates = polygonalArea.getCoordinates().length;

        Coordinate[] coordinates = new Coordinate[numberOfCoordinates];
        for (int i = 0; i < numberOfCoordinates; i++) {
            Location location = polygonalArea.getCoordinateAt(i);
            coordinates[i] = new Coordinate(location.getXCoordinate(), location.getYCoordinate());
        }

        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing linearRing = new GeometryFactory().createLinearRing(coordinates);
        Polygon polygon = new Polygon(linearRing, null, geometryFactory);

        return polygon;
    }
}
