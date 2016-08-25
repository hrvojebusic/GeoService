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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationAnalyzer {

    @Autowired
    private PhoneLocationRepository phoneLocationRepository;

    @Autowired
    private SMSGateway smsGateway;

    public List<PhoneLocationResource> getPersonsForPolygon(PolygonResource polygonResource) {
        try {
            return phoneLocationRepository
                    .findByLocationWithin(PolygonResource.to(polygonResource)).get()
                    .stream()
                    .map(phoneLocation -> PhoneLocationResource.from(phoneLocation))
                    .collect(Collectors.toList());
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void notifyPersonsSms(MessageRequest messageRequest, ListenableFutureCallback<MessageReport> callback) {

        phoneLocationRepository
                .findByLocationWithin(PolygonResource.to(messageRequest.getPolygon()))
                .addCallback(new ListenableFutureCallback<List<PhoneLocation>>() {

                    @Override
                    public void onFailure(Throwable throwable) {
                        callback.onFailure(throwable);
                    }

                    @Override
                    public void onSuccess(List<PhoneLocation> phoneLocations) {

                        GatewayRequest gatewayRequest = new GatewayRequest(
                                Collections.singletonList(new GatewayRequestEntity(
                                        messageRequest.getSender(),
                                        phoneLocations.stream().map(PhoneLocation::getNumber).map(String::valueOf).collect(Collectors.toList()),
                                        messageRequest.getMessage()
                                ))
                        );

                        smsGateway.push(gatewayRequest, new ListenableFutureCallback<GatewayResponse>() {

                            @Override
                            public void onFailure(Throwable throwable) {
                                callback.onFailure(throwable);
                            }

                            @Override
                            public void onSuccess(GatewayResponse gatewayResponse) {

                                MessageReport messageReport = new MessageReport(gatewayResponse.getBulkId());
                                for (GatewayResponseEntity gatewayResponseEntity : gatewayResponse.getMessages()) {
                                    messageReport.addMessageStatus(new MessageReportStatus(
                                            gatewayResponseEntity.getTo(),
                                            gatewayResponseEntity.getStatus().getDescription(),
                                            gatewayResponseEntity.getSmsCount(),
                                            gatewayResponseEntity.getMessageId()
                                    ));
                                }

                                callback.onSuccess(messageReport);
                            }
                        });
                    }
                });
    }
}
