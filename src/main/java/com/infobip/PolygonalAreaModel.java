package com.infobip;

import com.infobip.database.model.Location;
import com.infobip.database.model.PolygonalArea;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PolygonalAreaModel {

    private String id;

    private String description;

    private Location[] coordinates;

    protected PolygonalAreaModel() {}

    public static PolygonalArea to(PolygonalAreaModel polygonalAreaModel) {
        return new PolygonalArea(
                polygonalAreaModel.getId(),
                polygonalAreaModel.getDescription(),
                polygonalAreaModel.getCoordinates()
        );
    }

    public static PolygonalAreaModel from(PolygonalArea polygonalArea) {
        return new PolygonalAreaModel(
                polygonalArea.getId(),
                polygonalArea.getDescription(),
                polygonalArea.getCoordinates()
        );
    }
}
