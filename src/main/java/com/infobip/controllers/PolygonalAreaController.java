package com.infobip.controllers;

import com.infobip.controllers.model.resource.PolygonalAreaResource;
import com.infobip.database.model.PolygonalArea;
import com.infobip.database.repository.PolygonalAreaRepository;
import com.infobip.location.LocationAnalyzer;
import com.infobip.location.PolygonAreaAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/polygon")
public class PolygonalAreaController {

    @Autowired
    private PolygonalAreaRepository polygonalAreaRepository;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private PolygonAreaAnalyzer polygonAreaAnalyzer;


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return ResponseEntity.ok(polygonalAreaRepository
                .findAll()
                .stream()
                .map(PolygonalAreaResource::from)
                .collect(Collectors.toList()));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody PolygonalAreaResource resource) {
        PolygonalArea polygonalArea = PolygonalAreaResource.to(resource);
        polygonalAreaRepository.save(polygonalArea);
        executorService.submit(() -> polygonAreaAnalyzer.checkPolygonalAreas());
        return ResponseEntity.status(HttpStatus.CREATED).body(PolygonalAreaResource.from(polygonalArea));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        polygonalAreaRepository.delete(id);
        return ResponseEntity.ok().build();
    }
}
