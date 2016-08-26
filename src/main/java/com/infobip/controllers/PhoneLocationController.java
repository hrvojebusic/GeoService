package com.infobip.controllers;

import com.infobip.controllers.model.resource.LocationResource;
import com.infobip.controllers.model.resource.PhoneLocationResource;
import com.infobip.controllers.model.resource.PolygonResource;
import com.infobip.database.model.PhoneLocation;
import com.infobip.database.repository.PhoneLocationRepository;
import com.infobip.location.LocationAnalyzer;
import com.infobip.location.PolygonAreaAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/coordinates")
@Slf4j
public class PhoneLocationController {

    @Autowired
    private PhoneLocationRepository phoneLocationRepository;

    @Autowired
    private LocationAnalyzer locationAnalyzer;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private PolygonAreaAnalyzer polygonAreaAnalyzer;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity> getAll() {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult();

        phoneLocationRepository.findAll().addCallback(
                r -> deferredResult.setResult(
                        ResponseEntity.ok(
                                r.stream()
                                        .map(PhoneLocationResource::from)
                                        .collect(Collectors.toList())
                        )
                ),
                t -> handleOnFailure(t, deferredResult));

        return deferredResult;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity> getOne(@PathVariable("id") String id) {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>();

        phoneLocationRepository.findById(id).addCallback(
                r -> deferredResult.setResult(ResponseEntity.ok(r)),
                t -> handleOnFailure(t, deferredResult)
        );

        return deferredResult;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity> addOne(@RequestBody PhoneLocationResource resource) {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>();

        PhoneLocation phoneLocation = PhoneLocationResource.to(resource);
        phoneLocation.setUpdated(Calendar.getInstance().getTime());

        phoneLocationRepository.save(phoneLocation).addCallback(
                r -> {
                    deferredResult.setResult(new ResponseEntity<>(PhoneLocationResource.from(r), HttpStatus.CREATED));
                    scheduleRerun();
                },
                t -> handleOnFailure(t, deferredResult)
        );

        return deferredResult;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity> updateOne(@PathVariable("id") String id, @RequestBody PhoneLocationResource resource) {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>();

        phoneLocationRepository.findById(id).addCallback(
                p1 -> {
                    if (updateEntity(p1, resource)) {
                        phoneLocationRepository.save(p1).addCallback(
                                p2 -> deferredResult.setResult(ResponseEntity.ok().build()),
                                t2 -> handleOnFailure(t2, deferredResult)
                        );
                    }
                },
                t1 -> handleOnFailure(t1, deferredResult)
        );

        return deferredResult;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public DeferredResult<ResponseEntity> deleteAll() {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>();

        phoneLocationRepository.deleteAll().addCallback(
                v -> {
                    deferredResult.setResult(ResponseEntity.ok().build());
                    scheduleRerun();
                },
                t -> handleOnFailure(t, deferredResult)
        );

        return deferredResult;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public DeferredResult<ResponseEntity> deleteOne(@PathVariable("id") String id) {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>();

        phoneLocationRepository.delete(id).addCallback(
                v -> {
                    deferredResult.setResult(ResponseEntity.ok().build());
                    scheduleRerun();
                },
                t -> handleOnFailure(t, deferredResult)
        );

        return deferredResult;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity> getUsersInArea(@RequestBody PolygonResource resource) {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>();

        locationAnalyzer.getPersonsForPolygon(resource, new ListenableFutureCallback<List<PhoneLocationResource>>() {

            @Override
            public void onFailure(Throwable throwable) {
                handleOnFailure(throwable, deferredResult);
            }

            @Override
            public void onSuccess(List<PhoneLocationResource> phoneLocationResources) {
                deferredResult.setResult(ResponseEntity.ok(phoneLocationResources));
            }
        });

        return deferredResult;
    }

    @RequestMapping(value = "/filter")
    public DeferredResult<ResponseEntity> filter(@RequestParam MultiValueMap<String, String> attributes) {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>();

        phoneLocationRepository.findAll().addCallback(
                r -> deferredResult.setResult(ResponseEntity.ok(filterUsers(r, attributes))),
                t -> handleOnFailure(t, deferredResult)
        );

        return deferredResult;
    }

    private boolean updateEntity(PhoneLocation existing, PhoneLocationResource resource) {
        boolean wasUpdated = false;
        boolean wasLocationUpdated = false;

        if (resource.getSubscriber() != null && resource.getSubscriber().getAttributes() != null) {
            existing.getSubscriber().replace(resource.getSubscriber().getAttributes());
            wasUpdated = true;
        }

        if (resource.getNumber() != null && !resource.getNumber().equals(existing.getNumber())) {
            existing.setNumber(resource.getNumber());
            wasUpdated = true;
        }

        if (resource.getLocation() != null && !resource.getLocation().equals(existing.getLocation())) {
            existing.setLocation(LocationResource.to(resource.getLocation()));
            wasUpdated = true;
            wasLocationUpdated = true;
        }

        if (wasUpdated) {
            existing.setUpdated(Calendar.getInstance().getTime());
        }

        if (wasLocationUpdated) {
            scheduleRerun();
        }

        return wasUpdated;
    }

    private List<PhoneLocationResource> filterUsers(List<PhoneLocation> phoneLocations,
                                                    MultiValueMap<String, String> attributes) {
        List<PhoneLocationResource> result = new ArrayList<>();

        for (PhoneLocation phoneLocation : phoneLocations) {
            if (phoneLocation.matchesAttributes(attributes)) {
                result.add(PhoneLocationResource.from(phoneLocation));
            }
        }

        return result;
    }

    private void handleOnFailure(Throwable t, DeferredResult<ResponseEntity> deferredResult) {
        log.error(t.getMessage(), t);
        deferredResult.setResult(ResponseEntity.status(500).build());
    }

    private void scheduleRerun() {
        executorService.submit(() -> polygonAreaAnalyzer.checkPolygonalAreas());
    }
}
