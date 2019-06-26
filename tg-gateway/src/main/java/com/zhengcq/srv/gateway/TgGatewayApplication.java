package com.zhengcq.srv.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@ComponentScan({ "com.zhengcq.srv.core"})
public class TgGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgGatewayApplication.class, args);
	}

}
