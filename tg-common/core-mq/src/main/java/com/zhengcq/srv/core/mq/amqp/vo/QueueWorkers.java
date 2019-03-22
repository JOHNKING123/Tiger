package com.zhengcq.srv.core.mq.amqp.vo;

import com.zhengcq.srv.core.mq.amqp.consumer.processer.IProcessDataService;

import java.util.*;


/**
 * rabbit exchange and queue set
 * 
 * @author jackie.liu
 * 
 */
public class QueueWorkers {

	private int consumerCount = 1;
	private String queueName;
	private Set<String> defaultRoutingKeys = new HashSet<String>();
	private Set<String> routingKeys = new HashSet<String>();
	private Map<String, List<IProcessDataService>> routings = new HashMap<>();
	
	public QueueWorkers(){
		this.consumerCount = 1;
	}

	public int getConsumerCount() {
		return consumerCount;
	}

	public void setConsumerCount(int consumerCount) {
		this.consumerCount = consumerCount;
	}
	
	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public Set<String> getDefaultRoutingKeys() {
		return defaultRoutingKeys;
	}

	public void setDefaultRoutingKeys(Set<String> defaultRoutingKeys) {
		this.defaultRoutingKeys = defaultRoutingKeys;
		this.routingKeys.addAll(defaultRoutingKeys);
	}

	public Set<String> getRoutingKeys() {
		return routingKeys;
	}

	public Map<String, List<IProcessDataService>> getRoutings() {
		return routings;
	}

	public void setRoutings(Map<String, List<IProcessDataService>> routings) {
		this.routings = routings;
		this.routingKeys.addAll(routings.keySet());
	}

	public void addRoutingProcessSrv(String routingKey, IProcessDataService srv){
		List<IProcessDataService> srvs = routings.get(routingKey);
		if(srvs == null){
			srvs = new ArrayList<IProcessDataService>();
			routings.put(routingKey, srvs);
		}

		if(!srvs.contains(srv)){
			srvs.add(srv);
		}
	}


}
