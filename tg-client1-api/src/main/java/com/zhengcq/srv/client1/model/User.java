package com.zhengcq.srv.client1.model;

import com.zhengcq.srv.core.db.base.SuperBaseModel;
import lombok.Data;

@Data
public class User extends SuperBaseModel {

    private Long    id;

    private String name;

    private Integer age;

    private String nickName;
}
