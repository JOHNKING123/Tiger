package com.zhengcq.srv.client1.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class RemoteCallService {

    private ConcurrentHashMap<String,CompletableFuture<String>> futureMap = new ConcurrentHashMap<>();

    public String getOrder(String orderNo) throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = new CompletableFuture<>();

        futureMap.put(orderNo,future);

        return future.get();
    }

    public void finishRequest(){
        for(String orderNo:futureMap.keySet()){
            CompletableFuture<String> future = futureMap.get(orderNo);

            future.complete(orderNo);
        }
    }
}
