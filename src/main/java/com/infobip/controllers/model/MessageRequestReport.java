package com.infobip.controllers.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode
@ToString
public class MessageRequestReport {

    private String bulkId;

    private List<MessageRequestStatus> messageRequestStatusList = new ArrayList<>();

    public MessageRequestReport(String bulkId){
        this.bulkId = bulkId;
    }

    public void addMessageStatus(MessageRequestStatus messageRequestStatus){
        messageRequestStatusList.add(messageRequestStatus);
    }

    public List<MessageRequestStatus> getMessageRequestStatusList(){
        return Collections.unmodifiableList(messageRequestStatusList);
    }
}
