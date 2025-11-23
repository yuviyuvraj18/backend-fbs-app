package com.fbs.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.fbs.app"})

public class FBS_APIApplication {

    public static void main(String[] args) {
        SpringApplication.run(FBS_APIApplication.class, args);

    }
}
