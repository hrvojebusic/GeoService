package com.infobip.database.model;

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
        if(index < 0 || index >= coordinates.length) {
            throw new IndexOutOfBoundsException();
        }
        return coordinates[index];
    }
}
