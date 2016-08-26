package com.infobip.controllers.model.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class LocationResource {

    @NotNull
    private Double x;

    @NotNull
    private Double y;

    protected LocationResource() {
    }

    public static GeoJsonPoint to(LocationResource locationModel) {
        return new GeoJsonPoint(
                locationModel.getX(),
                locationModel.getY()
        );
    }

    public static LocationResource from(GeoJsonPoint location) {
        return new LocationResource(
                location.getX(),
                location.getY()
        );
    }
}
