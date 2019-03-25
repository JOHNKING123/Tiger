package com.zhengcq.srv.client1.listener;

import com.zhengcq.srv.client1.config.QueueConfig;
import com.zhengcq.srv.core.mq.amqp.consumer.ISimpleMessageListener;
import com.zhengcq.srv.core.mq.domain.QueueMessage;
import com.zhengcq.srv.core.mq.domain.enums.ConsumeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestMqListener implements ISimpleMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(TestMqListener.class);
    @Override
    public ConsumeStatus consumeMessage(QueueMessage message) {
        byte[] body = message.getBody();
        logger.warn("自定义处理 接受消息："+(new String(body)));
        return ConsumeStatus.SUCCESS;
    }
}
