package com.zhengcq.srv.client1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan({"com.zhengcq.srv.client1", "com.zhengcq.srv.core"})
@MapperScan({"com.zhengcq.srv.client1"})
public class TgClient1Application {

	public static void main(String[] args) {
		SpringApplication.run(TgClient1Application.class, args);
	}

}
