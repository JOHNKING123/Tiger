package com.zhengcq.srv.core.mq.amqp.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.zhengcq.srv.core.mq.amqp.service.base.AbstractAmqpService;
import com.zhengcq.srv.core.mq.enums.ExchangeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.zhengcq.srv.core.message.task.amqp.controller.QueueConfigCtrl;

public class RabbitConsumerMaker extends AbstractAmqpService {
	private List<Consumer> consumers = new ArrayList<>();

	public RabbitConsumerMaker(ConnectionFactory connectionFactory, Integer threadSize) {
		super(connectionFactory, threadSize);
	}

	public void createConsumer(String exchange, String queue, String routingKey, ISimpleMessageListener listener, int consumerCount, boolean isReliable) throws Exception {
		for (int i =0; i< consumerCount; i++) {
			Channel channel = this.getConnection().createChannel();
			// declare queue if not exists
			this.queueDeclare(channel, exchange, queue, routingKey);
			if(isReliable) {
				Consumer consumer = new ReliableQueueConsumer(channel, queue, listener);
				consumers.add(consumer);
			} else {
				Consumer consumer = new DefaultQueueConsumer(channel, queue, listener);
				consumers.add(consumer);
			}

		}
	}


	private void queueDeclare(Channel channel, String exchange, String queue, String routingKey) throws Exception{
		// declare queu if not exists
		channel.exchangeDeclare(exchange, ExchangeType.EX_TYPE_TOPIC.getDisplayName().toLowerCase(), true);
		Map<String, Object> args = new HashMap<>();
		channel.queueDeclare(queue, AMQP_DURABLE, AMQP_EXCLUSIVE, AMQP_AUTO_DELETE, args);
		channel.queueBind(queue, exchange, routingKey);
	}


	@Override
	protected void run() throws Exception {
		// Subscribes();
	}

	@Override
	protected void stop() throws Exception {
		logger.info("RabbitMQ service start to stop subscribe service!");
//		for(Consumer consumer: consumers){
//			consumer.handleShutdownSignal();
//		}
		logger.info("RabbitMQ service success to stop subscribe service!");

	}

}
