package com.zhengcq.srv.core.common.lock;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class LimitCountDown {

    private class Sync extends AbstractQueuedSynchronizer{

        public int tryAcquireShared(int ignore){

            long newCount = count.incrementAndGet();
            if(newCount > limit){
                return -1;
            }
            return 1;
        }

        public boolean tryReleaseShared(int ignore){
            count.decrementAndGet();

            return true;
        }
    }

    public LimitCountDown(long limit){
        this.count  = new AtomicLong(0);
        this.sync = new Sync();
        this.limit = limit;
    }

    private final Sync sync;

    private final AtomicLong  count ;

    private volatile long  limit;


    public void countDown(){
        sync.tryReleaseShared(0);
    }

    public int  countUp(){
       return sync.tryAcquireShared(0);
    }
}
