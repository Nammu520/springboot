package com.idea.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages={"com.idea"})
@EnableAsync
public class IdeaWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdeaWebApplication.class, args);
    }

}
