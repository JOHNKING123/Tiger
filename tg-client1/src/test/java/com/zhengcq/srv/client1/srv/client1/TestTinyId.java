package com.zhengcq.srv.client1.srv.client1;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.zhengcq.srv.core.common.util.OkHttp3Utils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName: TestTinyId
 * @Description: todo
 * @Company: 广州市两棵树网络科技有限公司
 * @Author: zhengcq
 * @Date: 2020/12/28
 */
public class TestTinyId {

    public static Integer count = 0;
    @Test
    public void testTinyId1() {
        Set<Integer> set = new HashSet<>();
        // 1000 线程同时请求100次
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(1000);
        try {
            for (int j = 0; j < 1000; j++) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            start.await();
                            for (int k = 0; k < 10; k++) {
                                String rs = OkHttp3Utils.get("http://172.18.40.37:9999/tinyid/id/nextId?bizType=test&batchSize=100&token=0f673adf80504e2eaa552f5d791b644c&randomFlag=1");
//                                JSONObject jsonObject = JSONObject.parseObject(rs);
//                                JSONArray jsonArray = (JSONArray)jsonObject.get("data");
//                                synchronized (count) {
////                                    set.addAll(jsonArray.toJavaList(Integer.class));
//                                    count += jsonArray.size();
//                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        end.countDown();
                    }
                });
                thread.start();
            }
            long startTime = System.currentTimeMillis();
            start.countDown();
            end.await();
            long endTime = System.currentTimeMillis();
            System.out.println("end");
            System.out.println("cost time(ms):" + (endTime - startTime));
//            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String rs = OkHttp3Utils.get("http://172.18.40.37:9999/tinyid/id/nextId?bizType=test&batchSize=100&token=0f673adf80504e2eaa552f5d791b644c&randomFlag=1");
//        String rs = "{\"data\":[2202667,2202669,2202676,2202692,2202710,2202731,2202754,2202784,2202823,2202870,2202874,2202883,2202897,2202919,2202941,2202969,2203000,2203036,2203079,2203081,2203088,2203104,2203122,2203143,2203166,2203196,2203235,2203282,2203286,2203295,2203309,2203331,2203353,2203381,2203412,2203448,2203491,2203493,2203500,2203516,2203534,2203555,2203578,2203608,2203647,2203694,2203698,2203707,2203721,2203743,2203765,2203793,2203824,2203860,2203903,2203905,2203912,2203928,2203946,2203967,2203990,2204020,2204059,2204106,2204110,2204119,2204133,2204155,2204177,2204205,2204236,2204272,2204315,2204317,2204324,2204340,2204358,2204379,2204402,2204432,2204471,2204518,2204522,2204531,2204545,2204567,2204589,2204617,2204648,2204684,2204727,2204729,2204736,2204752,2204770,2204791,2204814,2204844,2204883,2204930],\"code\":200,\"message\":\"\"}";
//        JSONObject jsonObject = JSONObject.parseObject(rs);
//        System.out.println(jsonObject);
//
//        JSONArray jsonArray = (JSONArray)jsonObject.get("data");
//        Set<Integer> set = new HashSet<>();
//        set.addAll(jsonArray.toJavaList(Integer.class));
//        System.out.println(set);
    }


    @Test
    public void testZk() {
        String connectionInfo = "192.168.253.3:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString(connectionInfo)
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();
        String path = "/tg/seq";
        client.start();
        try {
            client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path, "testSeq".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPreconditions() {
        Integer a = null;
        Preconditions.checkNotNull(a, "a can not be null");
    }
}
