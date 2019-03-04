package com.zhengcq.srv.client1.controller;

import com.zhengcq.srv.client1.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RefreshScope
public class TestController implements TestService {

    @Value("${userName}")
    private String userName;

    @GetMapping("/hello")
    public String hello(){
        return userName;
    }
}
