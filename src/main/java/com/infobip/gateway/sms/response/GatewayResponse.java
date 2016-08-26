package com.infobip.gateway.sms.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class GatewayResponse {

    private String bulkId;

    private List<GatewayResponseEntity> messages = new ArrayList<>();

    protected GatewayResponse() {
    }
}
