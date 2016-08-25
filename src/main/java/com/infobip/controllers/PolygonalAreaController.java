package com.infobip.controllers;

import com.infobip.controllers.model.resource.PolygonalAreaResource;
import com.infobip.database.model.PolygonalArea;
import com.infobip.database.repository.PolygonalAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/polygon")
public class PolygonalAreaController {

    @Autowired
    private PolygonalAreaRepository polygonalAreaRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAll() {
        return ResponseEntity.ok(polygonalAreaRepository
                .findAll()
                .stream()
                .map(PolygonalAreaResource::from)
                .collect(Collectors.toList()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody PolygonalAreaResource resource) {
        PolygonalArea polygonalArea = PolygonalAreaResource.to(resource);
        polygonalAreaRepository.save(polygonalArea);
        return ResponseEntity.status(HttpStatus.CREATED).body(PolygonalAreaResource.from(polygonalArea));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        polygonalAreaRepository.delete(id);
        return ResponseEntity.ok().build();
    }
}
