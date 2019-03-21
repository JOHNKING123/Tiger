package com.zhengcq.srv.core.mq.amqp.service.base;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BatchGetAmqpBase<T> extends AbstractAmqpService {

	public BatchGetAmqpBase(ConnectionFactory connectionFactory, Integer threadSize) {
		super(connectionFactory, threadSize);
	}

	public abstract T passData(byte[] data);
	
	public abstract boolean preCheckData(T data);
	
	public abstract boolean processAllData(List<T> datas);

	public abstract String getQueueName();

	public abstract Integer getBatchMessageCount();

	@Override
	public void run() throws Exception {
		Channel channel = this.getConnection().createChannel();
		List<GetResponse> rsps = this.getBatchResponses(channel, this.getQueueName(), this.getBatchMessageCount());
		if(rsps.size() > 0){
			this.processResponses(channel, rsps);
		}
	}

	@Override
	public void stop() throws Exception {

	}

	
	private boolean processResponses(Channel channel, List<GetResponse> rsps){
		try {
			List<T> datas = new ArrayList<T>();
			for(GetResponse rsp: rsps){
				T data = this.passData(rsp.getBody());
				boolean isValid = this.preCheckData(data);
				if(!isValid){
					channel.basicNack(rsp.getEnvelope().getDeliveryTag(), false, true);
				}else{
					datas.add(data);
				}
			}
			if(datas.size() > 0){
				GetResponse lastRsp = rsps.get(rsps.size()-1);
				boolean isSuccess = processAllData(datas);
				if(isSuccess){
					channel.basicAck(lastRsp.getEnvelope().getDeliveryTag(), true);
				}else{
					channel.basicNack(lastRsp.getEnvelope().getDeliveryTag(), true, false);
				}
				return isSuccess;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
		
	}
	
	private List<GetResponse> getBatchResponses(Channel channel, String queueName, int messageCount){
		List<GetResponse> lists = new ArrayList<GetResponse>();
		try {
			//channel = AmqpUtil.getConnection().createChannel();
			for(int i=0; i<= messageCount; i++){
				GetResponse rsp = channel.basicGet(queueName, false);
				if(rsp.getBody() != null){
					lists.add(rsp);
				}
				if(rsp.getMessageCount() == 0){
					break;
				}
			}
			
		} catch (Exception e) {
			logger.error("",e);
		}
		return lists;
	}
	
}
