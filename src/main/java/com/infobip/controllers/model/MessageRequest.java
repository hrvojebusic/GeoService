package com.infobip.controllers.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
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
