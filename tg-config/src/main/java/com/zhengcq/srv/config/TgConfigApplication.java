package com.zhengcq.srv.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class TgConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgConfigApplication.class, args);
	}

}
