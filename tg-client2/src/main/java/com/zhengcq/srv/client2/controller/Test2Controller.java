package com.zhengcq.srv.client2.controller;

import com.zhengcq.core.server.base.BaseController;
import com.zhengcq.srv.client2.client.TestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/tg-client2/test2")
public class Test2Controller extends BaseController{

    @Autowired
    private TestClient testClient;


    @GetMapping("/say-hello")
    public String sayHello(HttpServletRequest request){

        Object accessToken = request.getParameter("access_token");
        String accessTokenStr = "";
        if(accessToken != null){
            accessTokenStr = (String)accessToken;
        }
        String name = testClient.hello(accessTokenStr);

        if(name == null){
            name = "none";
        }
        return name+" say hello";
    }

    @GetMapping("/say-hi")
    public String sayHi(){
        logger.warn("test test warn");
        logger.info("test test info");

        logger.debug("test test debug");
        logger.error("test test error");
        return "hi,john";
    }
}
