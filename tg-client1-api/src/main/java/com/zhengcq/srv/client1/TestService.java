package com.zhengcq.srv.client1;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface TestService {
    String ROOT_PATH = "/api/tg-client1/test";


    @GetMapping(value = ROOT_PATH+"/hello")
    String hello(@RequestParam(required = false,value = "access_token")String accessToken);
}
