package com.infobip.controllers.model.resource;

import com.infobip.database.model.Subscriber;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SubscriberResource {

    private Map<String, String> attributes = new HashMap<>();

    protected SubscriberResource() {
    }

    public SubscriberResource(Map<String, String> attributes) {
        this.attributes.putAll(attributes);
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public static Subscriber to(SubscriberResource subscriberResource) {
        return new Subscriber(subscriberResource.attributes);
    }

    public static SubscriberResource from(Subscriber subscriber) {
        return new SubscriberResource(subscriber.getAttributes());
    }
}
