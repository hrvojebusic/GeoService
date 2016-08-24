package com.infobip.controllers.model;

import com.infobip.controllers.model.resource.PolygonResource;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@Setter(AccessLevel.PRIVATE)
public class MessageRequest {

    @NotEmpty
    private String sender;

    @NotEmpty
    private String message;

    @NotNull
    private PolygonResource polygon;

    protected MessageRequest(){}
}
