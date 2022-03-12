package com.lxr.cli.nexus;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class Config {
    @Value("${nexusURL:http://localhost:8081}")
    private String nexusurl;

    Config(){

    }

    @Bean
    @Qualifier("simpleWebClient")
    @Primary
    public WebClient webClient() {
        return WebClient.builder().baseUrl(nexusurl)
                .build();
    }


    @Bean
    @Qualifier("fileDownloadWebClient")
    public WebClient webClientWithLargeBuffer() {
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer ->
                                configurer.defaultCodecs()
                                        .maxInMemorySize(2 * 1024)
                        )
                        .build())
                .build();
    }
}
