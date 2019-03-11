package com.zhengcq.srv.core.db.json;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class IntegerToStringSerializer extends JsonSerializer<Integer> {
    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try{
            if(value == null){
                gen.writeString("0");
            } else {
                gen.writeString(value.toString());
            }
        }catch (Exception ex){
            gen.writeString("0");
        }
    }
}

