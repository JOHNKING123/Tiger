package com.zhengcq.srv.client3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan({ "com.zhengcq.srv.core","com.zhengcq.srv.client3"})
public class TgClient3Application {

	public static void main(String[] args) {
		SpringApplication.run(TgClient3Application.class, args);
	}

}
