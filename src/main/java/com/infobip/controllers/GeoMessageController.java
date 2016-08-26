package com.infobip.controllers;

import com.infobip.controllers.model.MessageReport;
import com.infobip.controllers.model.MessageRequest;
import com.infobip.location.LocationAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(GeoMessageController.class);

    @Autowired
    private LocationAnalyzer locationAnalyzer;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity> sendMessage(@Validated @RequestBody MessageRequest request) {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult();

        locationAnalyzer.notifyPersonsSms(request, new ListenableFutureCallback<MessageReport>() {

            @Override
            public void onFailure(Throwable throwable) {
                LOG.error(throwable.getMessage(), throwable);
                deferredResult.setResult(ResponseEntity.status(500).build());
            }

            @Override
            public void onSuccess(MessageReport messageReport) {
                deferredResult.setResult(ResponseEntity.ok(messageReport));
            }
        });

        return deferredResult;
    }
}
