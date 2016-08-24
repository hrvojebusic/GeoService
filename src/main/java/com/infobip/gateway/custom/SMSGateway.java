package com.infobip.gateway.custom;

import com.infobip.controllers.model.MessageRequestReport;
import com.infobip.controllers.model.MessageRequestStatus;
import com.infobip.gateway.Gateway;
import com.infobip.gateway.GatewayRequest;
import com.infobip.gateway.SMSGatewayRequest;
import com.infobip.gateway.model.SingleMessageReport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;

public class SMSGateway implements Gateway {

    private RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    private String gatewayUrl;

    private String authorizationType;

    private String authorizationKey;

    public SMSGateway(String gatewayUrl, String authorizationType, String authorizationKey) {
        this.gatewayUrl = gatewayUrl;
        this.authorizationType = authorizationType;
        this.authorizationKey = authorizationKey;

        headers.put("Authorization", Collections.singletonList(authorizationType + " " + authorizationKey));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    @Override
    public MessageRequestReport push(Collection<GatewayRequest> requests) {
        MessageRequestReport messageRequestReport = new MessageRequestReport(null);

        for (GatewayRequest gatewayRequest : requests) {
            HttpEntity<SMSGatewayRequest> entity =
                    new HttpEntity<>(new SMSGatewayRequest(
                            gatewayRequest.getFrom(),
                            gatewayRequest.getTo(),
                            gatewayRequest.getText()
                    ),
                            headers
                    );

            SingleMessageReport report = restTemplate.postForObject(gatewayUrl, entity, SingleMessageReport.class);
            messageRequestReport.addMessageStatus(new MessageRequestStatus(
                    report.getMessages().get(0).getTo(),
                    report.getMessages().get(0).getStatus().getDescription(),
                    report.getMessages().get(0).getMessageId()
            ));
        }

        return messageRequestReport;
    }
}
