package com.infobip.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infobip.gateway.sms.SMSGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableScheduling
@Configuration
public class BeanConfiguration {

    private static final Integer EXECUTOR_THREADS = 6;

    private static final String smsUrl = "https://api.infobip.com/sms/1/text/multi";

    private static final String smsAuthorizationType = "Basic";

    private static final String smsAuthorizationKey = "dmJpbGljOTE6MTIzNDU2Nzg5MA==";

    @Bean
    public SMSGateway smsGateway() {
        return new SMSGateway(smsUrl, smsAuthorizationType, smsAuthorizationKey);
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(EXECUTOR_THREADS);
    }

    @Bean
    public ObjectMapper configJackson() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"));
        return objectMapper;
    }
}
