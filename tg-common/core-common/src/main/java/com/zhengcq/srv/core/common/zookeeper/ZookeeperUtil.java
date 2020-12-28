package com.zhengcq.srv.core.common.zookeeper;

import lombok.Data;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName: ZookeeperUtil
 * @Description: todo
 * @Company: 广州市两棵树网络科技有限公司
 * @Author: zhengcq
 * @Date: 2020/12/22
 */
public class ZookeeperUtil {


    public static Integer i = 0;
    public static void main(String[] args) {
        String connectionInfo = "192.168.253.3:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString(connectionInfo)
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();
        String path = "/tg/tmp";
        client.start();
        try {
//            client.create()
//                    .creatingParentContainersIfNeeded()
//                    .withMode(CreateMode.EPHEMERAL)
//                    .forPath(path, "temp".getBytes());
//            client.create()
//                    .withMode(CreateMode.EPHEMERAL)
//                    .forPath(path + "/node1", "node1".getBytes());
//            client.getData().usingWatcher(new Watcher() {
//                @Override
//                public void process(WatchedEvent watchedEvent) {
//                    System.out.println(watchedEvent.getPath() + "   " + watchedEvent.getType());
//                    if (watchedEvent.getType().equals(Event.EventType.NodeDataChanged)) {
//                        System.out.println("data changed");
//                        try {
//                            client.getData().usingWatcher(this).forPath(path);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).forPath(path);

//            TreeCache treeCache = new TreeCache(client, path);
//            treeCache.start();
//            Stat stat = client.checkExists().forPath("/tg/testNode");
//            if (stat == null) {
//                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/tg/testNode", "init".getBytes());
//            }
//            treeCache.getListenable().addListener(new TreeCacheListener() {
//                @Override
//                public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
//                    System.out.println(treeCacheEvent.getType() +  "   " + treeCacheEvent.getData());
//                }
//            });
            String lockPath = "/tg/lock";
            CountDownLatch start = new CountDownLatch(1);
            CountDownLatch end = new CountDownLatch(10);
            InterProcessMutex lock = new InterProcessMutex(client, lockPath);
//            client.create()
//                    .creatingParentContainersIfNeeded()
//                    .withMode(CreateMode.PERSISTENT)
//                    .forPath("/tg/order", "1/1000".getBytes());
            for (int j = 0; j < 10; j++) {
                String nodeName = "node";

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String realNodePath = nodeName;
                        try {

                            start.await();
//                            realNodePath = lock(client, lockPath, nodeName);
//                            lock.acquire();
//                            System.out.println(Thread.currentThread().getName() + "   " + ++i);
                            SegmentId segmentId = getDistributeSegmentId(client, "/tg/order");
                            System.out.println(segmentId);

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                Thread.sleep(500);
//                                unlock(client, lockPath, realNodePath);
//                                lock.release();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        end.countDown();
                    }
                });
                thread.start();
            }
            start.countDown();
            end.await();
            System.out.println("end");

            Stat stat = client.checkExists().forPath("/checkNode");
            System.out.println(stat);
            TimeUnit.SECONDS.sleep(60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String  lock(CuratorFramework client, String path, String nodeName) throws Exception {
//        Stat stat = client.checkExists().forPath(path);
//        if (stat == null) {
//            client.create()
//                    .creatingParentContainersIfNeeded()
//                    .withMode(CreateMode.PERSISTENT)
//                    .forPath(path, "lock".getBytes());
//        }
        String nodePath = path + "/" + nodeName;
        String realNodePath = client.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(nodePath, nodePath.getBytes());
        System.out.println(Thread.currentThread().getName() + " : " + realNodePath);
        List<String> childs = client.getChildren().forPath(path);
        Collections.sort(childs, (o1, o2) -> {
            return o1.compareTo(o2);
        });
        System.out.println(Thread.currentThread().getName() + " : " + realNodePath + "; childs: " + childs);
        if (checkGetLock(childs, realNodePath)) {
            return realNodePath;
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        String realNodeName = getRealNodeName(realNodePath);
        String watchNodeName = "";
        for (int i = childs.size() - 1; i >= 0; i--) {
            if (childs.get(i).compareTo(realNodeName) < 0) {
                watchNodeName = childs.get(i);
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + "[" + realNodeName + "]" + " watch " + watchNodeName);
        Stat stat = client.checkExists().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(realNodePath + " event " + watchedEvent.getType());
                if (watchedEvent.getType().equals(Event.EventType.NodeDeleted)) {
                    try {
                        List<String> childsTmp = client.getChildren().forPath(path);
                        Collections.sort(childsTmp, (o1, o2) -> {
                            return o1.compareTo(o2);
                        });
                        System.out.println("watch event realNode:" + realNodePath + ", child: " + childsTmp);
                        if (checkGetLock(childsTmp, realNodePath)) {
                            countDownLatch.countDown();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).forPath(path + "/" + watchNodeName);
        if (stat == null) {
            return realNodePath;
        }
        countDownLatch.await();
        return realNodePath;
    }


      public static boolean checkGetLock(List<String> childs, String nodePath) {
        if (childs != null && !childs.isEmpty() && getRealNodeName(nodePath).equals(childs.get(0))) {
            return true;
        }
        return false;
    }

    public static String getRealNodeName(String nodePath) {
        String[] tmps = nodePath.split("/");
        return tmps[tmps.length -1];
    }

    public static void unlock(CuratorFramework client, String path, String nodePath) throws Exception {
        System.out.println("unlock " + nodePath);
        Stat stat = client.checkExists().forPath(path);
        if (stat != null) {
            Stat nodeStat = client.checkExists().forPath(nodePath);
            if (nodeStat != null) {
                client.delete().forPath(nodePath);
            }
        }
    }



    @Data
    public static class SegmentId {
        private Long min;
        private Long max;
        private AtomicLong current;
        public SegmentId(Long min, Long max) {
            this.max = max;
            this.min = min;
            this.current = new AtomicLong(min);
        }

        public long getNextId() {
            long nextId = current.addAndGet(1L);
            if (nextId > max) {
                return -1;
            }
            return nextId;
        }

        @Override
        public String toString() {
            return String.format("max:%s min:%s  cur:%s", max, min, current.get());
        }
    }

    public static SegmentId getDistributeSegmentId(CuratorFramework client, String bizPath) throws Exception {
        String nodePath = bizPath + "/node";
        String realNodePath = client.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(nodePath, nodePath.getBytes());
        System.out.println(Thread.currentThread().getName() + " : " + realNodePath);
        List<String> childs = client.getChildren().forPath(bizPath);
        Collections.sort(childs, (o1, o2) -> {
            return o1.compareTo(o2);
        });
        System.out.println(Thread.currentThread().getName() + " : " + realNodePath + "; childs: " + childs);
        if (checkGetLock(childs, realNodePath)) {
            SegmentId segmentId = getByBizPath(client, bizPath);
            client.delete().forPath(realNodePath);
            return segmentId;
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        String realNodeName = getRealNodeName(realNodePath);
        String watchNodeName = "";
        for (int i = childs.size() - 1; i >= 0; i--) {
            if (childs.get(i).compareTo(realNodeName) < 0) {
                watchNodeName = childs.get(i);
                break;
            }
        }
        Stat stat = client.checkExists().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(realNodePath + " event " + watchedEvent.getType());
                if (watchedEvent.getType().equals(Event.EventType.NodeDeleted)) {
                    try {
                        List<String> childsTmp = client.getChildren().forPath(bizPath);
                        Collections.sort(childsTmp, (o1, o2) -> {
                            return o1.compareTo(o2);
                        });
                        System.out.println("watch event realNode:" + realNodePath + ", child: " + childsTmp);
                        if (checkGetLock(childsTmp, realNodePath)) {
                            countDownLatch.countDown();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).forPath(bizPath + "/" + watchNodeName);
        countDownLatch.await();
        SegmentId segmentId = getByBizPath(client, bizPath);
        client.delete().forPath(realNodePath);
        return segmentId;
    }

    public static SegmentId getByBizPath(CuratorFramework client, String bizPath) throws Exception {
        String data = new String(client.getData().forPath(bizPath));
        Long tmpId = -1L;
        if (!data.isEmpty() && data.contains("/")) {
            String[] tmpStrs = data.split("/");
            Long min = Long.valueOf(tmpStrs[0]);
            Long max = Long.valueOf(tmpStrs[1]);
            SegmentId segmentId = new SegmentId(min, max);
            Long minTmp = max + 1L;
            Long maxTmp = max + 1000L;
            client.setData().forPath(bizPath, String.format("%s/%s", minTmp, maxTmp).getBytes());
            return segmentId;
        }
        return null;
    }
}
