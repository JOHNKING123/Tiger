package com.zhengcq.srv.core.db.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.zhengcq.srv.core.common.utils.DateUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Created by jiang on 2017/7/28.
 * yyMMdd
 */

public class CustomerBatchToDateDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        try {
            if(jsonParser.getValueAsString() == null || jsonParser.getValueAsString().length() <= 1) {
                return null;
            } else {
                Date curDate = null;
                if(jsonParser.getValueAsString().length() == 6) {
                    curDate = DateUtils.parseDate("20" + jsonParser.getValueAsString(),"yyyy-MM-dd");
                }
                return curDate;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}