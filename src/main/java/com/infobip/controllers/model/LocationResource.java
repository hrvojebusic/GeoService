package com.infobip.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Data
@AllArgsConstructor
public class LocationResource {

    private Double xCoordinate;

    private Double yCoordinate;

    protected LocationResource() {
    }

    public static GeoJsonPoint to(LocationResource locationModel) {
        return new GeoJsonPoint(
                locationModel.getXCoordinate(),
                locationModel.getYCoordinate()
        );
    }

    public static LocationResource from(GeoJsonPoint location) {
        return new LocationResource(
                location.getX(),
                location.getY()
        );
    }
}
