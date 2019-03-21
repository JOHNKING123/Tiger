package com.zhengcq.srv.core.mq.base;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ExecutionPool implements IExecutionService {
	
	private Map<String, IExecutionService> services;
	private ExecutorService executorSrv;

	public void setServices(Map<String, IExecutionService> services) {
		this.services = services;
	}

	public ExecutionPool(){
		
	}
	
	public ExecutionPool(Map<String, IExecutionService> services){
		this.services = services;
	}
	
	/**
	 * initialize method 
	 * @PostConstruct
	 */
	public void doStart() throws Exception{
		ExecutorService exeSrv = this.getExecutorSrv();
		if (this.services.size()>0) {
			for (String key : this.services.keySet()) {
				IExecutionService service = this.services.get(key);
				exeSrv.execute(() -> {
					try {
						service.doStart();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
		}
	}

	/**
	 * initialize method
	 * @PostConstruct
	 */
	public void doStop() throws Exception{
		if(executorSrv != null){
			executorSrv.shutdown();
		}
	}

	private ExecutorService getExecutorSrv(){
		if(executorSrv == null || executorSrv.isShutdown()){
			executorSrv = Executors.newFixedThreadPool(this.services.size());
		}
		return executorSrv;
	}

	
}
