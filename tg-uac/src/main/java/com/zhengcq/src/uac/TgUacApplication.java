package com.zhengcq.src.uac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@EnableAuthorizationServer
@SpringBootApplication
public class TgUacApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgUacApplication.class, args);
	}

}
