package com.zhengcq.srv.core.mq.amqp.vo;

import java.util.List;

/**
 * Created by clude on 7/8/16.
 */
public class CeleryResult {

    // {"status": "SUCCESS", "traceback": null, "result": {"status": 1, "task": "shgt.q.acct.df.login_success"}, "task_id": "0|1468838653211|0474f4311885490e96", "children": []}
    private String status;
    private String traceback;

    private String task_id;
    private List<String> children;
//    private String result;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTraceback() {
        return traceback;
    }

    public void setTraceback(String traceback) {
        this.traceback = traceback;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

//    public String getResult() {
//        return result;
//    }
//
//    public void setResult(String result) {
//        this.result = result;
//    }
}
