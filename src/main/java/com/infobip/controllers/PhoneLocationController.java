package com.infobip.controllers;

import com.infobip.controllers.model.resource.LocationResource;
import com.infobip.controllers.model.resource.PhoneLocationResource;
import com.infobip.controllers.model.resource.PolygonResource;
import com.infobip.database.model.PhoneLocation;
import com.infobip.database.repository.PhoneLocationRepository;
import com.infobip.location.LocationAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/coordinates")
public class PhoneLocationController {

    @Autowired
    private PhoneLocationRepository phoneLocationRepository;

    @Autowired
    private LocationAnalyzer locationAnalyzer;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAll() {
        return ResponseEntity.ok(phoneLocationRepository
                .findAll()
                .stream()
                .map(PhoneLocationResource::from)
                .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getOne(@PathVariable("id") String id) {
        return ResponseEntity.ok(phoneLocationRepository.findOne(id));
    }

    @RequestMapping(value = "/filter")
    public ResponseEntity filter(@RequestParam MultiValueMap<String, String> attributes) {
        return ResponseEntity.ok(filterUsers(attributes));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addOne(@RequestBody PhoneLocationResource resource) {
        PhoneLocation phoneLocation = PhoneLocationResource.to(resource);
        phoneLocation.setUpdated(Calendar.getInstance().getTime());
        phoneLocationRepository.save(phoneLocation);
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
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteOne(@PathVariable String id) {
        phoneLocationRepository.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUsersInArea(@RequestBody PolygonResource resource) {
        return ResponseEntity.ok(locationAnalyzer.getPersonsForPolygon(resource));
    }

    private void updateEntity(String id, PhoneLocationResource resource) {
        PhoneLocation phoneLocation = phoneLocationRepository.findOne(id);
        boolean wasUpdated = false;

        if (resource.getSubscriber() != null && resource.getSubscriber().getAttributes() != null) {
            phoneLocation.getSubscriber().replace(resource.getSubscriber().getAttributes());
        }

        if (resource.getNumber() != null) {
            phoneLocation.setNumber(resource.getNumber());
            wasUpdated = true;
        }

        if (resource.getLocation() != null) {
            phoneLocation.setLocation(
                    LocationResource.to(resource.getLocation())
            );
            wasUpdated = true;
        }

        if (wasUpdated) {
            phoneLocation.setUpdated(Calendar.getInstance().getTime());
            phoneLocationRepository.save(phoneLocation);
        }
    }

    private List<PhoneLocationResource> filterUsers(MultiValueMap<String, String> attributes) {
        List<PhoneLocationResource> result = new ArrayList<>();

        for (PhoneLocation phoneLocation : phoneLocationRepository.findAll()) {
            if(phoneLocation.matchesAttributes(attributes)) {
                result.add(PhoneLocationResource.from(phoneLocation));
            }
        }

        return result;
    }
}
