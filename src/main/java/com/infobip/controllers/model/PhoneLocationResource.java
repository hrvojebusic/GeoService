package com.infobip.controllers.model;

import com.infobip.database.model.PhoneLocation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.Date;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class PhoneLocationResource {

    private String id;

    private Long number;

    private LocationResource location;

    private Date updated;

    protected PhoneLocationResource() {}

    public static PhoneLocationResource from(PhoneLocation phoneLocation){
        return new PhoneLocationResource(phoneLocation.getId(),
                phoneLocation.getNumber(),
                new LocationResource(phoneLocation.getLocation().getX(), phoneLocation.getLocation().getY()),
                phoneLocation.getUpdated());
    }

    public static PhoneLocation to(PhoneLocationResource resource){
        return new PhoneLocation(resource.getNumber(),
                new GeoJsonPoint(resource.getLocation().getXCoordinate(), resource.getLocation().getYCoordinate()),
                resource.getUpdated());
    }

}
