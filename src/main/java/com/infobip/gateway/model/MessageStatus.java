package com.infobip.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageStatus {

    private String description;

    protected MessageStatus() {}
}
