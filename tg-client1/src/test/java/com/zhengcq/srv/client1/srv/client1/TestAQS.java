package com.zhengcq.srv.client1.srv.client1;

import com.zhengcq.srv.core.common.lock.LimitCountDown;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class TestAQS {

    private volatile LimitCountDown limitCountDown = null;

    private int countDown = 0;

    @Test
    public void testAQS(){
        limitCountDown = new LimitCountDown(5L);

        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(10000);
        for(int i= 0;i<10000;i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        begin.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("thread"+Thread.currentThread()+"begin");
                    countDown++;
                    if(countDown > 5){
                        System.out.println("thread"+Thread.currentThread()+" countDown:"+countDown);
                    }
                    int rs = limitCountDown.countUp();
                    System.out.println("thread"+Thread.currentThread()+"end rs:"+rs);

                    end.countDown();
                }
            });
            thread.start();
        }

        try {
            begin.countDown();
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
