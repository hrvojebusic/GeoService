package com.infobip.gateway.sms.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class GatewayRequest {

    private List<GatewayRequestEntity> messages = new ArrayList<>();

    protected GatewayRequest() {}
}
