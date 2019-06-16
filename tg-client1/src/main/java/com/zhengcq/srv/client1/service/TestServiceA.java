package com.zhengcq.srv.client1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceA {

    @Autowired
    private TestServiceB testServiceB;

}
