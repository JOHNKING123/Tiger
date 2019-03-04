package com.zhengcq.srv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class TgDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgDiscoveryApplication.class, args);
	}

}
