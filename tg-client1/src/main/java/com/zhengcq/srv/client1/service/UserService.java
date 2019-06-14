package com.zhengcq.srv.client1.service;

import com.zhengcq.srv.client1.dao.UserDao;
import com.zhengcq.srv.client1.model.User;
import com.zhengcq.srv.core.db.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseServiceImpl<UserDao, User> {
}
