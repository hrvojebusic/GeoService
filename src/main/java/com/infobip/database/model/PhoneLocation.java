package com.infobip.database.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class PhoneLocation {

    @Setter(AccessLevel.PRIVATE)
    @Id
    private String id;

    private long number;

    private Location location;

    private Date updated;

    public PhoneLocation(long number, Location location, Date updated){
        this.number = number;
        this.location = location;
        this.updated = updated;
    }
}
