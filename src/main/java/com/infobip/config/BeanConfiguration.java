package com.infobip.config;

import com.infobip.sms.gateway.SMSGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    private static final String smsUrl = "https://api.infobip.com/sms/1/text/multi";
    private static final String smsAuthorizationType = "Basic";
    private static final String smsAuthorizationKey = "dmJpbGljOTE6MTIzNDU2Nzg5MA==";

    @Bean
    public SMSGateway smsGateway() {
        return new SMSGateway(smsUrl, smsAuthorizationType, smsAuthorizationKey);
    }
}
