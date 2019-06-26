package com.zhengcq.srv.client1.controller;

import com.zhengcq.core.server.annotation.LogAnnotation;
import com.zhengcq.core.server.base.BaseController;
import com.zhengcq.srv.client1.TestService;
import com.zhengcq.srv.client1.model.User;
import com.zhengcq.srv.client1.service.UserService;
import com.zhengcq.srv.core.common.entity.JsonResult;
import com.zhengcq.srv.core.mq.amqp.consumer.RabbitConsumerMaker;
import com.zhengcq.srv.core.mq.amqp.producer.RabbitMqProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/tg-client1/test")
@RefreshScope
public class TestController extends BaseController implements TestService {

    @Value("${userName}")
    private String userName;

    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    @Autowired
    private UserService userService;


    @GetMapping("/hello")
    public String hello(@RequestParam(required = false,value = "access_token")String accessToken){

        logger.warn("test test warn");
        logger.info("test test info");

        logger.debug("test test debug");
        logger.error("test test error");
        return userName;
    }

    @PostMapping("/test-mq")
    public JsonResult testMq(@RequestParam("msg")String msg){


        rabbitMqProducer.Publish(msg.getBytes(),"testMsg","testMsg",0);
        return JsonResult.ok(true);
    }

    @PostMapping("/test-mq-consumer")
    public JsonResult testMqConsumer(){
	
	System.out.println("this is vi edit");

        return JsonResult.ok(true);
    }


    @GetMapping("/get-user")
    public JsonResult<User>  getUser(@RequestParam("userId")Long userId){

//        User user = userService.selectById(userId);
        logger.warn("test arthas haha");
        userService.testPostProcessor(userName);
        userService.selectById(6L);
       User user =  userService.getByUserId(6L);
       user.setName("nikou");
       userService.updateById(user);
        return JsonResult.ok(new User());
    }


    @GetMapping("/test-session")
    public String testSession(HttpServletRequest request){
        Object o = request.getSession().getAttribute("springboot");
        if(o == null){
            o = "spring boot 牛逼了!!!有端口"+request.getLocalPort()+"生成";
            request.getSession().setAttribute("springboot", o);
        }

        return "端口=" + request.getLocalPort() +  " sessionId=" + request.getSession().getId() +"<br/>"+o;
    }
}
