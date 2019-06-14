package com.zhengcq.srv.core.common.support.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 数字加密混淆类
 * Created by clude on 9/26/17.
 * @version 1.0
 * @since 1.0
 *
 * 使用样例：
 * String md5Secret = "DEfM1902CODE";
 * String encodedCode = SimpleNumberCoder.encode("123456", md5Secret);
 * String originalCode = SimpleNumberCoder.decode(encodedCode, md5Secret, 60);
 */
public class BaseXNumberCoder {

    private static Long BASE_TS_LINE = getDate(2010, 1, 1);
    private static final int TS_LEN = 9; // 时间戳字段长度，基于2010年取差值，9位时间戳可保30年
    private static final int MD5_SECURITY_CODE_LEN = 1; // MD5 检验码长度
    private static final long DEFAULT_TIMESTAMP_MAX_AGE = 180; //加密信息的默认过期时间， 精度 秒

    private static final BASE62Encoder baseEncoder = new BASE62Encoder("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    private static String getMD5(String str) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        return new BigInteger(1, md.digest()).toString();
    }

    /**
     * 获取指定的UTC时间
     * @param year
     * @param month
     * @param date
     * @return
     */
    private static Long getDate(int year, int month, int date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(year, month, date, 0, 0, 0);
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * @param secret  md5 secret
     * @return
     * @throws Exception
     */
    public static String encode(String sourceNumber, String secret) throws Exception {
        // 获取基于基础时间的时间戳
        Long ts = new Date().getTime() / 1000 - BASE_TS_LINE;

        Integer rnd = (int)(Math.random() * 5) + 4;

        String strOriginal = ts.toString() + sourceNumber;

        // md5加密原始字符串，生成短检验码
        String md5Hash = getMD5(rnd.toString() + strOriginal + secret);
        String md5SecurityCode = md5Hash.substring(0, MD5_SECURITY_CODE_LEN);

        Long finalNumber =  Long.parseLong(rnd.toString() + md5SecurityCode + strOriginal);

        return baseEncoder.encodeBase10(finalNumber);

    }

    /**
     * 解密，检验安全码和有效期，返回原始数值
     * @param encodedBaseStr  已加密的编码
     * @param secret    md5 secret
     * param timestampMaxAge    加密码允许的最大过期时间， （精度 秒)
     * @return
     * @throws Exception
     */
    public static String decode(String encodedBaseStr, String secret, long timestampMaxAge) throws Exception {
        String encodedNumber = baseEncoder.decodeBase62(encodedBaseStr).toString();

        String rnd = encodedNumber.substring(0, 1);
        String md5SecurityCode = encodedNumber.substring(1, MD5_SECURITY_CODE_LEN + 1);
        String strOriginal = encodedNumber.substring(1+MD5_SECURITY_CODE_LEN);
        String ts = strOriginal.substring(0, TS_LEN);
        String sourceNumber = strOriginal.substring(TS_LEN);


        // 检查md5检验码是否有效
        String md5Hash = getMD5(rnd + strOriginal + secret);
        if(!md5Hash.substring(0, MD5_SECURITY_CODE_LEN).equals(md5SecurityCode)) {
            throw new Exception("Invalid security code");
        }

        // 检查时间戳的有效性, 如果时间戳超过timestamp max age，就认为该数据无效
        long currentTs = System.currentTimeMillis() / 1000;
        if (currentTs - BASE_TS_LINE - Long.parseLong(ts) >= timestampMaxAge) {
            throw new Exception("Invalid timestamp, code expired");
        }

        return sourceNumber;
    }

    /**
     * 解密，检验安全码和有效期，返回原始数值
     * @param encodedNumber  已加密的编码
     * @param secret    md5 secret
     * @return
     * @throws Exception
     */
    public static String decode(String encodedNumber, String secret) throws Exception {
        return decode(encodedNumber, secret, DEFAULT_TIMESTAMP_MAX_AGE);
    }

    public static boolean isValid(String sourceNumber, String encodedBaseStr, String secret, long timestampMaxAge) {
        try {
            String decodedSourceNo = decode(encodedBaseStr, secret, timestampMaxAge);
            return sourceNumber.equals(decodedSourceNo);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValid(String sourceNumber, String encodedBaseStr, String secret) {
        return isValid(sourceNumber, encodedBaseStr, secret, DEFAULT_TIMESTAMP_MAX_AGE );
    }

    public static void main(String[] args) throws Exception {
        String md5Secret = "Td5Y8s3Pq9Un";
        String baseEncodeNo = BaseXNumberCoder.encode("224", md5Secret);
        System.out.println(baseEncodeNo);
        String baseSourceNo = BaseXNumberCoder.decode(baseEncodeNo, md5Secret, 60);
        System.out.println(baseSourceNo);
        boolean isValid = BaseXNumberCoder.isValid( "224", baseEncodeNo, md5Secret, 60);
        System.out.println(isValid);
    }
}
