package com.zhengcq.srv.core.mq.amqp.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jiang on 2016/7/8.
 */
public class CommonUtil {

    public static final int MAX_JOB_GROUP_LENGTH = 64 ;
    public static final int MAX_JOB_NAME_LENGTH = 64 ;
    public static final int MAX_JOB_PARAMETER_LENGTH = 65530;
    public static final int MAX_JOB_DESCRIPTION_LENGTH = 128 ;

    public static final int MAX_RESULT_TRACEBACK_LENGTH = 2048;
    public static final int MAX_RESULT_STRING_LENGTH = 1024;

    // 最新的taskid由4部分组成
    //  a|b|c|d
    //  a : saveLog
    //  b : 0 不报警  1邮件报警 2微信报警  1+ 2 邮件+微信报警
    //  c : 时间戳
    //  d : 随机字符串
    public static final int TASK_UUID_ARRAY_LENGTH = 4 ;

    public static Long getCrc32(String str) {
        java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
        crc32.update(str.getBytes());
        return crc32.getValue();
    }


    /**
     * @return  本机主机名
     */
    public static String getHostName() {
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (ia == null ) {
            return "unknown";
        }
        else {
            return ia.getHostName();
        }
    }

    public static boolean isRegexMatching(String str, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static String getDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public static String getDateByTimestamp(long timestamp) {
//        Timestamp ts = new Timestamp(timestamp);
        Date ts = new Date(timestamp);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //方法一
            return sdf.format(ts);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
