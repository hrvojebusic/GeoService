package com.infobip.controllers.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode
@ToString
public class MessageReport {

    private String bulkId;

    private List<MessageStatus> messageStatusList = new ArrayList<>();

    public MessageReport(String bulkId){
        this.bulkId = bulkId;
    }

    public void addMessageStatus(MessageStatus messageStatus){
        messageStatusList.add(messageStatus);
    }

    public List<MessageStatus> getMessageStatusList(){
        return Collections.unmodifiableList(messageStatusList);
    }
}
