package com.infobip.controllers.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class PolygonResource {

    private List<LocationResource> coordinates;

    protected PolygonResource() {}
    public static Polygon to(PolygonResource polygonResource) {
        return new Polygon(
                polygonResource
                        .getCoordinates()
                        .stream()
                        .map(lr -> new Point(lr.getXCoordinate(), lr.getYCoordinate()))
                        .collect(Collectors.toList()));
    }

    public static PolygonResource from(Polygon polygon) {
        return new PolygonResource(
                polygon
                        .getPoints()
                        .stream()
                        .map(point -> new LocationResource(point.getX(),point.getY()))
                        .collect(Collectors.toList())
        );
    }
}
