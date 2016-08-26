package com.infobip.gateway.sms;

import com.infobip.gateway.sms.request.GatewayRequest;
import com.infobip.gateway.sms.response.GatewayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.Collections;

public class SMSGateway {

    private static final Logger TRAFFIC = LoggerFactory.getLogger("SMS traffic");

    private static final Logger LOG = LoggerFactory.getLogger(SMSGateway.class);

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";

    private AsyncRestTemplate restTemplate = new AsyncRestTemplate(
            new HttpComponentsAsyncClientHttpRequestFactory()
    );

    private HttpHeaders headers = new HttpHeaders();

    private String gatewayUrl;

    public SMSGateway(String gatewayUrl, String authorizationType, String authorizationKey) {
        this.gatewayUrl = gatewayUrl;
        String authorizationHeaderValue = authorizationType + " " + authorizationKey;

        headers.put(AUTHORIZATION_HEADER_KEY, Collections.singletonList(authorizationHeaderValue));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    public void push(GatewayRequest gatewayRequest, ListenableFutureCallback<GatewayResponse> callback) {
        TRAFFIC.info(gatewayRequest.toString());

        restTemplate
                .postForEntity(gatewayUrl, new HttpEntity<>(gatewayRequest, headers), GatewayResponse.class)
                .addCallback(
                        r -> callback.onSuccess(r.getBody()),
                        t -> {
                            LOG.info(t.getMessage(), t);
                            callback.onFailure(t);
                        }
                );
    }
}
