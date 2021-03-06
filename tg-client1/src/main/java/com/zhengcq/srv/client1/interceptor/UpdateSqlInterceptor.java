package com.zhengcq.srv.client1.interceptor;

import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.toolkit.IOUtils;
import com.baomidou.mybatisplus.toolkit.VersionUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class UpdateSqlInterceptor implements Interceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        if (ms.getSqlCommandType() == SqlCommandType.DELETE || ms.getSqlCommandType() == SqlCommandType.UPDATE) {
            Object parameter = invocation.getArgs()[1];
            BoundSql boundSql = ms.getBoundSql(parameter);
           logger.warn(boundSql.getSql());
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
