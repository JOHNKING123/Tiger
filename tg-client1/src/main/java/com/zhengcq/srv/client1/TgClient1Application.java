package com.zhengcq.srv.client1;

import com.zhengcq.srv.core.db.annotation.MyBatisDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

//@EnableResourceServer  //开启资源保护
@SpringBootApplication
@EnableEurekaClient
@EnableAspectJAutoProxy(proxyTargetClass = false)
@ComponentScan({"com.zhengcq.srv.client1", "com.zhengcq.srv.core","com.zhengcq.core"})
@MapperScan(value = {"com.zhengcq.srv.client1"}, annotationClass = MyBatisDao.class)
public class TgClient1Application {

	public static void main(String[] args) {
		SpringApplication.run(TgClient1Application.class, args);
	}

}
