package com.zhengcq.srv.core.mq.amqp.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.zhengcq.srv.core.mq.domain.QueueMessage;
import com.zhengcq.srv.core.mq.domain.enums.ConsumeStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultQueueConsumer extends BaseQueueConsumer {


	public DefaultQueueConsumer(Channel c, String q, ISimpleMessageListener listener) throws Exception {
		super(c, q, listener);
	}

	@Override
    public ConsumeStatus processData(String consumerTag,
							Envelope envelope,
							AMQP.BasicProperties properties,
							byte[] body) {
		try{
			QueueMessage msg = new QueueMessage(envelope.getRoutingKey(), envelope.getRoutingKey(), properties.getMessageId(), body);
			return this.getListener().consumeMessage(msg);
		} catch (Exception ex) {
			log.error("Processing Data filed ", ex);
			return ConsumeStatus.FAILED;
		}


	}
};
