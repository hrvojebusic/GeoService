package com.infobip.sms.gateway.request;

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
