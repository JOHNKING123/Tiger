package com.zhengcq.srv.core.db.config;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableConfigurationProperties(DruidProperties.class)
//@ImportAutoConfiguration(DruidAutoConfiguration.class)
//@AutoConfigureAfter(DruidAutoConfiguration.class)
public class MybatisPlusConfig {
//	@Autowired
//	@Qualifier("mysqlDataSource")
//	private DataSource dataSource;

	@Autowired
	private MybatisProperties properties;

	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();

//	@Autowired(required = false)
//	private Interceptor[] interceptors;
//
	@Autowired(required = false)
	private DatabaseIdProvider databaseIdProvider;

	/**
	 *	 mybatis-plus分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor page = new PaginationInterceptor();
		page.setDialectType("mysql");
		return page;
	}

	private Interceptor[] getInterceptors(){
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return new Interceptor[]{page};
    }

	/**
	 * 这里全部使用mybatis-autoconfigure 已经自动加载的资源。不手动指定
	 * 配置文件和mybatis-boot的配置文件同步
	 * @return
	 */
	@Bean
	@Primary
	public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() {
		MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
		mybatisPlus.setDataSource(dataSource());
		mybatisPlus.setVfs(SpringBootVFS.class);
		if (StringUtils.hasText(this.properties.getConfigLocation())) {
			mybatisPlus.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		mybatisPlus.setConfiguration(properties.getConfiguration());
//		if (!ObjectUtils.isEmpty(this.interceptors)) {
//			mybatisPlus.setPlugins(this.interceptors);
//		}
		mybatisPlus.setPlugins(this.getInterceptors());

		// MP 全局配置，更多内容进入类看注释
		GlobalConfiguration globalConfig = new GlobalConfiguration();
		globalConfig.setDbType(DBType.MYSQL.name());
		// ID 策略 AUTO->`0`("数据库ID自增") INPUT->`1`(用户输入ID") ID_WORKER->`2`("全局唯一ID") UUID->`3`("全局唯一ID")
		globalConfig.setIdType(2);
		globalConfig.setDbColumnUnderline(true);
		mybatisPlus.setGlobalConfig(globalConfig);
		MybatisConfiguration mc = new MybatisConfiguration();
		mc.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		if(properties.getConfiguration() != null){
			mc.setMapUnderscoreToCamelCase(properties.getConfiguration().isMapUnderscoreToCamelCase());
		}
		mybatisPlus.setConfiguration(mc);
		if (this.databaseIdProvider != null) {
			mybatisPlus.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
			mybatisPlus.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		}
		if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
			mybatisPlus.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
			mybatisPlus.setMapperLocations(this.properties.resolveMapperLocations());
		}
		return mybatisPlus;
	}


	@Autowired
	private DruidProperties druidProp;

	private DataSourceProperties dp;

	@Bean
	public Slf4jLogFilter slf4jLogFilter(){
		Slf4jLogFilter filter = new Slf4jLogFilter();
		filter.setStatementExecutableSqlLogEnable(true);
		return filter;
	}

	@Bean(initMethod = "init", destroyMethod = "close", name = "mysqlDataSource")
	@Primary
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(druidProp.getUrl());
		dataSource.setUsername(druidProp.getUsername());
		dataSource.setPassword(druidProp.getPassword());
		dataSource.setDriverClassName(null);
		if (druidProp.getInitialSize() > 0) {
			dataSource.setInitialSize(druidProp.getInitialSize());
		}
		if (druidProp.getMinIdle() > 0) {
			dataSource.setMinIdle(druidProp.getMinIdle());
		}
		if (druidProp.getMaxActive() > 0) {
			dataSource.setMaxActive(druidProp.getMaxActive());
		}

		dataSource.setTestOnBorrow(druidProp.isTestOnBorrow());
		try {
			if (!com.alibaba.druid.util.StringUtils.isEmpty(druidProp.getFilters())){
				dataSource.setFilters(druidProp.getFilters());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return dataSource;
	}


	@Bean(name = "transactionManager")
    @Primary
	public DataSourceTransactionManager rdsTransactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
//
//	@Bean(name = "sqlSessionFactory")
//    @Primary
//	public SqlSessionFactory rdsSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource kylinDataSource) throws Exception {
//		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//		sessionFactory.setDataSource(kylinDataSource);
//		return sessionFactory.getObject();
//	}
}
