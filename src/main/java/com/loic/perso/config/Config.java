package com.loic.perso.config;

import com.twilio.sdk.TwilioRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public TwilioRestClient client() {
        return new TwilioRestClient("OMITTED", "OMITTED");
    }
}
