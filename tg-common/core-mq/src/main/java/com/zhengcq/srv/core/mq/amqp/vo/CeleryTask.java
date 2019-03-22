package com.zhengcq.srv.core.mq.amqp.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JavaType;
import com.zhengcq.srv.core.common.utils.JSonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clude on 7/8/16.
 */
public class CeleryTask {
    private String task;
    private String id;
    private List<String> args;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public void setArg(Object arg){
        this.args = new ArrayList<>();
        if(arg instanceof  String){
            this.args.add((String)arg);
        }else{
            this.args.add(JSonUtils.toJson(arg));
        }
    }

    public String toJson(){
        return JSonUtils.toJson(this);
    }

    public static CeleryTask load(String jsonStr) {
        return JSonUtils.loadJson(jsonStr, CeleryTask.class);
    }

    @JsonIgnore
    public <T> T getArgData(Class<T> valueType){
        if(args != null && args.size() > 0){
            return JSonUtils.loadJson(args.get(0), valueType);
        }
        return null;
    }

    @JsonIgnore
    public <T> T getArgData(JavaType javaType){
        if(args != null && args.size() > 0){
            return JSonUtils.loadJson(args.get(0), javaType);
        }
        return null;
    }
}
