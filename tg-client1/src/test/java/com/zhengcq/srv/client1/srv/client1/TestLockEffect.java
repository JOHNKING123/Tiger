package com.zhengcq.srv.client1.srv.client1;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class TestLockEffect {


    private int shareValue = 0;

    private final int workers = 1000;

    private final  int  dataCount = 1000;
    @Test
    public void testSync(){
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(workers);
        for(int i = 0 ;i<workers;i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        begin.await();
                        for (int j= 0;j<dataCount;j++){
                            increaseBySync();
                        }

                        end.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
        }

        try {
            long beginTime = new Date().getTime();
            begin.countDown();
            end.await();
            long endTime = new Date().getTime();
            System.out.println("cost time:"+(endTime-beginTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testLock(){
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(workers);
        for(int i = 0 ;i<workers;i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        begin.await();
                        for (int j= 0;j<dataCount;j++){
                            increaseByLock();
                        }

                        end.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
        }

        try {
            long beginTime = new Date().getTime();
            begin.countDown();
            end.await();
            long endTime = new Date().getTime();
            System.out.println("cost time:"+(endTime-beginTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public synchronized void increaseBySync(){

        shareValue++;
        System.out.println("shareValue:"+shareValue);
    }
    private static ReentrantLock lock = new ReentrantLock(false);
    public void increaseByLock(){
        try {
            lock.lock();
            shareValue++;
            System.out.println("shareValue:"+shareValue);
        }catch (Exception e){
            throw e;
        }finally {
            lock.unlock();
        }
    }
}
