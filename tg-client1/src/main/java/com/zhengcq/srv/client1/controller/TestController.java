package com.zhengcq.srv.client1.controller;

import com.zhengcq.core.server.base.BaseController;
import com.zhengcq.srv.client1.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tg-client1/test")
@RefreshScope
public class TestController extends BaseController implements TestService {

    @Value("${userName}")
    private String userName;

    @GetMapping("/hello")
    public String hello(){

        logger.warn("test test warn");
        logger.info("test test info");

        logger.debug("test test debug");
        logger.error("test test error");
        return userName;
    }
}
