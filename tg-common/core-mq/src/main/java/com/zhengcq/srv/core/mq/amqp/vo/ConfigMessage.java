package com.zhengcq.srv.core.mq.amqp.vo;

import java.util.List;

public class ConfigMessage {
	private String exchangeName;
	private List<QueueWorkers> queues; // All queue and routingKey
	
	private String dlExchangeName;  // dead letter exchange name
	private List<QueueWorkers> dlQueues; // All queue and routingKey
	

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public List<QueueWorkers> getQueues() {
		return queues;
	}

	public void setQueues(List<QueueWorkers> queues) {
		this.queues = queues;
	}

	public String getDlExchangeName() {
		return dlExchangeName;
	}

	public void setDlExchangeName(String dlExchangeName) {
		this.dlExchangeName = dlExchangeName;
	}

	public List<QueueWorkers> getDlQueues() {
		return dlQueues;
	}

	public void setDlQueues(List<QueueWorkers> dlQueues) {
		this.dlQueues = dlQueues;
	}

	

}
