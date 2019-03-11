package com.zhengcq.srv.core.db.base;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zhengcq.core.db.json.IdWorkerDeserializer;

/**
 * Created by jiang on 2017/6/14.
 */

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseIncAutoModel extends SuperBaseModel {

    @JsonSerialize(using=ToStringSerializer.class)
    @JsonDeserialize(using=IdWorkerDeserializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    // 加此id_是为了避免long类型的js精度问题
    @JsonIgnore
    public String getId_() {
        return id == null ? "" : id.toString();
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
}