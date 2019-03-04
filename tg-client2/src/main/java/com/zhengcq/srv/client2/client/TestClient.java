package com.zhengcq.srv.client2.client;

import com.zhengcq.srv.client1.TestService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("TG-CLIENT1")
public interface TestClient extends TestService{
}
