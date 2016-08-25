package com.infobip.gateway.sms;

import com.infobip.gateway.sms.request.GatewayRequest;
import com.infobip.gateway.sms.response.GatewayResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class SMSGateway {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    private String gatewayUrl;

    private String authorizationType;

    private String authorizationKey;

    public SMSGateway(String gatewayUrl, String authorizationType, String authorizationKey) {
        this.gatewayUrl = gatewayUrl;
        this.authorizationType = authorizationType;
        this.authorizationKey = authorizationKey;

        headers.put(AUTHORIZATION_HEADER_NAME, Collections.singletonList(this.authorizationType + " " + this.authorizationKey));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    public GatewayResponse push(GatewayRequest gatewayRequest) {
        return restTemplate.postForObject(gatewayUrl, new HttpEntity<>(gatewayRequest, headers), GatewayResponse.class);
    }
}
