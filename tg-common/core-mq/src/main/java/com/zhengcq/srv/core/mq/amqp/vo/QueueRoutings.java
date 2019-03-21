package com.zhengcq.srv.core.mq.amqp.vo;

import java.util.HashSet;
import java.util.Set;


/**
 * rabbit exchange and queue set
 * 
 * @author jackie.liu
 * 
 */
public class QueueRoutings {

	private String queueName;
	private String exchangeName;
	private Set<String> routingKeys = new HashSet<String>();

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public Set<String> getRoutingKeys() {
		return routingKeys;
	}

	public void setRoutingKeys(Set<String> routingKeys) {
		this.routingKeys = routingKeys;
	}

	public void addRoutingKey(String routingKey) {
		this.routingKeys.add(routingKey);
	}
}
