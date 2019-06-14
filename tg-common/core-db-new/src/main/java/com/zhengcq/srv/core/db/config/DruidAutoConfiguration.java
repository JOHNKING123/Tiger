package com.zhengcq.srv.core.db.config;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author liuzh
 * @since 2017/2/5.
 */
@Configuration
@EnableConfigurationProperties(DruidProperties.class)
@ConditionalOnClass(DruidDataSource.class)
//@ConditionalOnProperty(prefix = "druid", name = "url")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class DruidAutoConfiguration {

    @Autowired
    private DruidProperties properties;

    private DataSourceProperties dp;


//    <bean id="logFilter" class="com.alibaba.druid.filter.logging.Slf4jLogFilter">
//    <property name="statementExecutableSqlLogEnable" value="false" />
//    </bean>

    @Bean
    public Slf4jLogFilter slf4jLogFilter(){
        Slf4jLogFilter filter = new Slf4jLogFilter();
        filter.setStatementExecutableSqlLogEnable(true);
        return filter;
    }

    @Bean(initMethod = "init", destroyMethod = "close", name = "mysqlDataSource")
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        if (properties.getInitialSize() > 0) {
            dataSource.setInitialSize(properties.getInitialSize());
        }
        if (properties.getMinIdle() > 0) {
            dataSource.setMinIdle(properties.getMinIdle());
        }
        if (properties.getMaxActive() > 0) {
            dataSource.setMaxActive(properties.getMaxActive());
        }

        dataSource.setTestOnBorrow(properties.isTestOnBorrow());
        try {
            if (!StringUtils.isEmpty(properties.getFilters())){
                dataSource.setFilters(properties.getFilters());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }
}
