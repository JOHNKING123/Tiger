package com.zhengcq.srv.core.db.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Created by jiang on 2017/7/5.
 */
public class BooleanToLongDeserializer extends JsonDeserializer<Long> {
    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            if(jsonParser.getBooleanValue()){
                return 1L;
            } else {
                return 0L;
            }
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}