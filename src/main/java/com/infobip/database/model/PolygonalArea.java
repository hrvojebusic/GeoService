package com.infobip.database.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class PolygonalArea {

    @Setter(AccessLevel.PRIVATE)
    @Id
    private String id;

    private String inMessage;

    private String outMessage;

    private String sender;

    @Setter(AccessLevel.PRIVATE)
    private List<String> users = new ArrayList<>();

    private GeoJsonPolygon polygon;

    protected PolygonalArea() {
    }

    public PolygonalArea(String inMessage, String outMessage, String sender, GeoJsonPolygon polygon) {
        this.inMessage = inMessage;
        this.outMessage = outMessage;
        this.sender = sender;
        this.polygon = polygon;
    }

    public List<String> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public void addUser(String id) {
        users.add(id);
    }

    public void removeUser(String id) {
        users.remove(id);
    }

    public void removeUsers() {
        users.clear();
    }
}
