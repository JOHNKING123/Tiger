package com.zhengcq.srv.core.mq.amqp.service.base;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zhengcq.srv.core.mq.base.IExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractAmqpService implements IExecutionService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected static final boolean AMQP_DURABLE = true;
	protected static final boolean AMQP_EXCLUSIVE = false;
	protected static final boolean AMQP_AUTO_DELETE = false;
	protected static final boolean AMQP_AUTO_ACK = false;

	// temp queue
	protected static final boolean AMQP_TEMPQUEUE_AUTO_DELETE = true;
	protected static final boolean AMQP_TEMPQUEUE_EXCLUSIVE  = false;

	protected static final String DEFAULT_EXCHANGE_NAME = "TIGER_DEFAULT_EXCHANGE";

	protected static final String DEFAULT_ROUTING_KEY = "TIGER_DEFAULT_ROUTING_KEY";

	private Connection connection;
	private ExecutorService executorSrv;

	private ConnectionFactory connectionFactory;
	private int threadSize = 0;

	public AbstractAmqpService(ConnectionFactory connectionFactory, Integer threadSize){
		this.connectionFactory = connectionFactory;
		this.threadSize = threadSize;
	}

	public Connection getConnection() throws Exception {
		if(connection == null || !connection.isOpen()){
			if(threadSize > 0){
				executorSrv = Executors.newFixedThreadPool(threadSize);
				connection = connectionFactory.newConnection(executorSrv);
			}else{
				connection = connectionFactory.newConnection();
			}

		}
		return connection;
	}

	public ConnectionFactory getFactory()
	{
		return connectionFactory;
	}

	public void doStart() throws Exception {
		this.run();
	}

	public void doStop() throws Exception {
		this.stop();
		if(this.connection != null && this.connection.isOpen()){
			this.connection.close();
		}

		if(executorSrv != null && !executorSrv.isShutdown()){
			executorSrv.shutdown();
		}
	}

	protected abstract void run() throws Exception;
	protected abstract void stop() throws Exception;

}
