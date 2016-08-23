package com.infobip.controllers.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class PolygonalAreaResource {

    private String id;

    private String description;

    private Integer userId;

    private List<LocationResource> coordinates;

    protected PolygonalAreaResource() {}
}
