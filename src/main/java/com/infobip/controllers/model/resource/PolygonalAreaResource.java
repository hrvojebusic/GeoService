package com.infobip.controllers.model.resource;

import com.infobip.database.model.PolygonalArea;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PolygonalAreaResource {
    @Setter(AccessLevel.PRIVATE)
    private String id;

    private String inMessage;

    private String outMessage;

    private String sender;

    @Setter(AccessLevel.PRIVATE)
    private List<String> users = new ArrayList<>();

    private PolygonResource polygon;

    public List<String> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public void addUser(String id) {
        users.add(id);
    }

    public void removeUser(String id) {
        users.remove(id);
    }

    protected PolygonalAreaResource() {}

    public static PolygonalArea to(PolygonalAreaResource resource) {
        return new PolygonalArea(resource.getInMessage(), resource.getOutMessage(),resource.getSender(),
                new GeoJsonPolygon(resource.getPolygon().getCoordinates().stream().
                        map(coordinate -> new Point(coordinate.getX(), coordinate.getY())).collect(Collectors.toList())));
    }

    public static PolygonalAreaResource from(PolygonalArea area) {
        return new PolygonalAreaResource(area.getId(), area.getInMessage(),
                area.getOutMessage(), area.getSender(), area.getUsers(),
                new PolygonResource(area.getPolygon().getPoints().stream().
                        map(point -> new LocationResource(point.getX(), point.getY())).collect(Collectors.toList())));
    }
}
