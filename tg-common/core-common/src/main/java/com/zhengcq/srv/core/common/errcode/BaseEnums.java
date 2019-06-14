package com.zhengcq.srv.core.common.errcode;

/**
 * Created by jiang on 2017/5/15.
 */
public interface BaseEnums {
    int getCode();
    String getDisplayName();

    default int code(){
        return this.getCode();
    }

    default String name(){
        return this.getDisplayName();
    }
}
