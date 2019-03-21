package com.zhengcq.srv.core.mq.amqp.producer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zhengcq.srv.core.core.utils.StringUtils;
import com.zhengcq.srv.core.mq.amqp.service.base.AbstractAmqpService;

public class RabbitMqProducer extends AbstractAmqpService {

    // AMQP.BasicProperties.Builder().deliveryMode(2)
    // 强制数据包需要持久化
    private AMQP.BasicProperties defaultProp = new AMQP.BasicProperties.Builder().deliveryMode(2).contentType("application/json").contentEncoding("utf-8").build();


    public RabbitMqProducer(ConnectionFactory connectionFactory, Integer threadSize) {
        super(connectionFactory, threadSize);
    }

    /**
     * Send a message to the rabbitMQ, method 2
     * @param data
     * @param routingKey
     */
    public void Publish(byte[] data, String routingKey, String exchange, long msg_ttl ) {
        this.Publish(data, routingKey, exchange, null, msg_ttl);
    }

    /**
     * Send a message to the rabbitMQ, method 2
     * @param data
     * @param routingKey
     */
    public void Publish(byte[] data, String routingKey, String exchange, String messageId, long msg_ttl ) {
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder().deliveryMode(2).contentType( "application/json");
        if(msg_ttl > 0) {
            builder.expiration(String.valueOf(msg_ttl));
        }

        if(!StringUtils.isEmpty(messageId)) {
            builder.messageId(messageId);
        }
        this.Publish(data, routingKey, exchange, builder.build());
    }

    /**
     * Send a message to the rabbitMQ, method 2
     * @param data
     * @param routingKey
     */
    public void Publish(byte[] data, String routingKey, String exchange, AMQP.BasicProperties properties) {
        try {
            logger.debug("RabbitMQ connection to create.");
            Connection connection = this.getConnection();

            Channel channel = connection.createChannel();//Create a channel
            channel.exchangeDeclare(exchange, "topic", true);

            logger.debug("------------------sending data:");
            // Sets the queue to persistent, also need the message is also set to be persistent,
            // channel.basicPublish(exchange, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, data);
            // byte[] messageBodyBytes = "Hello, world!".getBytes();

            if(properties == null){
                AMQP.BasicProperties prop = defaultProp;
                channel.basicPublish(exchange, routingKey, prop, data);
            } else {
                channel.basicPublish(exchange, routingKey, properties, data);
            }

            logger.debug(new String(data));
            channel.close();
            //connection.close();
            logger.debug("RabbitMQ Message sent successfully!");
            //Call reception method
            //new GetData(this.hostName,1).gettingData();
        } catch (Exception e) {
            logger.error("",e);
        }
    }

    @Override
    protected void run() throws Exception {

    }

    @Override
    protected void stop() throws Exception {

    }
}
