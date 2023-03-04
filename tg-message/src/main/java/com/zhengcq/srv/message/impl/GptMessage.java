package com.zhengcq.srv.message.impl;

import com.alibaba.fastjson.JSONObject;

import java.util.Calendar;

public class GptMessage implements java.io.Serializable {
    private static final long serialVersionUID = 4497631594624976160L;
    private int created;
    private GptMessageUsage usage;
    private String model;
    private String id;
    private GptMessageChoices[] choices;
    private String object;

    public int getCreated() {
        return this.created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public GptMessageUsage getUsage() {
        return this.usage;
    }

    public void setUsage(GptMessageUsage usage) {
        this.usage = usage;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GptMessageChoices[] getChoices() {
        return this.choices;
    }

    public void setChoices(GptMessageChoices[] choices) {
        this.choices = choices;
    }

    public String getObject() {
        return this.object;
    }

    public void setObject(String object) {
        this.object = object;
    }


    public static void main(String[] args) {
        String jsonStr = "{\"id\":\"cmpl-6qGQs5uzFfIxrnHN7kkVoYCVb13xe\",\"object\":\"text_completion\",\"created\":1677914242,\"model\":\"text-davinci-003\",\"choices\":[{\"text\":\"\\n\\nOn April 17, 1930, the Chinese Communist Party established the Chinese Soviet Republic, a self-declared state in the Chinese provinces of Jiangxi and Fujian. This marked the beginning of a struggle for control of China between the Chinese Nationalists and the newly formed Chinese Communist Party. This struggle lasted until 1949 when the Chinese Nationalists were driven from the mainland of China, effectively ending their control over the Chinese government.\",\"index\":0,\"logprobs\":null,\"finish_reason\":\"stop\"}],\"usage\":{\"prompt_tokens\":7,\"completion_tokens\":86,\"total_tokens\":93}}";

        GptMessage gptMessage = JSONObject.parseObject(jsonStr, GptMessage.class);
        if (gptMessage != null && !gptMessage.getId().equals("")){
            System.out.println(gptMessage.getChoices()[0].getText());
        }
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.toString());
    }
}

