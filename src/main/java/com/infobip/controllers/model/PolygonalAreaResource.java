package com.infobip.controllers.model;

import com.infobip.database.model.Location;
import com.infobip.database.model.PolygonalArea;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PolygonalAreaResource {

    private String id;

    private String description;

    private Location[] coordinates;

    protected PolygonalAreaResource() {}

    public static PolygonalArea to(PolygonalAreaResource polygonalAreaResource) {
        return new PolygonalArea(
                polygonalAreaResource.getId(),
                polygonalAreaResource.getDescription(),
                polygonalAreaResource.getCoordinates()
        );
    }

    public static PolygonalAreaResource from(PolygonalArea polygonalArea) {
        return new PolygonalAreaResource(
                polygonalArea.getId(),
                polygonalArea.getDescription(),
                polygonalArea.getCoordinates()
        );
    }
}
