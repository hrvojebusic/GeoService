package com.infobip.database.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Subscriber {

    private Map<String, String> attributes = new HashMap<>();

    protected Subscriber() {
    }

    public Subscriber(Map<String, String> attributes) {
        this.attributes.putAll(attributes);
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public void removeAttribute(String key) { attributes.remove(key); }

    public void addAll(Map<String, String> attributes) {
        this.attributes.putAll(attributes);
    }

    public void removeAll() {
        attributes.clear();
    }

    public void replace(Map<String, String> attributes) {
        removeAll();
        addAll(attributes);
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}