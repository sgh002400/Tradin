package com.tradin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableCaching
@RestController
public class TradinApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradinApplication.class, args);
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Health Check Success";
    }
}
