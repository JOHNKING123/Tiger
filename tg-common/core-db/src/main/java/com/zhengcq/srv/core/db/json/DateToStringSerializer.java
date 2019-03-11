package com.zhengcq.srv.core.db.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zhengcq.srv.core.common.utils.DateUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Created by jiang on 2017/5/22.
 */
public class DateToStringSerializer extends ToStringSerializer {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try{
            if(value == null){
                gen.writeString("");
            } else if( value.toString().startsWith("0000-00-00") ) {
                gen.writeString("");
            } else {
                gen.writeString(DateUtils.formatDate((Date)value));
            }
        }catch (Exception ex){
            gen.writeString("");
        }
    }

}
