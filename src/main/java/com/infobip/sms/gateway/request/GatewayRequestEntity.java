package com.infobip.sms.gateway.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class GatewayRequestEntity {

    private String from;
    private List<String> to = new ArrayList<>();
    private String text;

    protected GatewayRequestEntity() {}
}
