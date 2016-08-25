package com.infobip.location;

import com.infobip.controllers.model.*;
import com.infobip.controllers.model.resource.PhoneLocationResource;
import com.infobip.controllers.model.resource.PolygonResource;
import com.infobip.database.model.PhoneLocation;
import com.infobip.database.repository.PhoneLocationRepository;
import com.infobip.gateway.sms.SMSGateway;
import com.infobip.gateway.sms.request.GatewayRequest;
import com.infobip.gateway.sms.request.GatewayRequestEntity;
import com.infobip.gateway.sms.response.GatewayResponse;
import com.infobip.gateway.sms.response.GatewayResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(LocationAnalyzer.class);

    @Autowired
    private PhoneLocationRepository phoneLocationRepository;

    @Autowired
    private SMSGateway smsGateway;

    public void getPersonsForPolygon(PolygonResource polygonResource,
                                     ListenableFutureCallback<List<PhoneLocationResource>> callback) {
        phoneLocationRepository
                .findByLocationWithin(PolygonResource.to(polygonResource))
                .addCallback(
                        r -> callback.onSuccess(r.stream()
                                .map(PhoneLocationResource::from)
                                .collect(Collectors.toList())),
                        t -> {
                            LOG.error(t.getMessage(), t);
                            callback.onFailure(t);
                        }
                );
    }

    public void notifyPersonsSms(MessageRequest messageRequest,
                                 ListenableFutureCallback<MessageReport> callback) {
        phoneLocationRepository
                .findByLocationWithin(PolygonResource.to(messageRequest.getPolygon()))
                .addCallback(
                        r -> {
                            GatewayRequest gatewayRequest = MessageRequest.toGatewayRequest(messageRequest, r);
                            smsGateway.push(gatewayRequest, getCallbackForSmsGateway(gatewayRequest, callback));
                        },
                        t -> {
                            LOG.error(t.getMessage(), t);
                            callback.onFailure(t);
                        }
                );
    }

    private ListenableFutureCallback<GatewayResponse> getCallbackForSmsGateway(
            GatewayRequest gatewayRequest,
            ListenableFutureCallback<MessageReport> callback) {

        return new ListenableFutureCallback<GatewayResponse>() {

            @Override
            public void onFailure(Throwable throwable) {
                LOG.error(throwable.getMessage(), throwable);
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(GatewayResponse gatewayResponse) {
                callback.onSuccess(MessageReport.fromGatewayResponse(gatewayResponse));
            }
        };
    }
}
