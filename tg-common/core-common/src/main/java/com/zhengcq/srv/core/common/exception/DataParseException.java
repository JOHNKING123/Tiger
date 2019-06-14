package com.zhengcq.srv.core.common.exception;


import com.zhengcq.srv.core.common.exception.base.BaseException;
import com.zhengcq.srv.core.common.support.HttpCode;

/**
 * @author YoorstoreTech
 * @version 2016年5月20日 下午3:19:19
 */
@SuppressWarnings("serial")
public class DataParseException extends BaseException {

	public DataParseException() {
	}

	public DataParseException(Throwable ex) {
		super(ex);
	}

	public DataParseException(String message) {
		super(message);
	}

	public DataParseException(String message, Throwable ex) {
		super(message, ex);
	}

	public HttpCode getHttpCode() {
		return HttpCode.INTERNAL_SERVER_ERROR;
	}

}
