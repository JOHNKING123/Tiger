package com.zhengcq.srv.core.mq.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QueueMessage {
    private static final long serialVersionUID = 8445773977180406428L;
    private String topic;
    private int flag;
    private Map<String, String> properties;
    private byte[] body;

    public QueueMessage() {
    }

    public QueueMessage(String topic, byte[] body) {
        this(topic, "", "", 0, body, true);
    }

    public QueueMessage(String topic, String tags, String keys, int flag, byte[] body, boolean waitStoreMsgOK) {
        this.topic = topic;
        this.flag = flag;
        this.body = body;
        if (tags != null && tags.length() > 0) {
            this.setTags(tags);
        }

        if (keys != null && keys.length() > 0) {
            this.setKeys(keys);
        }

        this.setWaitStoreMsgOK(waitStoreMsgOK);
    }

    public QueueMessage(String topic, String tags, byte[] body) {
        this(topic, tags, "", 0, body, true);
    }

    public QueueMessage(String topic, String tags, String keys, byte[] body) {
        this(topic, tags, keys, 0, body, true);
    }

    public void setKeys(String keys) {
        this.putProperty("KEYS", keys);
    }

    void putProperty(String name, String value) {
        if (null == this.properties) {
            this.properties = new HashMap();
        }

        this.properties.put(name, value);
    }

    void clearProperty(String name) {
        if (null != this.properties) {
            this.properties.remove(name);
        }

    }

    public String getUserProperty(String name) {
        return this.getProperty(name);
    }

    public String getProperty(String name) {
        if (null == this.properties) {
            this.properties = new HashMap();
        }

        return (String)this.properties.get(name);
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTags() {
        return this.getProperty("TAGS");
    }

    public void setTags(String tags) {
        this.putProperty("TAGS", tags);
    }

    public String getKeys() {
        return this.getProperty("KEYS");
    }

    public void setKeys(Collection<String> keys) {
        StringBuffer sb = new StringBuffer();
        Iterator var3 = keys.iterator();

        while(var3.hasNext()) {
            String k = (String)var3.next();
            sb.append(k);
            sb.append(" ");
        }

        this.setKeys(sb.toString().trim());
    }

    public int getDelayTimeLevel() {
        String t = this.getProperty("DELAY");
        return t != null ? Integer.parseInt(t) : 0;
    }

    public void setDelayTimeLevel(int level) {
        this.putProperty("DELAY", String.valueOf(level));
    }

    public boolean isWaitStoreMsgOK() {
        String result = this.getProperty("WAIT");
        return null == result ? true : Boolean.parseBoolean(result);
    }

    public void setWaitStoreMsgOK(boolean waitStoreMsgOK) {
        this.putProperty("WAIT", Boolean.toString(waitStoreMsgOK));
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public byte[] getBody() {
        return this.body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getBuyerId() {
        return this.getProperty("BUYER_ID");
    }

    public void setBuyerId(String buyerId) {
        this.putProperty("BUYER_ID", buyerId);
    }

    public String toString() {
        return "Message [topic=" + this.topic + ", flag=" + this.flag + ", properties=" + this.properties + ", body=" + (this.body != null ? this.body.length : 0) + "]";
    }
}
