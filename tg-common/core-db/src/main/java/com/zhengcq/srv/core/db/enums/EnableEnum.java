package com.zhengcq.srv.core.db.enums;



import com.zhengcq.srv.core.mq.srv.core.common.baseEnums.BaseEnums;

import java.io.Serializable;

/**
 * Created by clude on 4/16/18.
 */
public enum EnableEnum implements BaseEnums, Serializable {
    ////////以下值不要轻易改，很多地方都用了0，1这两个值进行判断//////////////
    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private Integer value;
    private String name;

    private EnableEnum(Integer value, String name) {

        this.value = value;
        this.name = name;
    }

    public static EnableEnum getByValue(Integer value){
        EnableEnum[] codes = EnableEnum.values();
        for(EnableEnum c : codes){
            if (c.getCode() == value) {
                return c;
            }
        }
        return null;
    }


    @Override
    public int getCode() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return name;
    }
}
