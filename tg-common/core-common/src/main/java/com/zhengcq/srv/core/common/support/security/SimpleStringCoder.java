package com.zhengcq.srv.core.common.support.security;

import org.springframework.util.Base64Utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;

/**
 * 字符串加密检查类
 * Created by clude on 12/28/17.
 * @version 1.0
 * @since 1.0
 *
 * 使用样例：
 * String secret = "DEfM1902CODE";
 * String encodedCode = SimpleStringCoder.encode("C1002", secret);
 * String originalCode = SimpleNumberCoder.decode(encodedCode, secret);
 */
public class SimpleStringCoder {

    private static final int TS_LEN = 10; // 时间戳字段长度
    private static final int MD5_SECURITY_CODE_LEN = 16; // MD5 检验码长度
    private static final int RND_LEN = 3; // MD5 检验码长度

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    private static String getMD5(String str) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        return new BigInteger(1, md.digest()).toString(16);
    }


    /**
     * @param sourceStr 原始数值
     * @param secret  md5 secret
     * @return
     * @throws Exception
     */
    public static String encode(String sourceStr, String secret) throws Exception {
        // 获取基于基础时间的时间戳
        Long ts = new Date().getTime() / 1000;

        Integer rnd = (int)(Math.random() * 99) + 100;

        // 构造原始字符串, 带时间戳信息
        String strOriginalValue = rnd.toString() + ts.toString() + sourceStr;

        String baseCodedChars = Base64Utils.encodeToUrlSafeString(strOriginalValue.getBytes());

        // md5加密原始字符串，生成短检验码
        String md5Hash = getMD5(baseCodedChars + secret);
        String md5SecurityCode = md5Hash.substring(0, MD5_SECURITY_CODE_LEN);

        // 混淆原始字符串信息并带上md5检验码
        return md5SecurityCode + baseCodedChars;
    }

    /**
     * 解密，检验安全码和有效期，返回原始数值
     * @param encodedStr  已加密的编码
     * @param secret    md5 secret
     * param timestampMaxAge    加密码允许的最大过期时间， （精度 秒)
     * @return
     * @throws Exception
     */
    public static String decode(String encodedStr, String secret, long timestampMaxAge) throws Exception {
        String md5SecurityCode = encodedStr.substring(0, MD5_SECURITY_CODE_LEN);
        String base64Chars = encodedStr.substring(MD5_SECURITY_CODE_LEN);

        // 检查md5检验码是否有效
        String md5Hash = getMD5(base64Chars + secret);
        if(!md5Hash.substring(0, MD5_SECURITY_CODE_LEN).equals(md5SecurityCode)) {
            throw new Exception("Invalid security code");
        }

        String iOriginalValue = new String(Base64Utils.decodeFromUrlSafeString(base64Chars));

        // 获取时间戳并检查有效性
        // 检查时间戳的有效性, 如果时间戳超过timestamp max age，就认为该数据无效
        if(timestampMaxAge != Long.MAX_VALUE) {
            String ts = iOriginalValue.substring(RND_LEN, TS_LEN);
            long currentTs = System.currentTimeMillis() / 1000;
            if (currentTs - Long.parseLong(ts) >= timestampMaxAge) {
                throw new Exception("Invalid timestamp, code expired");
            }
        }

        // 获取原始号码
        String sourceNumber = iOriginalValue.substring(RND_LEN + TS_LEN);

        return sourceNumber;
    }

    /**
     * 解密，检验安全码和有效期，返回原始数值
     * @param encodedStr  已加密的编码
     * @param secret    md5 secret
     * @return
     * @throws Exception
     */
    public static String decode(String encodedStr, String secret) throws Exception {
        return decode(encodedStr, secret, Long.MAX_VALUE);
    }

    public static boolean isValid(String sourceStr, String encodedBaseStr, String secret) {
        try {
            String decodedSourceNo = decode(encodedBaseStr, secret);
            return sourceStr.equals(decodedSourceNo);
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        String secret = "Td5Y8s3Pq9Un";
        String companyCode = "C1002";
        String accesstoken = SimpleStringCoder.encode(companyCode, secret);
        System.out.println(accesstoken);
        String baseSourceNo = SimpleStringCoder.decode(accesstoken, secret);
        System.out.println(baseSourceNo);
        boolean isValid = SimpleStringCoder.isValid( companyCode, accesstoken, secret);
        System.out.println(isValid);
    }

}
