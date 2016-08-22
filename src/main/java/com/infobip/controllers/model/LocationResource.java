package com.infobip.controllers.model;

import com.infobip.database.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationResource {

    private Double xCoordinate;

    private Double yCoordinate;

    protected LocationResource() {
    }

    public static Location to(LocationResource locationModel) {
        return new Location(
                locationModel.getXCoordinate(),
                locationModel.getYCoordinate()
        );
    }

    public static LocationResource from(Location location) {
        return new LocationResource(
                location.getXCoordinate(),
                location.getYCoordinate()
        );
    }
}
