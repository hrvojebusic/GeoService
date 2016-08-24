package com.infobip.controllers;

import com.infobip.controllers.model.MessageRequest;
import com.infobip.location.LocationAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class GeoMessageController {

    @Autowired
    private LocationAnalyzer locationAnalyzer;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendMessage(@Validated @RequestBody MessageRequest request) {
            return ResponseEntity.ok(locationAnalyzer.notifyPersonsSms(request));
    }
}
