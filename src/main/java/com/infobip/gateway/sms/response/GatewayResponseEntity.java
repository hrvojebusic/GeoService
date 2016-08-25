package com.infobip.gateway.sms.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GatewayResponseEntity {

    private String to;

    private GatewayResponseEntityStatus status;

    private Integer smsCount;

    private String messageId;

    protected GatewayResponseEntity() {}
}