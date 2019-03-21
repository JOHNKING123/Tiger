package com.zhengcq.srv.core.mq.domain.enums;

/**
 * The enum Mq send type enum.
 *
 * @author Yoorstore
 */
public enum ConsumeStatus {
	/**
	 * 成功.
	 */
	SUCCESS,

	/**
	 * 失败.
	 */
	FAILED,

    /**
     * 可重试
	 */
	RESEND_LATER
}
