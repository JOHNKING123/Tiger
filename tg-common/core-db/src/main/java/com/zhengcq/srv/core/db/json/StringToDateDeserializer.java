package com.zhengcq.srv.core.db.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.zhengcq.srv.core.utils.DateUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Created by jiang on 2017/7/28.
 */

public class StringToDateDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        try {
            if(jsonParser.getValueAsString() == null || jsonParser.getValueAsString().length() <= 1) {
                return null;
            } else {
                Date curDate = null;
                if(jsonParser.getValueAsString().length() == 10) {
                    // 2018-01-01
                    curDate = DateUtils.parseDate(jsonParser.getValueAsString(),"yyyy-MM-dd");
                } else if (jsonParser.getValueAsString().length() == 14) {
                    // 20180226090558
                    String tmp = jsonParser.getValueAsString();
                    String fmtTmp = String.format("%s-%s-%s %s:%s:%s",
                            tmp.substring(0,4),
                            tmp.substring(4,6),
                            tmp.substring(6,8),
                            tmp.substring(8,10),
                            tmp.substring(10,12),
                            tmp.substring(12));
                    curDate = DateUtils.parseDate(fmtTmp,"yyyy-MM-dd HH:mm:ss");
                } else {
                    curDate = DateUtils.parseDate(jsonParser.getValueAsString(),"yyyy-MM-dd HH:mm:ss");
                }
                return curDate;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}