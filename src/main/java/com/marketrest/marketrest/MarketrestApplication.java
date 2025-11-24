package com.marketrest.marketrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.marketrest")
@EntityScan(basePackages = "com.marketrest.entitys")

public class MarketrestApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketrestApplication.class, args);
    }
}

