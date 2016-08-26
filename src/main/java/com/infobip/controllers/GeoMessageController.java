package com.infobip.controllers;

import com.infobip.controllers.model.MessageReport;
import com.infobip.controllers.model.MessageRequest;
import com.infobip.location.LocationAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/messages")
public class GeoMessageController {

    @Autowired
    private LocationAnalyzer locationAnalyzer;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult sendMessage(@Validated @RequestBody MessageRequest request) {

        DeferredResult<ResponseEntity> deferredResult = new DeferredResult();

        locationAnalyzer.notifyPersonsSms(request, new ListenableFutureCallback<MessageReport>() {

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("SHIT HAPPENED");
            }

            @Override
            public void onSuccess(MessageReport messageReport) {
                deferredResult.setResult(ResponseEntity.ok(messageReport));
            }
        });

        return deferredResult;
    }
}
