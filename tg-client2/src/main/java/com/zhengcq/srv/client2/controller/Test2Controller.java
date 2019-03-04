package com.zhengcq.srv.client2.controller;

import com.zhengcq.srv.client2.client.TestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test2")
public class Test2Controller {

    @Autowired
    private TestClient testClient;


    @GetMapping("/say-hello")
    public String sayHello(){

        String name = testClient.hello();

        if(name == null){
            name = "none";
        }
        return name+" say hello";
    }
}
