package com.yo.webtoon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ElasticConfig {

    @Value("${spring.elasticsearch.uris}")
    private String uri;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .rootUri(uri)
            .build();
    }
}
