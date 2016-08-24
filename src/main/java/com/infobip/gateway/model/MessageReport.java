package com.infobip.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageReport {

    private String to;

    private MessageStatus status;

    private String messageId;

    protected MessageReport() {}
}