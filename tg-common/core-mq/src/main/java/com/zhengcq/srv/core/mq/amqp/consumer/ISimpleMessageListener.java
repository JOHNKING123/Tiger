package com.zhengcq.srv.core.mq.amqp.consumer;

import com.zhengcq.srv.core.mq.domain.QueueMessage;
import com.zhengcq.srv.core.mq.domain.enums.ConsumeStatus;

public interface ISimpleMessageListener {
    ConsumeStatus consumeMessage(QueueMessage message);
}
