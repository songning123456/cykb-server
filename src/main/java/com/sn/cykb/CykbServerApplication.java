package com.sn.cykb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author songning
 */
@EnableAsync
@SpringBootApplication
public class CykbServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CykbServerApplication.class, args);
    }
}
