package com.zhengcq.srv.core.db.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Created by jiang on 2017/7/5.
 */
public class StringToIntegerDeserializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            if(jsonParser.getValueAsString() == null) {
                return 0;
            } else {
                return Integer.parseInt(jsonParser.getValueAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}