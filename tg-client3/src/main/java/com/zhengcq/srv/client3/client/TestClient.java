package com.zhengcq.srv.client3.client;

import com.zhengcq.srv.client1.TestService;
//import com.zhengcq.srv.client1.config.TestServiceFallback;
import com.zhengcq.srv.client3.config.TestServiceFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "TG-CLIENT1",fallback = TestServiceFallback.class)
public interface TestClient extends TestService{
}
