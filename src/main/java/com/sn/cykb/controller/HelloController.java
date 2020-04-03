package com.sn.cykb.controller;

import com.sn.cykb.annotation.AControllerAspect;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: songning
 * @date: 2020/4/3 19:23
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @AControllerAspect(description = "测试接口是否成功连接")
    @GetMapping("/say")
    public String sayHello() {
        return "say hello!!!";
    }
}
