package com.infobip.controllers;

import com.infobip.controllers.model.resource.PolygonalAreaResource;
import com.infobip.database.model.PolygonalArea;
import com.infobip.database.repository.PolygonalAreaRepository;
import com.infobip.location.PolygonAreaAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/polygon")
@Slf4j
public class PolygonalAreaController {

    @Autowired
    private PolygonalAreaRepository polygonalAreaRepository;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private PolygonAreaAnalyzer polygonAreaAnalyzer;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity> getAll() {
        DeferredResult result = new DeferredResult();

        polygonalAreaRepository
                .findAll()
                .addCallback(polygonalAreas -> result.setResult(
                        ResponseEntity.status(HttpStatus.OK).body(
                                polygonalAreas
                                        .stream()
                                        .map(PolygonalAreaResource::from)
                                        .collect(Collectors.toList()))), e -> {
                    log.info("Error", e);
                });

        return result;
    }


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity> add(@RequestBody PolygonalAreaResource resource) {

        DeferredResult result = new DeferredResult();
        PolygonalArea polygonalArea = PolygonalAreaResource.to(resource);

        polygonalAreaRepository
                .save(polygonalArea)
                .addCallback(polygonalArea1 -> result
                                .setResult(ResponseEntity
                                        .status(HttpStatus.CREATED)
                                        .body(PolygonalAreaResource.from(polygonalArea))),
                        e -> {
                            log.info("Error", e);
                        });
        executorService.submit(() -> polygonAreaAnalyzer.checkPolygonalAreas());
        return result;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        DeferredResult result = new DeferredResult();
        polygonalAreaRepository
                .delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
