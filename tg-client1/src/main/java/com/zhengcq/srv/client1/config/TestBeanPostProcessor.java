package com.zhengcq.srv.client1.config;

import com.zhengcq.srv.client1.service.UserService;
import com.zhengcq.srv.client1.service.UserServiceEnhance;
import com.zhengcq.srv.core.db.base.BaseServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Date;

public class TestBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
//        System.out.println("postProcessBeforeInitialization----"+s);
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
//        System.out.println("postProcessAfterInitialization----"+s);
        if(s.equals("userService")){
            System.out.println("postProcessAfterInitialization----"+s+",i find you");
            return new UserServiceEnhance();
        }
        return o;
    }
}
