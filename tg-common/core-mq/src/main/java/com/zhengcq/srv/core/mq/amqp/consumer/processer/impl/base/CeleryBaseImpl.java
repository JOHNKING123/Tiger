package com.zhengcq.srv.core.mq.amqp.consumer.processer.impl.base;

import com.zhengcq.srv.core.mq.amqp.consumer.processer.IProcessDataService;
import com.zhengcq.srv.core.mq.amqp.vo.CeleryTask;

public abstract class CeleryBaseImpl implements IProcessDataService {


	public boolean processData(byte[] data) throws Exception {
		CeleryTask celeryTask = CeleryTask.load(new String(data));

		boolean isSuccess = this.processCeleryTask(celeryTask);

		// TODO: save the task status based on the isSuccess flag

		return isSuccess;
	}
	
	public abstract boolean processCeleryTask (CeleryTask celeryTask) throws Exception;

}
