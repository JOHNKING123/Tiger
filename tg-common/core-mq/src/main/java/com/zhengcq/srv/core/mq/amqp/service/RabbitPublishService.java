package com.zhengcq.srv.core.mq.amqp.service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zhengcq.srv.core.mq.amqp.service.base.AbstractAmqpService;
import com.zhengcq.srv.core.mq.amqp.util.CommonUtil;

import java.util.HashMap;
import java.util.Map;

//@Service("rabbitPublishService")
public class RabbitPublishService extends AbstractAmqpService {

	//private Logger logger = Logger.getLogger(RabbitSubscribeAmqp.class);

	// AMQP.BasicProperties.Builder().deliveryMode(2)
	// 强制数据包需要持久化
	private AMQP.BasicProperties defaultProp = new AMQP.BasicProperties.Builder().deliveryMode(2).contentType("application/json").contentEncoding("utf-8").build();

//	@Autowired
//	public RabbitPublishService(ConnectionFactory connectionFactory, @Qualifier(value="intFixedThreadsPool")Integer threadSize) {
//		super(connectionFactory, threadSize);
//	}

//	@Autowired
	public RabbitPublishService(ConnectionFactory connectionFactory, Integer threadSize) {
		super(connectionFactory, threadSize);
	}

	public boolean DeleteQueueByName(String queueName){
		try{
			logger.debug("TempPublish RabbitMQ connection to delete. " + queueName );
			Connection connection = this.getConnection();
			if(connection == null) {
				logger.error("TempPublish get connection failed!");
				return false;
			}
			Channel channel = connection.createChannel();
			if(channel == null ) {
				logger.error("TempPublish get createChannel failed!");
				return false;
			}

			boolean isDeleted = false;
			AMQP.Queue.DeleteOk ok = channel.queueDelete( queueName );
			if(ok.getMessageCount() > 0){
				isDeleted = true;
			}

			channel.close();
			//connection.close();
			logger.debug("DeleteTempQueue RabbitMQ delete Queue successfully!");

			if( !isDeleted ){
				logger.warn(String.format("删除queue失败 : %s " ,queueName ) );
				return false;
			} else {
				return true;
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.error("DeleteQueueByName RabbitmtRublishServer Exception ",ex);
			return false;
		}
	}

	public boolean DeleteTempQueue(String taskID ) {
		String []uuid = taskID.split("\\|");
		int alarmLevel = 0 ;
		if( uuid.length == CommonUtil.TASK_UUID_ARRAY_LENGTH ) {
			alarmLevel = Integer.valueOf( uuid[1] );
		}

		if( alarmLevel <= 0) {
			// 不需要报警
			return true;
		}

		String queueName = String.format("temp.%s",taskID);
		try{
			for(int i = 0 ; i < 3 ; i++){
				if(DeleteQueueByName(queueName)){
					return true;
				}
			}
			return false;
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.error(String.format("Delete Temp Queue failed! %s " , taskID ) );
			logger.error("DeleteTempQueue RabbitmtRublishServer Exception ",ex);
			return false;
		}
	}

	public void CreateTempPublish(byte[] data, long msg_ttl, String tempQueue, long queue_ttl, String exchangeName,String deadLetterExchange,String deadLetterRoutingKey) {
		try {
			logger.debug("TempPublish RabbitMQ connection to create.");
			Connection connection = this.getConnection();
			if(connection == null) {
				logger.error("TempPublish get connection failed!");
				return;
			}
			Channel channel = connection.createChannel();
			if(channel == null ) {
				logger.error("TempPublish get createChannel failed!");
				return;
			}

			String queueName = String.format("temp.%s",tempQueue);
			String routingKey = String.format("temp.%s",tempQueue);

			Map<String, Object> args = new HashMap<>();
			args.put("x-dead-letter-exchange",deadLetterExchange);
			args.put("x-dead-letter-routing-key",deadLetterRoutingKey);
			args.put("x-expires",  queue_ttl );

			channel.exchangeDeclare(exchangeName, "topic", true);
			AMQP.Queue.DeclareOk queueDeclareStatus = channel.queueDeclare(queueName, AMQP_DURABLE, AMQP_TEMPQUEUE_EXCLUSIVE, AMQP_TEMPQUEUE_AUTO_DELETE, args);
			if( queueDeclareStatus == null ) {
				logger.error("channel.queueDeclare failed!");
			}

			//logger.warn(String.format("queueDeclare %s %s", queueDeclareStatus.getQueue() , JSonUtils.toJson(queueDeclareStatus)));

			AMQP.Queue.BindOk ok = channel.queueBind(queueName, exchangeName, routingKey);

			// fixed maybe rabbit task is delay
			// 20170117
			AMQP.BasicProperties messageProp = new AMQP.BasicProperties.Builder().deliveryMode(2).contentType("application/json").contentEncoding("UTF-8").expiration(String.valueOf(msg_ttl)).build();
			channel.basicPublish(exchangeName, routingKey, messageProp, data);

			channel.close();
			//connection.close();
			logger.debug("TempPublish RabbitMQ Message sent successfully!");
			//Call reception method
			//new GetData(this.hostName,1).gettingData();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error( "CreateTempPublish Exception ",ex ) ;
		}
	}
	/**
	 * Send a message to the rabbitMQ, method 2
	 * @param data
	 * @param routingKey
	 */
	public void Publish(byte[] data, long msg_ttl , String routingKey, String exchange) {
		try {
			logger.debug("RabbitMQ connection to create.");
			Connection connection = this.getConnection();

			Channel channel = connection.createChannel();//Create a channel
			channel.exchangeDeclare(exchange, "topic", true);

			logger.debug("------------------sending data:");
			// Sets the queue to persistent, also need the message is also set to be persistent,
			// channel.basicPublish(exchange, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, data);
			// byte[] messageBodyBytes = "Hello, world!".getBytes();

			if(msg_ttl > 0) {
				AMQP.BasicProperties messageProp = new AMQP.BasicProperties.Builder().deliveryMode(2).contentType( "application/json").expiration(String.valueOf(msg_ttl)).build();
				channel.basicPublish(exchange, routingKey, messageProp, data);
			}  else {
				channel.basicPublish(exchange, routingKey, defaultProp, data);
			}

			logger.debug(data.toString());
			channel.close();
			//connection.close();
			logger.debug("RabbitMQ Message sent successfully!");
			//Call reception method
			//new GetData(this.hostName,1).gettingData();
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	/**
	 * Send a message to the rabbitMQ, method 2
	 * @param data
	 * @param routingKey
	 */
	public void Publish(byte[] data, long ttl, String routingKey) {
		this.Publish(data, ttl, routingKey, "ex_default");
	}

	public void Publish(String data,long ttl, String routingKey){
		this.Publish(data.getBytes(), ttl , routingKey);
	}


	@Override
	protected void run() throws Exception {
//		Subscribes();
	}

	@Override
	protected void stop() throws Exception {

	}

}
