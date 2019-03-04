package com.zhengcq.srv.client2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.zhengcq.srv.client2"})
public class TgClient2Application {

	public static void main(String[] args) {
		SpringApplication.run(TgClient2Application.class, args);
	}

}
