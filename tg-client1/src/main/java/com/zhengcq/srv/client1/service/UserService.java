package com.zhengcq.srv.client1.service;

import com.zhengcq.core.server.annotation.LogAnnotation;
import com.zhengcq.srv.client1.dao.UserDao;
import com.zhengcq.srv.client1.model.User;
import com.zhengcq.srv.core.db.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends BaseServiceImpl<UserDao, User> {

    @LogAnnotation
    public void testPostProcessor(String str){
        System.out.println("testPostProcessor-----"+str);
    }

    public User getByUserId(Long userId){
        testPostProcessor("haha");
        return baseDao.getByUserId(userId);
    }
}
