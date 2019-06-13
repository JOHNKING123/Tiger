package com.zhengcq.srv.client1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
@EnableEurekaClient
public class TgClient1Application {

	public static void main(String[] args) {
		SpringApplication.run(TgClient1Application.class, args);
	}

}
