package com.zhengcq.srv.core.common.exception;


import com.zhengcq.srv.core.common.errcode.IErrorEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务异常.
 *
 * @author Yoorstore
 */
@Slf4j
public class BizException extends RuntimeException {

	/**
	 * 异常码
	 */
	protected int code;

	private static final long serialVersionUID = 2160241586346324994L;

	public BizException() {
	}

	public BizException(Throwable cause) {
		super(cause);
	}

	public BizException(String message) {
		super(message);
	}

	public BizException(String message, Throwable cause) {
		super(message, cause);
	}

	public BizException(int code, String message) {
		super(message);
		this.code = code;
	}

	public BizException(int code, String msgFormat, Object... args) {
		super(String.format(msgFormat, args));
		this.code = code;
	}

	public BizException(IErrorEnum codeEnum, Object... args) {
		super(String.format(codeEnum.msg(), args));
		this.code = codeEnum.code();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
