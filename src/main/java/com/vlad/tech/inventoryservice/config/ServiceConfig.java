package com.vlad.tech.inventoryservice.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public Gson gson(){
        return new Gson();
    }
}
