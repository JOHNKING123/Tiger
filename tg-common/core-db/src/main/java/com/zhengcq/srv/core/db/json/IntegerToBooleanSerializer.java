package com.zhengcq.srv.core.db.json;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.zhengcq.srv.core.common.utils.StringUtils;

import java.io.IOException;

public class IntegerToBooleanSerializer extends JsonSerializer<Integer> {
    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try{
            if(value == null){
                gen.writeBoolean(false);
            } else {
                if(!StringUtils.isZero(value)){
                    gen.writeBoolean(true);
                } else {
                    gen.writeBoolean(false);
                }
            }
        }catch (Exception ex){
            gen.writeBoolean(false);
        }
    }
}

