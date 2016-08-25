package com.infobip.controllers.model;

import com.infobip.controllers.model.resource.PhoneLocationResource;
import com.infobip.controllers.model.resource.PolygonResource;
import com.infobip.database.model.PhoneLocation;
import com.infobip.gateway.sms.request.GatewayRequest;
import com.infobip.gateway.sms.request.GatewayRequestEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Data
@Setter(AccessLevel.PRIVATE)
public class MessageRequest {

    @NotEmpty
    private String sender;

    @NotEmpty
    private String message;

    @NotNull
    private PolygonResource polygon;

    protected MessageRequest(){}

    public static GatewayRequest toGatewayRequest(MessageRequest messageRequest,
                                                  Collection<PhoneLocation> phoneLocations) {
        return new GatewayRequest(
                Collections.singletonList(new GatewayRequestEntity(
                        messageRequest.getSender(),
                        phoneLocations
                                .stream()
                                .map(PhoneLocation::getNumber)
                                .map(String::valueOf)
                                .collect(Collectors.toList()),
                        messageRequest.getMessage()
                ))
        );
    }
}
