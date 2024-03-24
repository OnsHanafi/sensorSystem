package com.esprit.sensorsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SensorSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SensorSystemApplication.class, args);
    }

}
