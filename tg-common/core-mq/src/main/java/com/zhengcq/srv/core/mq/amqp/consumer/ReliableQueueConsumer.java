package com.zhengcq.srv.core.mq.amqp.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.zhengcq.srv.core.mq.domain.QueueMessage;
import com.zhengcq.srv.core.mq.domain.enums.ConsumeStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReliableQueueConsumer extends BaseQueueConsumer {

    public ReliableQueueConsumer(Channel c, String q, ISimpleMessageListener listener) throws Exception {
        super(c, q, listener);
    }

    @Override
    public ConsumeStatus processData(String consumerTag,
							Envelope envelope,
							AMQP.BasicProperties properties,
							byte[] body) {
		try{
			String[] arrTopicTags = envelope.getRoutingKey().split("\\.");
			if(arrTopicTags.length != 2) {
				throw new RuntimeException("Invalid RoutingKey Retrieved " + envelope.getRoutingKey());
			}

			String topic = arrTopicTags[0];
			String tag = arrTopicTags[1];
			QueueMessage msg = new QueueMessage(topic, tag, properties.getMessageId(), body);
			return getListener().consumeMessage(msg);
		} catch (Exception ex) {
			log.error("Processing Data filed ", ex);
			return ConsumeStatus.FAILED;
		}
	}
};
