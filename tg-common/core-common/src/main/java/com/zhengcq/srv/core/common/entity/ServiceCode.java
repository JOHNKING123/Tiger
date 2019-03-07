package com.zhengcq.srv.core.common.entity;

/**
 * Created by clude on 3/15/17.
 * 错误码修改
 * 0    成功
 * 非0  失败 ( 默认失败错误码为1 )
 */
public interface ServiceCode {
    static final int OK = 0;
    static final int FAILED = 1;
}
