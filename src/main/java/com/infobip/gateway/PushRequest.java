package com.infobip.gateway;

/**
 * Created by grumin on 11.8.2016.
 */
public class PushRequest {

    private final String from;
    private final String to;
    private final String text;

    public PushRequest(String from, String to, String text) {
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

    @Override
    public String toString() {
        return "PushRequest{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
