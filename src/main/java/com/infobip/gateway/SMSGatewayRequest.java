package com.infobip.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SMSGatewayRequest extends GatewayRequest{

    protected SMSGatewayRequest() {}

    public SMSGatewayRequest(String from, String to, String text) {
        super(from, to, text);
    }
}
