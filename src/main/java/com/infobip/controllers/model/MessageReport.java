package com.infobip.controllers.model;

import com.infobip.gateway.sms.response.GatewayResponse;
import com.infobip.gateway.sms.response.GatewayResponseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode
@ToString
public class MessageReport {

    private String bulkId;

    private List<MessageReportStatus> messageReportStatusList = new ArrayList<>();

    public MessageReport(String bulkId) {
        this.bulkId = bulkId;
    }

    public void addMessageStatus(MessageReportStatus messageReportStatus) {
        messageReportStatusList.add(messageReportStatus);
    }

    public List<MessageReportStatus> getMessageReportStatusList() {
        return Collections.unmodifiableList(messageReportStatusList);
    }

    public static MessageReport fromGatewayResponse(GatewayResponse gatewayResponse) {
        MessageReport messageReport = new MessageReport(gatewayResponse.getBulkId());

        for (GatewayResponseEntity gatewayResponseEntity : gatewayResponse.getMessages()) {
            messageReport.addMessageStatus(new MessageReportStatus(
                    gatewayResponseEntity.getTo(),
                    gatewayResponseEntity.getStatus().getDescription(),
                    gatewayResponseEntity.getSmsCount(),
                    gatewayResponseEntity.getMessageId()
            ));
        }

        return messageReport;
    }

    @Data
    @AllArgsConstructor
    @Setter(AccessLevel.PRIVATE)
    public static class MessageReportStatus {

        private String number;

        private String status;

        private Integer smsCount;

        private String messageId;
    }
}
