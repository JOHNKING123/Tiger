package com.zhengcq.srv.client3.controller;

import com.zhengcq.core.server.base.BaseController;
import com.zhengcq.srv.client3.client.TestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/tg-client3/test")
public class TestController extends BaseController {

    @Autowired
    private TestClient testClient;

    @GetMapping("/test-session")
    public String testSession(HttpServletRequest request){
        Object o = request.getSession().getAttribute("springboot");
        if(o == null){
            o = "spring boot 牛逼了!!!有端口"+request.getLocalPort()+"生成";
            request.getSession().setAttribute("springboot", o);
        }

        return "端口=" + request.getLocalPort() +  " sessionId=" + request.getSession().getId() +"<br/>"+o;
    }

    @GetMapping("/test-hystrix")
    public String testHystrix(){

        String rs = testClient.hello("xxxx");
        logger.debug("rs:"+rs);
        return "hello,i am fine";
    }
}
