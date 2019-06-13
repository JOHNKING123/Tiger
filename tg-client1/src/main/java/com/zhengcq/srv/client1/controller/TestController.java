package com.zhengcq.srv.client1.controller;

import com.zhengcq.core.server.base.BaseController;
import com.zhengcq.srv.client1.TestService;
import com.zhengcq.srv.core.common.entity.JsonResult;
import com.zhengcq.srv.core.mq.amqp.consumer.RabbitConsumerMaker;
import com.zhengcq.srv.core.mq.amqp.producer.RabbitMqProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tg-client1/test")
@RefreshScope
public class TestController extends BaseController implements TestService {

    @Value("${userName}")
    private String userName;

    @Autowired
    private RabbitMqProducer rabbitMqProducer;


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


        return JsonResult.ok(true);
    }
}
