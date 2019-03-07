package com.zhengcq.srv.core.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhengcq.srv.core.common.baseEnums.BaseEnums;
import lombok.Data;


/**
 * Created by clude on 3/15/17.
 */

@Data
public class JsonResult<T> {
    private int errcode = ServiceCode.FAILED;

    private String message;
    private T data;
    private String stackTrace;

    public JsonResult(){};

    public JsonResult(T data){
        this.errcode = ServiceCode.OK;
        this.data = data;
    }

    public JsonResult(int errcode, T data, String message){
        this.errcode = errcode;
        this.data = data;
        this.message = message;
    }

    public static <T> JsonResult ok(T data){
        return new JsonResult(data);
    }

    public static JsonResult failed(String message, int errcode){
        return new JsonResult(errcode, null, message);
    }

    public static JsonResult failed(String message){
        return new JsonResult(ServiceCode.FAILED, null, message);
    }

    //增加错误码
    public static JsonResult failed(BaseEnums errCode){
        return new JsonResult(errCode.getCode(), null, errCode.getDisplayName());
    }

    @JsonIgnore
    public boolean isSucceeded(){
        return this.getErrcode() == ServiceCode.OK ;
    }

    @JsonIgnore
    public boolean isSucceededWithData(){
        if (this.getErrcode() == ServiceCode.OK && this.getData() != null) {
            return true;
        }
        return false;
    }


    public String getStackTrace() {
        return this.stackTrace;
    }

    public void setStackTrace(String strack) {
        this.stackTrace = strack;
    }
}
