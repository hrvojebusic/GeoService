package com.infobip.controllers;

import com.infobip.controllers.model.PolygonalAreaResource;
import com.infobip.database.model.PolygonalArea;
import com.infobip.database.repository.PolygonalAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/polygonalArea")
public class PolygonalAreaController {
    private final PolygonalAreaRepository polygonalAreaRepository;

    @Autowired
    public PolygonalAreaController(PolygonalAreaRepository polygonalAreaRepository) {
        this.polygonalAreaRepository = polygonalAreaRepository;
    }

    @RequestMapping(path= "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll(@RequestParam("userId") int userId) {
        List<PolygonalAreaResource> resources = polygonalAreaRepository.findByUserId(userId).stream().
                map(this::createResourceFromPolygonalArea)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @RequestMapping(path = "/{polygonId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOne(@PathVariable String polygonId) {
        PolygonalArea area = polygonalAreaRepository.findOne(polygonId);
        return ResponseEntity.ok(createResourceFromPolygonalArea(area));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody PolygonalAreaResource resource) {
        PolygonalArea polygonalArea = createPolygonalAreaFromResource(resource);
        polygonalAreaRepository.save(polygonalArea);
        return ResponseEntity.status(HttpStatus.CREATED).body(createResourceFromPolygonalArea(polygonalArea));
    }

    @RequestMapping(path = "/{polygonId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable String polygonId, @RequestBody PolygonalAreaResource resource) {
        updateEntity(polygonId, resource);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/{polygonId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable String polygonId) {
        polygonalAreaRepository.delete(polygonId);
        return ResponseEntity.ok().build();
    }


    private PolygonalAreaResource createResourceFromPolygonalArea(PolygonalArea polygonalArea){
        return new PolygonalAreaResource(polygonalArea.getId(), polygonalArea.getDescription(),
                polygonalArea.getUserId(), polygonalArea.getCoordinates());
    }

    private PolygonalArea createPolygonalAreaFromResource(PolygonalAreaResource resource){
        return new PolygonalArea(resource.getDescription(), resource.getUserId(),
                resource.getCoordinates());
    }

    private void updateEntity(String id, PolygonalAreaResource resource) {
        PolygonalArea polygonalArea = polygonalAreaRepository.findOne(id);
        boolean wasUpdated = false;
        if (resource.getDescription() != null) {
            polygonalArea.setDescription(resource.getDescription());
            wasUpdated = true;
        }
        if (resource.getUserId() != null) {
            polygonalArea.setUserId(resource.getUserId());
            wasUpdated = true;
        }
        if(resource.getCoordinates() != null) {
            polygonalArea.setCoordinates(resource.getCoordinates());
            wasUpdated = true;
        }
        if(wasUpdated){
            polygonalAreaRepository.save(polygonalArea);
        }
    }
}
