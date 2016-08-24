package com.infobip.controllers.model.resource;

import com.infobip.database.model.PhoneLocation;
import com.infobip.database.model.Subscriber;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.Date;

@Data
@AllArgsConstructor
public class PhoneLocationResource {

    private String id;

    private Subscriber subscriber;

    private Long number;

    private LocationResource location;

    private Date updated;

    protected PhoneLocationResource() {}

    public static PhoneLocationResource from(PhoneLocation phoneLocation){
        return new PhoneLocationResource(
                phoneLocation.getId(),
                phoneLocation.getSubscriber(),
                phoneLocation.getNumber(),
                new LocationResource(phoneLocation.getLocation().getX(), phoneLocation.getLocation().getY()),
                phoneLocation.getUpdated());
    }

    public static PhoneLocation to(PhoneLocationResource resource){
        return new PhoneLocation(
                resource.getSubscriber(),
                resource.getNumber(),
                new GeoJsonPoint(resource.getLocation().getX(), resource.getLocation().getY()),
                resource.getUpdated());
    }
}
