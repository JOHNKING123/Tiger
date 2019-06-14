package com.zhengcq.srv.core.db.json;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * 返回double类型数据  保留两位数
 * create by zhengcq 2018/04/28
 */
public class CustomDoubleSerialize extends ToStringSerializer {
    private DecimalFormat df = new DecimalFormat("##.00");
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if(value != null) {
            gen.writeNumber(Double.valueOf(df.format(value)));
        }
    }
}
