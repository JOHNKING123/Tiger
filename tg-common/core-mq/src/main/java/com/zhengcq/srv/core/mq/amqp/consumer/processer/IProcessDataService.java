package com.zhengcq.srv.core.mq.amqp.consumer.processer;


public interface IProcessDataService {
//	Logger logger = LoggerFactory.getLogger(IProcessDataService.class);
	public boolean processData(byte[] data) throws Exception;
}
