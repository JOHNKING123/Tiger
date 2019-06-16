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
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
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

//   @Bean
//   public BeanFactoryPostProcessor getTestBeanFactoryPostProcessor(){
//        return new TestBeanFactoryPostProcessor();
//   }
//
   @Bean
   public BeanPostProcessor getTestBeanPostPostProcessor(){
       return new TestBeanPostProcessor();
   }

}
