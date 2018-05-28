package com.orvibo.cloud.connection.server.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunlin on 2017/7/20.
 */
public class LogUtil {

    private static final String BUSINESS_CODE = "business_code";

    private static final String BUSINESS_VALUE = "business_value";

    private static final String SERIAL_NUMBER = "serial_number";

    private static final ThreadLocal<Map<String, String>> contextHolder = new ThreadLocal<Map<String, String>>();

    public static Logger getLogger(Class<?> clazz){
        return LoggerFactory.getLogger(clazz);
    }

    public static Logger getLogger(String name) {

        return LoggerFactory.getLogger(name);
    }

    public static void setBusinessCode(String code) {
        Map<String, String> map = getMap();
        map.put(BUSINESS_CODE, code);
        return ;
    }

    public static String getBusinessCode(){
        Map<String, String> map = getMap();
        return map.get(BUSINESS_CODE);
    }

    public static void setBusinessValue(String value) {
        Map<String, String> map = getMap();
        map.put(BUSINESS_VALUE, value);
        return ;
    }

    public static String getBusinessValue(){
        Map<String, String> map = getMap();
        return map.get(BUSINESS_VALUE);
    }

    public static void setSerialNumber(String value) {
        Map<String, String> map = getMap();
        map.put(SERIAL_NUMBER, value);
        return ;
    }

    public static String getSerialNumber(){
        Map<String, String> map = getMap();
        return map.get(SERIAL_NUMBER);
    }

    private static Map<String, String> getMap() {
        Map<String, String> map = contextHolder.get();
        if (map == null) {
            map = new HashMap<String, String>();
            contextHolder.set(map);
        }
        return map;
    }


}
