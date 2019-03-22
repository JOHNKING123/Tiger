package com.zhengcq.srv.client1.config;

import com.rabbitmq.client.ConnectionFactory;
import com.zhengcq.srv.core.mq.amqp.producer.RabbitMqProducer;
import lombok.extern.slf4j.Slf4j;
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

//    @Bean
//    RabbitMqProducer getRabbitPublishService() {
//        RabbitMqProducer publishService = new RabbitMqProducer(getConnectionFactory(), 10);
//        return publishService;
//    }

}
