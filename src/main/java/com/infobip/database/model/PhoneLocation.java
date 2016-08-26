package com.infobip.database.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class PhoneLocation {

    @Setter(AccessLevel.PRIVATE)
    @Id
    private String id;

    private Subscriber subscriber = new Subscriber();

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

    public boolean matchesAttributes(MultiValueMap<String, String> attributes) {
        Map<String, String> subscriberAttributes = subscriber.getAttributes();

        for (Map.Entry<String, List<String>> entry : attributes.entrySet()) {

            if (!subscriberAttributes.containsKey(entry.getKey())) {
                return false;
            }

            boolean innerLoop = false;

            for (String value : entry.getValue()) {
                if (subscriberAttributes.get(entry.getKey()).equals(value)) {
                    innerLoop = true;
                }
            }

            if (!innerLoop) {
                return false;
            }
        }

        return true;
    }
}