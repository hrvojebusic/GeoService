package com.infobip.database.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Polygon {

    @Setter(AccessLevel.PRIVATE)
    @Id
    @GeneratedValue
    private int id;

    private String description;

    private Location[] coordinates;
}
