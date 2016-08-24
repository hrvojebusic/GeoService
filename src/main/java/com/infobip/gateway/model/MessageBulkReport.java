package com.infobip.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class MessageBulkReport {

    private String bulkId;

    private List<MessageSingleReport> messages = new ArrayList<>();

    protected MessageBulkReport() {}
}
