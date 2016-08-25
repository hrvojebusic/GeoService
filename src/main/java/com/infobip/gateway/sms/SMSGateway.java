package com.infobip.gateway.sms;

import com.infobip.gateway.sms.request.GatewayRequest;
import com.infobip.gateway.sms.response.GatewayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class SMSGateway {

    private static final Logger LOG = LoggerFactory.getLogger(SMSGateway.class);

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private AsyncRestTemplate restTemplate = new AsyncRestTemplate(
            new HttpComponentsAsyncClientHttpRequestFactory()
    );

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

    public void push(GatewayRequest gatewayRequest, ListenableFutureCallback<GatewayResponse> callback) {
        LOG.info(gatewayRequest.toString());

        ListenableFuture<ResponseEntity<GatewayResponse>> future =
                restTemplate.postForEntity(gatewayUrl, new HttpEntity<>(gatewayRequest, headers), GatewayResponse.class);

        future.addCallback(new ListenableFutureCallback<ResponseEntity<GatewayResponse>>() {

            @Override
            public void onFailure(Throwable throwable) {
                LOG.info(throwable.getMessage(), throwable);
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ResponseEntity<GatewayResponse> gatewayResponseResponseEntity) {
                callback.onSuccess(gatewayResponseResponseEntity.getBody());
            }
        });
    }
}
