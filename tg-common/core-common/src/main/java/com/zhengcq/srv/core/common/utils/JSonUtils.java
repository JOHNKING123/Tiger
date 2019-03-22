package com.zhengcq.srv.core.common.utils;


import com.fasterxml.jackson.databind.JavaType;
import com.zhengcq.srv.core.common.support.JsonMapper;


/**
 * Created by clude on 10/8/16.
 */
public class JSonUtils {

//    private static XMLSerializer xmlserializer = new XMLSerializer();

    public static <T> T loadJson(String content, Class<T> valueType){
        return (T) JsonMapper.fromJsonString(content, valueType);
    }

    /**
     * @param content
     *            JavaBean class type
     * @param javaType
     *            json string
     * @return JavaBean
     */
    public static <T> T loadJson(String content, JavaType javaType) {
        try {
            return JsonMapper.getInstance().fromJson(content, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String toJson(Object object)  {
        return JsonMapper.toJsonString(object);
    }


    /*
	 * Singleton Implementation
	 */
    public static JSonUtils getInstance() {
        return Nested.Instance;
    }

    private static class Nested {
        static final JSonUtils Instance = new JSonUtils();

        private Nested() {
        }
    }

//    /**
//     * xml to json
//     * @param xmlString
//     * @return
//     */
//    public static String xml2json(String xmlString){
//        if(StringUtils.isNotBlank(xmlString)){
//            try {
//                return xmlserializer.read(xmlString).toString();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * json to xml
//     * @param jsonString
//     * @return
//     */
//    public static String json2xml(String jsonString) {
//        XMLSerializer xmlSerializer = new XMLSerializer();
//        return xmlSerializer.write(JSONSerializer.toJSON(jsonString));
//        // return xmlSerializer.write(JSONArray.fromObject(jsonString));//这种方式只支持JSON数组
//    }

}
