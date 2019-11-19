package com.nari.monitorresources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitorResourcesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorResourcesApplication.class, args);
    }

}
