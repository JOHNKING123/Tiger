package com.zhengcq.srv.core.db.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Created by jiang on 2017/7/27.
 * 日期格式定制化显示
 */



// 日期格式定制化显示为: yyMMdd
public class DateToCustomerBatchSerializer extends ToStringSerializer {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try{
            if(value == null){
                gen.writeString("");
            } else {
                // yyMMdd
                String formatDate = DateFormatUtils.format((Date)value,"yyMMdd");
                gen.writeString(formatDate);
            }
        }catch (Exception ex){
            gen.writeString("");
        }
    }

}
