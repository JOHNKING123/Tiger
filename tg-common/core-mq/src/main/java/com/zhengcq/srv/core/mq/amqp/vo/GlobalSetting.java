package com.zhengcq.srv.core.mq.amqp.vo;

//import com.zhengcq.srv.core.framework.utils.PropertiesLoader;
import com.zhengcq.srv.core.common.utils.PropertiesLoader;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service("globalSetting")
public class GlobalSetting {
	
	private final String[] RESOURCE_PATHS = { "amqp.properties", "queueSetting.properties" };
	private Properties properties;
	
	public GlobalSetting(){
		properties = new PropertiesLoader(this.RESOURCE_PATHS).getProperties();
		setAllProperties(properties);
	}
	/**
	 * email sender and host url.
	 */
	public String EMAIL_RECIVER = "cludezxf@163.com"; // Settings.APP.EMAIL_SUPPORT; // "ops@venovate.com";
	public String EMAIL_NOREPLY = "cludezxf@163.com"; // Settings.APP.EMAIL_NO_REPLY; // "no-reply@venovate.com"; //no-reply@venovate.com lxq_360@hotmail.com
	public String EMAIL_SENDER = "cludezxf@163.com"; // Settings.APP.EMAIL_NO_REPLY;


	public void setAllProperties(Properties properties){
	}

}
