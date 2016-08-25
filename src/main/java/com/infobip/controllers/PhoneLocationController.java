package com.infobip.controllers;

import com.infobip.controllers.model.resource.LocationResource;
import com.infobip.controllers.model.resource.PhoneLocationResource;
import com.infobip.controllers.model.resource.PolygonResource;
import com.infobip.database.model.PhoneLocation;
import com.infobip.database.repository.PhoneLocationRepository;
import com.infobip.location.LocationAnalyzer;
import com.infobip.location.PolygonAreaAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/coordinates")
public class PhoneLocationController {

    @Autowired
    private PhoneLocationRepository phoneLocationRepository;
    @Autowired
    private LocationAnalyzer locationAnalyzer;
    @Autowired
    private ExecutorService executorService;

    private PolygonAreaAnalyzer polygonAreaAnalyzer = null;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return ResponseEntity.ok(phoneLocationRepository
                .findAll()
                .stream()
                .map(PhoneLocationResource::from)
                .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult getOne(@PathVariable("id") String id) {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>();


        phoneLocationRepository.findById(id).addCallback((result) -> {
            if(result != null) {
                deferredResult.setResult(ResponseEntity.ok(result));
            }
            else{
                deferredResult.setResult(ResponseEntity.notFound().build());
            }
        }, (e) -> deferredResult.setResult(ResponseEntity.badRequest().body(e)));

        return deferredResult;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addOne(@RequestBody PhoneLocationResource resource) {
        PhoneLocation phoneLocation = PhoneLocationResource.to(resource);
        phoneLocation.setUpdated(Calendar.getInstance().getTime());
        phoneLocation = phoneLocationRepository.save(phoneLocation);
        executorService.submit(() -> polygonAreaAnalyzer.checkPolygonalAreas());
        return ResponseEntity.status(HttpStatus.CREATED).body(PhoneLocationResource.from(phoneLocation));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateOne(@PathVariable("id") String id, @RequestBody PhoneLocationResource resource) {
        updateEntity(id, resource);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteAll() {
        phoneLocationRepository.deleteAll();
        executorService.submit(() -> polygonAreaAnalyzer.checkPolygonalAreas());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteOne(@PathVariable("id") String id) {
        phoneLocationRepository.delete(id);
        executorService.submit(() -> polygonAreaAnalyzer.checkPolygonalAreas());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUsersInArea(@RequestBody PolygonResource resource) {
        return ResponseEntity.ok(locationAnalyzer.getPersonsForPolygon(resource));
    }

    @RequestMapping(value = "/filter")
    public ResponseEntity filter(@RequestParam MultiValueMap<String, String> attributes) {
        return ResponseEntity.ok(filterUsers(attributes));
    }

    private void updateEntity(String id, PhoneLocationResource resource) {
        PhoneLocation phoneLocation = phoneLocationRepository.findOne(id);
        boolean wasUpdated = false;
        boolean wasLocationUpdated = false;

        if (resource.getSubscriber() != null && resource.getSubscriber().getAttributes() != null) {
            phoneLocation.getSubscriber().replace(resource.getSubscriber().getAttributes());
            wasUpdated = true;
        }

        if (resource.getNumber() != null) {
            phoneLocation.setNumber(resource.getNumber());
            wasUpdated = true;
        }

        if (resource.getLocation() != null) {
            phoneLocation.setLocation(LocationResource.to(resource.getLocation()));
            wasUpdated = true;
            wasLocationUpdated = true;
        }

        if (wasUpdated) {
            phoneLocation.setUpdated(Calendar.getInstance().getTime());
            phoneLocationRepository.save(phoneLocation);
        }

        if (wasLocationUpdated) {
            executorService.submit(() -> polygonAreaAnalyzer.checkPolygonalAreas());
        }
    }

    private List<PhoneLocationResource> filterUsers(MultiValueMap<String, String> attributes) {
        List<PhoneLocationResource> result = new ArrayList<>();

        for (PhoneLocation phoneLocation : phoneLocationRepository.findAll()) {
            if (phoneLocation.matchesAttributes(attributes)) {
                result.add(PhoneLocationResource.from(phoneLocation));
            }
        }

        return result;
    }
}
