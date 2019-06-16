package com.zhengcq.srv.client1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceB {

    @Autowired
    private TestServiceA testServiceA;


}
