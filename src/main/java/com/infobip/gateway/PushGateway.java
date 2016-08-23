package com.infobip.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by grumin on 11.8.2016.
 */
public class PushGateway implements Gateway {

    private static final Logger LOG = LoggerFactory.getLogger(PushGateway.class);

    private String uri;
    private String from;
    private HttpHeaders headers = new HttpHeaders();

    private final RestTemplate restTemplate = new RestTemplate();

    public PushGateway(String uri, String from, String authorizationKey) {
        this.uri = uri;
        this.from = from;
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.put("Authorization", Collections.singletonList("App " + authorizationKey));
    }

    @Override
    public void push(Collection<GatewayRequest> requests) {
        for (GatewayRequest gatewayRequest : requests) {
            HttpEntity<PushRequest> entity = new HttpEntity<>(new PushRequest(from, gatewayRequest.getReceiver().getNumber(),
                    gatewayRequest.getText()), headers);
            String response = restTemplate.postForObject(uri, entity, String.class);
            LOG.info(response);
        }
    }
}
