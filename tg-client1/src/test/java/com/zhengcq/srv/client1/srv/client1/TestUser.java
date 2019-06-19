package com.zhengcq.srv.client1.srv.client1;

import com.zhengcq.srv.client1.model.User;
import com.zhengcq.srv.client1.service.UserService;
import com.zhengcq.srv.core.db.annotation.MyBatisDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUser {
    @Autowired
    private UserService userService;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void  testSessionCache(){

       SqlSession sqlSession1 =  sqlSessionFactory.openSession(true);
        SqlSession sqlSession2 =  sqlSessionFactory.openSession(true);
       User user1 = (User)sqlSession1.selectOne("com.zhengcq.srv.client1.dao.UserDao.selectById",6);

       System.out.println(user1);

        User user3 = (User)sqlSession2.selectOne("com.zhengcq.srv.client1.dao.UserDao.selectById",6);
        System.out.println(user3);

        user3.setName("tony");
        sqlSession2.update("com.zhengcq.srv.client1.dao.UserDao.updateById",user3);
        sqlSession2.commit();

       User user2 = (User)sqlSession1.selectOne("com.zhengcq.srv.client1.dao.UserDao.selectById",6);
        System.out.println(user2);


//        User user1 = userService.getByUserId(6L);
//        System.out.println(user1);
//        User user2 = userService.getByUserId(6L);
//        System.out.println(user2);
//        System.out.println(1);
    }
}
