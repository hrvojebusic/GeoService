package com.infobip.controllers.model.resource;

import com.infobip.database.model.Subscriber;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class SubscriberResource {

    private Map<String, String> attributes = new HashMap<>();

    protected SubscriberResource() {}

    public static Subscriber to(SubscriberResource subscriberResource) {
        return new Subscriber(new HashMap<>(subscriberResource.attributes));
    }

    public static SubscriberResource from(Subscriber subscriber) {
        return new SubscriberResource(new HashMap<>(subscriber.getAttributes()));
    }
}
