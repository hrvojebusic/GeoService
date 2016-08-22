package com.infobip.controllers;

import com.infobip.controllers.model.PersonCoordinateResource;
import com.infobip.database.model.PersonCoordinate;
import com.infobip.database.repository.PersonCoordinateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/coordinates")
public class PersonCoordinateController {

    private final PersonCoordinateRepository personCoordinateRepository;

    @Autowired
    public PersonCoordinateController(PersonCoordinateRepository personCoordinateRepository) {
        this.personCoordinateRepository = personCoordinateRepository;
    }

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        List<PersonCoordinateResource> resources = personCoordinateRepository.findAll().stream().
                map(this::createResourceFromPersonCoordinate)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable String id, @RequestBody PersonCoordinateResource resource) {
        updateEntity(id, resource);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody PersonCoordinateResource resource) {
        PersonCoordinate personCoordinate = createPersonCoordinateFromResource(resource);
        personCoordinateRepository.save(personCoordinate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createResourceFromPersonCoordinate(personCoordinate));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        personCoordinateRepository.delete(id);
        return ResponseEntity.ok().build();
    }

    private void updateEntity(String id, PersonCoordinateResource resource) {
        PersonCoordinate personCoordinate = personCoordinateRepository.findOne(id);
        boolean wasUpdated = false;
        if (resource.getLocation() != null) {
            personCoordinate.setLocation(resource.getLocation());
            wasUpdated = true;
        }
        if (resource.getNumber() != null) {
            personCoordinate.setNumber(resource.getNumber());
            wasUpdated = true;
        }
        if(wasUpdated){
            personCoordinate.setUpdated(Calendar.getInstance().getTime());
            personCoordinateRepository.save(personCoordinate);
        }
    }

    private PersonCoordinateResource createResourceFromPersonCoordinate(PersonCoordinate personCoordinate){
        return new PersonCoordinateResource(personCoordinate.getId(), personCoordinate.getNumber(),
                personCoordinate.getLocation(), personCoordinate.getUpdated());
    }

    private PersonCoordinate createPersonCoordinateFromResource(PersonCoordinateResource resource){
        return new PersonCoordinate(resource.getNumber(), resource.getLocation(),
                resource.getUpdated());
    }
}
