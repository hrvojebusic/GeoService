package com.infobip.controllers.model.resource;

import com.infobip.database.model.PhoneLocation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PhoneLocationResource {

    private String id;

    private SubscriberResource subscriber = new SubscriberResource();

    private Long number;

    private LocationResource location;

    private Date updated;

    protected PhoneLocationResource() {}

    public static PhoneLocationResource from(PhoneLocation phoneLocation){
        return new PhoneLocationResource(
                phoneLocation.getId(),
                SubscriberResource.from(phoneLocation.getSubscriber()),
                phoneLocation.getNumber(),
                LocationResource.from(phoneLocation.getLocation()),
                phoneLocation.getUpdated());
    }

    public static PhoneLocation to(PhoneLocationResource phoneLocationResource){
        return new PhoneLocation(
                SubscriberResource.to(phoneLocationResource.getSubscriber()),
                phoneLocationResource.getNumber(),
                LocationResource.to(phoneLocationResource.getLocation()),
                phoneLocationResource.getUpdated());
    }
}
