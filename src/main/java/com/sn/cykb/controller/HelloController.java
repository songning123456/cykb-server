package com.sn.cykb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author: songning
 * @date: 2020/4/3 19:23
 */
@RestController
@RequestMapping("/hello")
@Slf4j
public class HelloController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @GetMapping("/say")
    public String sayHello() {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("uniId");
        log.info("当前LB: {}", serviceInstance.getUri().getAuthority());
        Map result = restTemplate.getForObject("http://uniId/uni-id/generate/id", Map.class);
        System.out.println(result);
        return result.toString();
    }
}
