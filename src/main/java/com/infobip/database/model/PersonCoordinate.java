package com.infobip.database.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class PersonCoordinate {

    @Setter(AccessLevel.PRIVATE)
    @Id
    @GeneratedValue
    private int id;

    private long number;

    private Location location;

    private Date updated;
}
