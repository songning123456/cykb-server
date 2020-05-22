package com.sn.cykb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author songning
 */
@SpringBootApplication
@EnableEurekaClient
public class CykbServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CykbServerApplication.class, args);
    }
}
