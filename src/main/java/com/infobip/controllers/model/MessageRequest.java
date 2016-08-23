package com.infobip.controllers.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class MessageRequest {

    private String sender;

    private String message;

    private PolygonResource polygon;

    protected MessageRequest(){}
}
