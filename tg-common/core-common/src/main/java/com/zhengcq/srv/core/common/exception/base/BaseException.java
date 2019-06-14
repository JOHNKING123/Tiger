/**
 * 
 */
package com.zhengcq.srv.core.common.exception.base;

import com.zhengcq.srv.core.common.support.HttpCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;

/**
 * 
 * @author YoorstoreTech
 * @version 2016年6月7日 下午8:43:02
 */
@SuppressWarnings("serial")
public abstract class BaseException extends RuntimeException {
	private int errorCode = 0;

	public BaseException() {
	}

	public BaseException(Throwable ex) {
		super(ex);
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, Throwable ex) {
		super(message, ex);
	}

	public BaseException(int errorCode, String message){
		super(message);
		this.errorCode = errorCode;
	}

	public BaseException(int errorCode, Throwable ex){
		super(ex);
		this.errorCode = errorCode;
	}

	public BaseException(int errorCode, String message, Throwable ex){
		super(message, ex);
		this.errorCode = errorCode;
	}

	public void handler(ModelMap modelMap) {
		modelMap.put("httpCode", getHttpCode().value());
		if (StringUtils.isNotBlank(getMessage())) {
			modelMap.put("msg", getMessage());
		} else {
			modelMap.put("msg", getHttpCode().msg());
		}
		modelMap.put("errorCode", getErrorCode());
		modelMap.put("timestamp", System.currentTimeMillis());
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public abstract HttpCode getHttpCode();
}
