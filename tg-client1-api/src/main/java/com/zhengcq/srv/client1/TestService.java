package com.zhengcq.srv.client1;


import org.springframework.web.bind.annotation.GetMapping;

public interface TestService {
    String ROOT_PATH = "/test";


    @GetMapping(value = ROOT_PATH+"/hello")
    String hello();
}
