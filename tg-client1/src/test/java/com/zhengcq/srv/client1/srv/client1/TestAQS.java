package com.zhengcq.srv.client1.srv.client1;

import com.zhengcq.srv.core.common.lock.LimitCountDown;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest
public class TestAQS {

    private volatile LimitCountDown limitCountDown = null;

    private int countDown = 0;

    @Test
    public void testAQS(){
        limitCountDown = new LimitCountDown(5L);

        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(10);
        for(int i= 0;i<10;i++){
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
    public static int count = 0;
    @Test
    public void testTmp1() {
        ReentrantLock lock = new ReentrantLock(true);
        Condition condition = lock.newCondition();

        Thread aThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 100) {
                    try {
                        lock.lock();
                        if (count % 2 == 0) {
                            System.out.println("a");
                            count++;
                            condition.signal();
                        } else {
                            condition.await();
                        }
//                        System.out.println("a:count:" + count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        });
        Thread bThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 100) {
                    try {
                        lock.lock();
                        if (count % 2 == 1) {
                            System.out.println("b");
                            count++;
                            condition.signal();
                        } else {
                            condition.await();
                        }
//                        System.out.println("b:count:" + count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }

            }
        });
        aThread.start();
        bThread.start();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean aFunc() {
        System.out.println("a");
        return false;
    }
    public static boolean bFunc() {
        System.out.println("b");
        return true;
    }
    @Test
    public void testTmp2() {
        AtomicBoolean flag = new AtomicBoolean(true);
        Thread aThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        flag.compareAndSet(true, aFunc());
//                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread bThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        flag.compareAndSet(false, bFunc());
//                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        aThread.start();
        bThread.start();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
