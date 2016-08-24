package com.infobip.controllers.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class MessageRequestStatus {

    private String number;

    private String state;

    private String messageId;
}
