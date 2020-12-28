package com.zhengcq.srv.client1.controller;

import com.zhengcq.core.server.annotation.LogAnnotation;
import com.zhengcq.core.server.base.BaseController;
import com.zhengcq.srv.client1.TestService;
import com.zhengcq.srv.client1.model.User;
import com.zhengcq.srv.client1.service.UserService;
import com.zhengcq.srv.core.common.entity.JsonResult;
import com.zhengcq.srv.core.common.utils.JedisUtils;
import com.zhengcq.srv.core.mq.amqp.consumer.RabbitConsumerMaker;
import com.zhengcq.srv.core.mq.amqp.producer.RabbitMqProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/tg-client1/test")
@RefreshScope
public class TestController extends BaseController implements TestService {

    @Value("${userName}")
    private String userName;

//    @Autowired
//    private RabbitMqProducer rabbitMqProducer;

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


//        rabbitMqProducer.Publish(msg.getBytes(),"testMsg","testMsg",0);
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
    private Integer  shareValue = 0;
    @PostMapping("/test-redis-lock")
    public JsonResult  testRedisLock(@RequestParam("userId")Long userId) throws InterruptedException {
        System.out.println("i am thread:"+Thread.currentThread()+" start to work");
        String uuId = UUID.randomUUID().toString()+Thread.currentThread().toString();
        try {
            boolean lockRs  = JedisUtils.tryLock("shareValue",uuId,3000,50);
//            boolean lockRs = true;
            if(lockRs){
                Thread.sleep(2000);
                if(shareValue == 65){
                    shareValue++;
                    System.out.println("share value:"+shareValue);
                    System.out.println("current value is "+shareValue+",wait");
                    Thread.sleep(120000);
                }else{
                    shareValue++;
                    System.out.println("share value:"+shareValue);
                }


            }else {
                System.out.println("fail to get lock;share value:"+shareValue);
            }
        }catch (InterruptedException e1){
           throw  e1;
        }catch (Exception e){
            throw e;
        }finally {
            JedisUtils.unlock("shareValue",uuId);
        }
//        boolean lockRs1 = JedisUtils.lock("shareValue1","haha",60000);
//        System.out.println("lockRs1:"+lockRs1);


        return JsonResult.ok(true);
    }
}
