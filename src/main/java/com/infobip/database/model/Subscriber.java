package com.infobip.database.model;

import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class Subscriber {

    private Map<String, String> attributes = new HashMap<>();

    protected Subscriber() {
    }

    public void addAtrribute(String key, String value) {
        attributes.put(key, value);
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}