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
public class SimpleNumberCoder {

    private static Long BASE_TS_LINE = getDate(2010, 1, 1);
    private static final int TS_LEN = 9; // 时间戳字段长度，基于2010年取差值，9位时间戳可保30年
    private static final int MD5_SECURITY_CODE_LEN = 4; // MD5 检验码长度
    private static final long DEFAULT_TIMESTAMP_MAX_AGE = 60; //加密信息的默认过期时间， 精度 秒

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
     * @param sourceNumber 原始数值
     * @param secret  md5 secret
     * @return
     * @throws Exception
     */
    public static String encode(String sourceNumber, String secret) throws Exception {
        // 获取基于基础时间的时间戳
        Long ts = new Date().getTime() / 1000 - BASE_TS_LINE;

        // 构造原始字符串, 带时间戳信息
        String strOriginalValue = ts.toString() + sourceNumber;

        // md5加密原始字符串，生成短检验码
        String md5Hash = getMD5(strOriginalValue + secret);
        String md5SecurityCode = md5Hash.substring(0, MD5_SECURITY_CODE_LEN);
        // 生成20位异或掩码 由于 MD5_SECURITY_CODE_LEN长度为4 （最小就是10的3次方), 7次方能保证至少是21位长度了
        BigInteger maskCodes = new BigInteger(md5SecurityCode).pow(7).mod(BigInteger.TEN.pow(20));

        // 混淆原始字符串信息并带上md5检验码
        return md5SecurityCode + new BigInteger(strOriginalValue).xor(maskCodes);
    }

    /**
     * 解密，检验安全码和有效期，返回原始数值
     * @param encodedNumber  已加密的编码
     * @param secret    md5 secret
     * param timestampMaxAge    加密码允许的最大过期时间， （精度 秒)
     * @return
     * @throws Exception
     */
    public static String decode(String encodedNumber, String secret, long timestampMaxAge) throws Exception {
        String md5SecurityCode = encodedNumber.substring(0, MD5_SECURITY_CODE_LEN);
        String strXorValue = encodedNumber.substring(MD5_SECURITY_CODE_LEN);

        // 生成20位异或掩码 由于 MD5_SECURITY_CODE_LEN长度为4 （最小就是10的3次方), 7次方能保证至少是21位长度了
        BigInteger maskCodes = new BigInteger(md5SecurityCode).pow(7).mod(BigInteger.TEN.pow(20));
        // 从已混淆的字符串里获取原始信息
        BigInteger iOriginalValue = new BigInteger(strXorValue).xor(maskCodes);

        // 获取时间戳和原始号码
        String ts = iOriginalValue.toString().substring(0, TS_LEN);
        String sourceNumber = iOriginalValue.toString().substring(TS_LEN);

        // 检查md5检验码是否有效
        String md5Hash = getMD5(iOriginalValue + secret);
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

}
