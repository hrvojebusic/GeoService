package com.infobip.gateway.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class SMSGateway {

    @Autowired
    HttpEntity httpEntity;

    private HttpHeaders headers = new HttpHeaders();
}
