package com.infobip.config;

import com.infobip.gateway.Gateway;
import com.infobip.gateway.custom.SMSGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    private static final String smsUrl = "https://api.infobip.com/sms/1/text/single";
    private static final String smsAuthorizationType = "Basic";
    private static final String smsAuthorizationKey = "dmJpbGljOTE6MTIzNDU2Nzg5MA==";

    @Bean
    public Gateway smsGateway() {
        return new SMSGateway(smsUrl, smsAuthorizationType, smsAuthorizationKey);
    }
}
