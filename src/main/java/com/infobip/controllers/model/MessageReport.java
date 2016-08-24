package com.infobip.controllers.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode
@ToString
public class MessageReport {

    private String bulkId;

    private List<MessageReportStatus> messageReportStatusList = new ArrayList<>();

    public MessageReport(String bulkId) { this.bulkId = bulkId; }

    public void addMessageStatus(MessageReportStatus messageReportStatus){
        messageReportStatusList.add(messageReportStatus);
    }

    public List<MessageReportStatus> getMessageReportStatusList(){
        return Collections.unmodifiableList(messageReportStatusList);
    }
}
