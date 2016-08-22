package com.infobip.database.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Data
public class Polygon {

    @Setter(AccessLevel.PRIVATE)
    @Id
    private String id;

    private String description;

    private Location[] coordinates;
}
