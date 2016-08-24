package com.infobip.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PolygonResource {

    @Size(min=4)
    private List<LocationResource> coordinates;

    protected PolygonResource() {}
    public static Polygon to(PolygonResource polygonResource) {
        return new Polygon(
                polygonResource
                        .getCoordinates()
                        .stream()
                        .map(locationResource -> new Point(locationResource.getX(), locationResource.getY()))
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
