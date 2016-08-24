package com.infobip.location;

import com.infobip.controllers.model.*;
import com.infobip.database.repository.PhoneLocationRepository;
import com.infobip.sms.gateway.SMSGateway;
import com.infobip.sms.gateway.request.GatewayRequest;
import com.infobip.sms.gateway.request.GatewayRequestEntity;
import com.infobip.sms.gateway.response.GatewayResponse;
import com.infobip.sms.gateway.response.GatewayResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        return phoneLocationRepository
                .findByLocationWithin(PolygonResource.to(polygonResource))
                .stream()
                .map(phoneLocation -> PhoneLocationResource.from(phoneLocation))
                .collect(Collectors.toList());
    }

    public MessageReport notifyPersonsSms(MessageRequest messageRequest) {

        List<PhoneLocationResource> toBeNotified = getPersonsForPolygon(messageRequest.getPolygon());

        GatewayRequest gatewayRequest = new GatewayRequest(
                Collections.singletonList(new GatewayRequestEntity(
                        messageRequest.getSender(),
                        toBeNotified.stream().map(PhoneLocationResource::getNumber).map(String::valueOf).collect(Collectors.toList()),
                        messageRequest.getMessage()
                ))
        );

        GatewayResponse gatewayResponse = smsGateway.push(gatewayRequest);

        MessageReport messageReport = new MessageReport(gatewayResponse.getBulkId());
        for (GatewayResponseEntity gatewayResponseEntity : gatewayResponse.getMessages()) {
            messageReport.addMessageStatus(new MessageReportStatus(
                    gatewayResponseEntity.getTo(),
                    gatewayResponseEntity.getStatus().getDescription(),
                    gatewayResponseEntity.getSmsCount(),
                    gatewayResponseEntity.getMessageId()
            ));
        }

        return messageReport;
    }
}
