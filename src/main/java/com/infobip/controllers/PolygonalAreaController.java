package com.infobip.controllers;

import com.infobip.controllers.model.PolygonalAreaResource;
import com.infobip.database.model.PhoneLocation;
import com.infobip.database.model.PolygonalArea;
import com.infobip.database.repository.PhoneLocationRepository;
import com.infobip.database.repository.PolygonalAreaRepository;
import com.infobip.location.LocationAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/polygon")
public class PolygonalAreaController {

    private final PolygonalAreaRepository polygonalAreaRepository;

    @Autowired
    public PolygonalAreaController(PolygonalAreaRepository polygonalAreaRepository) {
        this.polygonalAreaRepository = polygonalAreaRepository;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        List<PolygonalAreaResource> resources = polygonalAreaRepository.findAll().stream().
                map(PolygonalAreaResource::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody PolygonalAreaResource resource) {
        PolygonalArea polygonalArea = PolygonalAreaResource.to(resource);
        polygonalAreaRepository.save(polygonalArea);
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        polygonalAreaRepository.delete(id);
        return ResponseEntity.ok().build();
    }
}
