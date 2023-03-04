package com.zhengcq.srv.message.impl;

public class GptMessageChoices implements java.io.Serializable {
    private static final long serialVersionUID = 7583624020930781581L;
    private String finish_reason;
    private int index;
    private String text;
    private Object logprobs;

    public String getFinish_reason() {
        return this.finish_reason;
    }

    public void setFinish_reason(String finish_reason) {
        this.finish_reason = finish_reason;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getLogprobs() {
        return this.logprobs;
    }

    public void setLogprobs(Object logprobs) {
        this.logprobs = logprobs;
    }
}
