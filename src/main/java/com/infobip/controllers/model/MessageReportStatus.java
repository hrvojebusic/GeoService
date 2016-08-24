package com.infobip.controllers.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class MessageReportStatus {

    private String number;

    private String status;

    private Integer smsCount;

    private String messageId;
}
