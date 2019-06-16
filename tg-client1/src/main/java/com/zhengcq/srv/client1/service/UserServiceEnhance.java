package com.zhengcq.srv.client1.service;

import java.io.Serializable;

public class UserServiceEnhance extends UserService {
    @Override
    public void testPostProcessor(String str){

        System.out.println("testPostProcessor-----before");
        super.testPostProcessor(str);
        System.out.println("testPostProcessor-----after");

    }
}
