package com.zhengcq.srv.baseDemo.testDemo;

import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProductorAndConsumer {


    public static void main(String[] args){
        Vector vector = new Vector();

        String s;

        Executor  threadPool  = new ThreadPoolExecutor(3,3,1, TimeUnit.MINUTES,new LinkedBlockingDeque<>());
        threadPool.execute(new Productor(vector));
        threadPool.execute(new Consummer(vector));
//        Thread produceThread = new Thread(new Productor(vector));
//        Thread consummerThread = new Thread(new Consummer(vector));
//
//        produceThread.start();
//        consummerThread.start();
    }


}

class Productor implements Runnable{

    private final Vector vector;

    public Productor(Vector vector){
        this.vector = vector;
    }
    @Override
    public void run() {

        while (true){
            for(int i=0;i<1000;i++){
                System.out.println("produce "+i);
                try {
                    Thread.sleep(500);
                    product(i);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void product(int i) throws InterruptedException {

        while (vector.size() == 7){
            synchronized (vector){
                System.out.println("producer wait for full vector");
                vector.wait();
            }
        }
        synchronized (vector){
            vector.add(i);
            vector.notifyAll();
        }
    }
}


class Consummer implements Runnable{
    private final Vector vector;

    public Consummer(Vector vector){
        this.vector = vector;
    }
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
                consumme();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void consumme() throws InterruptedException {
        while (vector.isEmpty()){
            synchronized (vector){
                System.out.println("consummer wait for empty vector");
                vector.wait();
            }
        }
        synchronized (vector){
            int rs = (Integer)vector.remove(0);
            System.out.println("consumme "+rs);
            vector.notifyAll();
        }
    }
}
