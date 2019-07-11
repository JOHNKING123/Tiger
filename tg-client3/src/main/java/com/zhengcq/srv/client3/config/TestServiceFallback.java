package com.zhengcq.srv.client3.config;

import com.zhengcq.srv.client1.TestService;
import org.springframework.stereotype.Component;

@Component
public class TestServiceFallback implements TestService {

    @Override
    public String hello(String accessToken) {
        return "hystrix error,say no";
    }
}
