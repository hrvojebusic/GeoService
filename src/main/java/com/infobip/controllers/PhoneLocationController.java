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
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/coordinates")
public class PhoneLocationController {

    private final PhoneLocationRepository phoneLocationRepository;
    private final LocationAnalyzer locationAnalyzer;

    @Autowired
    public PhoneLocationController(PhoneLocationRepository phoneLocationRepository, LocationAnalyzer locationAnalyzer) {
        this.phoneLocationRepository = phoneLocationRepository;
        this.locationAnalyzer = locationAnalyzer;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        List<PhoneLocationResource> resources = phoneLocationRepository.findAll().stream().
                map(PhoneLocationResource::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @RequestMapping(path= "/users", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUsersInArea(@RequestBody PolygonResource resource) {
        return ResponseEntity.ok( locationAnalyzer.getPersonsForPolygon(resource));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable String id, @RequestBody PhoneLocationResource resource) {
        updateEntity(id, resource);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody PhoneLocationResource resource) {
        PhoneLocation phoneLocation = PhoneLocationResource.to(resource);
        phoneLocation.setUpdated(Calendar.getInstance().getTime());
        phoneLocationRepository.save(phoneLocation);
        return ResponseEntity.status(HttpStatus.CREATED).body(PhoneLocationResource.from(phoneLocation));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        phoneLocationRepository.delete(id);
        return ResponseEntity.ok().build();
    }

    private void updateEntity(String id, PhoneLocationResource resource) {
        PhoneLocation phoneLocation = phoneLocationRepository.findOne(id);
        boolean wasUpdated = false;
        if (resource.getLocation() != null) {
            phoneLocation.setLocation(LocationResource.to(resource.getLocation()));
            wasUpdated = true;
        }
        if (resource.getNumber() != null) {
            phoneLocation.setNumber(resource.getNumber());
            wasUpdated = true;
        }
        if(wasUpdated){
            phoneLocation.setUpdated(Calendar.getInstance().getTime());
            phoneLocationRepository.save(phoneLocation);
        }
    }
}
