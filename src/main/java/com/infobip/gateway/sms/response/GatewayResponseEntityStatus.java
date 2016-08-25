package com.infobip.gateway.sms.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GatewayResponseEntityStatus {

    private String name;

    private String description;

    protected GatewayResponseEntityStatus() {}
}
