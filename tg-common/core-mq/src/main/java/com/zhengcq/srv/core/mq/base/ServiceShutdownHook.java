package com.zhengcq.srv.core.mq.base;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceShutdownHook extends Thread {
    private IExecutionService service;

    public ServiceShutdownHook(IExecutionService service ){
        super();
        this.service = service;
    }

    @Override
    public void run() {
        log.info("Start to stop the service.");
        try {
            service.doStop();
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("Service shutdown");
    }
}