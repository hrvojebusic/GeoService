package com.infobip.gateway;


import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Collection;

/**
 * Gateway implementation for SMS notification.
 * Created by hbusic on 11.8.2016.
 */
public class SMSGateway implements Gateway {

    private String apiKey;
    private String url;

    public SMSGateway(String apiKey, String url) {
        this.apiKey = apiKey;
        this.url = url;
    }

    @Override
    public void push(Collection<GatewayRequest> requests) throws IOException {

        for (GatewayRequest r : requests) {

            SMSRequest smsPackage = new SMSRequest(
                    "InfoSMS",
                    r.getReceiver().getNumber(),
                    r.getText());

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString  = mapper.writeValueAsString(smsPackage);

            String phrase = Request.Post(url)
                    .addHeader("Authorization","App " + apiKey)
                    .addHeader("Accept", "application/json")
                    .bodyString(jsonInString, ContentType.APPLICATION_JSON)
                    .execute().returnResponse().getStatusLine().getReasonPhrase();
        }
    }
}
