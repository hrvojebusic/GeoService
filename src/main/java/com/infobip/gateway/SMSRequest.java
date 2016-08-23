package com.infobip.gateway;

/**
 * Created by hbusic on 11.8.2016.
 */
public class SMSRequest {

    private String from;
    private String to;
    private String text;

    public SMSRequest(String from, String to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }
}
