package com.zhengcq.srv.client1.dao;

import com.zhengcq.srv.client1.model.User;
import com.zhengcq.srv.core.db.annotation.MyBatisDao;
import com.zhengcq.srv.core.db.base.BaseDao;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface UserDao extends BaseDao<User> {

    User getByUserId(@Param("userId")Long userId);
}
