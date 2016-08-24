package com.infobip.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GatewayRequest {

    private String from;
    private String to;
    private String text;

    protected GatewayRequest() {}
}
