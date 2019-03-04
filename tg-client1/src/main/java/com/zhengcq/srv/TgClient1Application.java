package com.zhengcq.srv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TgClient1Application {

	public static void main(String[] args) {
		SpringApplication.run(TgClient1Application.class, args);
	}

}
