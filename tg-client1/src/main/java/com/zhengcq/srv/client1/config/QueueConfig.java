package com.zhengcq.srv.client1.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.zhengcq.srv.client1.listener.TestMqListener;
import com.zhengcq.srv.core.mq.amqp.consumer.DefaultQueueConsumer;
import com.zhengcq.srv.core.mq.amqp.consumer.RabbitConsumerMaker;
import com.zhengcq.srv.core.mq.amqp.producer.RabbitMqProducer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//import com.yoorstore.srv.msg.mq.RabbitMqProducer;

/**
 * Created by clude on 8/24/18.
 */

@Slf4j
@Configuration
public class QueueConfig {

    private static final Logger logger = LoggerFactory.getLogger(QueueConfig.class);

    @Autowired
    RabbitProperties rabbitProperties;

    @Bean
    ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitProperties.getHost());
        factory.setPort(rabbitProperties.getPort());
        factory.setUsername(rabbitProperties.getUsername());
        factory.setPassword(rabbitProperties.getPassword());
        factory.setVirtualHost(rabbitProperties.getVirtualHost());

        return factory;
    }

    @Bean("cachingConnectionFactory")
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory cachingConnectionFactory=new CachingConnectionFactory();
        cachingConnectionFactory.setHost(rabbitProperties.getHost());
        cachingConnectionFactory.setPort(rabbitProperties.getPort());
        cachingConnectionFactory.setUsername(rabbitProperties.getUsername());
        cachingConnectionFactory.setPassword(rabbitProperties.getPassword());
        cachingConnectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
        cachingConnectionFactory.setPublisherConfirms(true);


        return cachingConnectionFactory;
    }

    @Bean
    RabbitMqProducer getRabbitPublishService() {
        RabbitMqProducer producer = new RabbitMqProducer(getConnectionFactory(), 10);
        return producer;
    }

    @Bean
    RabbitConsumerMaker getRabbitConsumerMaker(){
        RabbitConsumerMaker rabbitConsumerMaker = new RabbitConsumerMaker(getConnectionFactory(),10);

        return rabbitConsumerMaker;
    }

   @Bean
    DefaultQueueConsumer getDefaultQueueConsumer(RabbitConsumerMaker rabbitConsumerMaker, TestMqListener listener) throws Exception {
        DefaultQueueConsumer defaultQueueConsumer = rabbitConsumerMaker.createDefaultQueueConsumer("testMsg","testMsqQueueB","testMsg",listener);
        return defaultQueueConsumer;
   }

   @Bean
    public TopicExchange defaultExchange() {
        return new TopicExchange("testMsg");
    }
    /**
     * 获取队列A
     * @return
     */
    @Bean
    public Queue queueA() {
        return new Queue("testMsqQueueA", true); //队列持久
    }
    @Bean
    Binding bindingExchangeA() {
        return BindingBuilder.bind(queueA()).to(defaultExchange()).with("testMsg");
    }
   // @Bean
    public SimpleMessageListenerContainer messageListenerContainer( CachingConnectionFactory connectionFactory ){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        // 监听队列的名称
        container.setQueues(queueA());
        container.setExposeListenerChannel(true);
        // 设置每个消费者获取的最大消息数量
        container.setPrefetchCount(100);
        // 消费者的个数
        container.setConcurrentConsumers(1);
        // 设置确认模式为手工确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(new ChannelAwareMessageListener(){

            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                byte[] body = message.getBody();
                logger.info("接收到消息:" + new String(body));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }
        });
        return container;
    }

//    @Bean
//    RabbitMqProducer getRabbitPublishService() {
//        RabbitMqProducer publishService = new RabbitMqProducer(getConnectionFactory(), 10);
//        return publishService;
//    }

}
