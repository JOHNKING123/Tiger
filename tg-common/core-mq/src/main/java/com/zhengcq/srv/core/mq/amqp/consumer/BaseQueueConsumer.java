package com.zhengcq.srv.core.mq.amqp.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.zhengcq.srv.core.mq.domain.QueueMessage;
import com.zhengcq.srv.core.mq.domain.enums.ConsumeStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public abstract class BaseQueueConsumer extends DefaultConsumer {

	private final int MAX_RETRY_COUNT = 3;

	private Channel channel;
	private String queue;
	private ISimpleMessageListener listener;

	public Channel getChannel() {
		return channel;
	}

	public ISimpleMessageListener getListener() {return listener;}

    public BaseQueueConsumer(Channel c, String q, ISimpleMessageListener listener) throws Exception {
        super(c);
        this.channel = c;
        this.queue = q;
		this.listener = listener;
        this.channel.basicConsume(queue, false, this);
    }

    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
    	
    	log.debug(String.format("Start to process route key %s, and tag %d", envelope.getRoutingKey(),envelope.getDeliveryTag()));
		if( listener == null ){
			log.error("Consumer Listener is null, routingkey: {}, tag: {} ", envelope.getRoutingKey(),envelope.getDeliveryTag());
		}

		QueueMessage msg = new QueueMessage(envelope.getRoutingKey(), envelope.getRoutingKey(), properties.getMessageId(), body);
		ConsumeStatus status = processData(consumerTag, envelope, properties, body);

		if(status == ConsumeStatus.SUCCESS){
			channel.basicAck(envelope.getDeliveryTag(), false);
		} else if(status == ConsumeStatus.FAILED) {
			channel.basicReject(envelope.getDeliveryTag(), false);
		} else if(status == ConsumeStatus.RESEND_LATER) {
			// TODO, 需要requeue
			channel.basicReject(envelope.getDeliveryTag(), false);
		}
    }


    public abstract ConsumeStatus processData(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body);
};
