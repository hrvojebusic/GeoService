package com.infobip.database.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class PolygonEntity {

    @Setter(AccessLevel.PRIVATE)
    @Id
    @GeneratedValue
    private int id;

    private String description;

    private Location[] coordinates;

    public Location getCoordinateAt(Integer index) {
        if(index < 0 || index >= coordinates.length) {
            throw new IndexOutOfBoundsException();
        }
        return coordinates[index];
    }
}
