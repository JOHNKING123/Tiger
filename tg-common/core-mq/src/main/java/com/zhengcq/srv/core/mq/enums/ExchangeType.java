package com.zhengcq.srv.core.mq.enums;

import java.io.Serializable;

/**
 * Created by jiang on 2016/7/11.
 */
public enum ExchangeType implements Serializable {
    EX_TYPE_TOPIC (1,"Topic"), EX_TYPE_DIRECT(1,"Direct"), EX_TYPE_FANOUT(3,"Fanout");
    private int code;
    private String displayName;
    public int getCode() {
        return code;
    }
    public String getDisplayName() { return displayName; }
    private ExchangeType(int code, String name) {
        this.code = code;
        this.displayName = name;
    }

    public static ExchangeType valueFromCode(int code){
        for (ExchangeType v: ExchangeType.values()) {
            if(code == v.getCode()){
                return v;
            }
        }
        return null;
    }
}
