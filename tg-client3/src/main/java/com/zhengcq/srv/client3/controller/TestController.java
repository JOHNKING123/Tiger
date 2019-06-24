package com.zhengcq.srv.client3.controller;

import com.zhengcq.core.server.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/client3/test")
public class TestController extends BaseController {

    @GetMapping("/test-session")
    public String testSession(HttpServletRequest request){
        HttpSession session = request.getSession();

        System.out.println("test session;"+session);

        return "hello,";
    }
}
