package com.zhengcq.srv.core.mq.base;

/**
 * Created by clude on 7/16/16.
 */
public interface IExecutionService {
    void doStart() throws Exception;
    void doStop() throws Exception;
}
