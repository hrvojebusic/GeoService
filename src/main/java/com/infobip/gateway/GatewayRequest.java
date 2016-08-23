package com.infobip.gateway;

/**
 * Gateway request.
 * Created by hbusic on 10.8.2016.
 */
public class GatewayRequest {

    private Member receiver;
    private String text;

    public GatewayRequest(Member receiver, String text) {
        this.receiver = receiver;
        this.text = text;
    }

    public Member getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }
}
