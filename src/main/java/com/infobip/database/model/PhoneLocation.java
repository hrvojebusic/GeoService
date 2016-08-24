package com.infobip.database.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.Date;

@Data
public class PhoneLocation {

    @Setter(AccessLevel.PRIVATE)
    @Id
    private String id;

    private Subscriber subscriber;

    private Long number;

    private GeoJsonPoint location;

    private Date updated;

    protected PhoneLocation() {
    }

    public PhoneLocation(Subscriber subscriber, Long number, GeoJsonPoint location, Date updated) {
        this.subscriber = subscriber;
        this.number = number;
        this.location = location;
        this.updated = updated;
    }
}