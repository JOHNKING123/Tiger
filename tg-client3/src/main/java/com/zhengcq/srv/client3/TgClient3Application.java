package com.zhengcq.srv.client3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan({ "com.zhengcq.srv.core","com.zhengcq.srv.client3","com.zhengcq.srv.client3.config"})
@EnableFeignClients(basePackages = {"com.zhengcq.srv.client3"})
@EnableCircuitBreaker
public class TgClient3Application {

	public static void main(String[] args) {
		SpringApplication.run(TgClient3Application.class, args);
	}

}
