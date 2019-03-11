package com.zhengcq.srv.core.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

/**
 * Created by clude on 7/18/16.
 */
public class PropertiesContextHolder {
    private static PropertiesLoader propertiesLoader = null;

    private static String[] locations = ArrayUtils.EMPTY_STRING_ARRAY;

    public void setPropertiesLoader(PropertiesLoader propertiesLoader) {
        PropertiesContextHolder.propertiesLoader = propertiesLoader; // NOSONAR
    }

    public void setLocations(String[] locations) {
        PropertiesContextHolder.locations = locations;
        PropertiesContextHolder.propertiesLoader = new PropertiesLoader(locations);
    }

    public static void addAdditionalLocation(String... locations){
        PropertiesContextHolder.locations = ArrayUtils.addAll(PropertiesContextHolder.locations, locations);
        PropertiesContextHolder.propertiesLoader = new PropertiesLoader(PropertiesContextHolder.locations);
    }

    /**
     * get the static ApplicationContext.
     */
    public static PropertiesLoader getPropertiesLoader() {
        assertContextInjected();
        return propertiesLoader;
    }



    /**
     * 取出String类型的Property，但以System的Property优先,如果都为Null则抛出异常.
     */
    public static String getProperty(String key) {
        assertContextInjected();
        return propertiesLoader.getProperty(key);
    }

    /**
     * 取出String类型的Property，但以System的Property优先.如果都为Null则返回Default值.
     */
    public static String getProperty(String key, String defaultValue) {
        assertContextInjected();
        return propertiesLoader.getProperty(key, defaultValue);
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     */
    public static Integer getInteger(String key) {
        assertContextInjected();
        return propertiesLoader.getInteger(key);
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     */
    public static Integer getInteger(String key, Integer defaultValue) {
        assertContextInjected();
        return propertiesLoader.getInteger(key, defaultValue);
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     */
    public static Double getDouble(String key) {
        assertContextInjected();
        return propertiesLoader.getDouble(key);
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     */
    public static Double getDouble(String key, Integer defaultValue) {
        assertContextInjected();
        return propertiesLoader.getDouble(key, defaultValue);
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null抛出异常,如果内容不是true/false则返回false.
     */
    public static Boolean getBoolean(String key) {
        assertContextInjected();
        return propertiesLoader.getBoolean(key);
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null则返回Default值,如果内容不为true/false则返回false.
     */
    public static Boolean getBoolean(String key, boolean defaultValue) {
        assertContextInjected();
        return propertiesLoader.getBoolean(key, defaultValue);
    }


    /**
     * Check whether ApplicationContext is null.
     */
    private static void assertContextInjected() {
        Validate.validState(propertiesLoader != null, "properities loader未初始化注入, 请在spring.xml中定义PropertiesContextHolder.");
    }
}
