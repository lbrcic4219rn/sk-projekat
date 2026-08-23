package com.sk.hotelreservationservice.client.userservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class UserServiceClientConfiguration {

    @Bean
    public RestTemplate userServiceRestTemplate(@Value("${userservice.url}") String userServiceUrl) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(userServiceUrl));
        return restTemplate;
    }
}
