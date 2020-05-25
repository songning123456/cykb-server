package com.sn.cykb;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author songning
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class CykbServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CykbServerApplication.class, args);
    }
}
