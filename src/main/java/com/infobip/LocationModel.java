package com.infobip;

import com.infobip.database.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationModel {

    private Double xCoordinate;

    private Double yCoordinate;

    protected LocationModel() {
    }

    public static Location to(LocationModel locationModel) {
        return new Location(
                locationModel.getXCoordinate(),
                locationModel.getYCoordinate()
        );
    }

    public static LocationModel from(Location location) {
        return new LocationModel(
                location.getXCoordinate(),
                location.getYCoordinate()
        );
    }
}
