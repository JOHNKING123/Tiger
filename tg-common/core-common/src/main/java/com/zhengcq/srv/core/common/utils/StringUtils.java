package com.zhengcq.srv.core.common.utils;

import com.zhengcq.srv.core.mq.srv.core.common.utils.Encodes;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @author ThinkGem
 * @version 2013-05-22
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";
    
    /**
     * 转换为字节数组
     * @param str
     * @return
     */
    public static byte[] getBytes(String str){
    	if (str != null){
    		try {
				return str.getBytes(CHARSET_NAME);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
    	}else{
    		return null;
    	}
    }
    
    /**
     * 转换为字节数组
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes){
    	try {
			return new String(bytes, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			return EMPTY;
		}
    }
    
    /**
     * 是否包含字符串
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs){
    	if (str != null){
        	for (String s : strs){
        		if (str.equals(trim(s))){
        			return true;
        		}
        	}
    	}
    	return false;
    }
    
	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)){
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}
	
	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * @param html
	 * @return
	 */
	public static String replaceMobileHtml(String html){
		if (html == null){
			return "";
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
	}
	
	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * @param txt
	 * @return
	 */
	public static String toHtml(String txt){
		if (txt == null){
			return "";
		}
		return replace(replace(Encodes.escapeHtml(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String abbr2(String param, int length) {
		if (param == null) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		int n = 0;
		char temp;
		boolean isCode = false; // 是不是HTML代码
		boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
		for (int i = 0; i < param.length(); i++) {
			temp = param.charAt(i);
			if (temp == '<') {
				isCode = true;
			} else if (temp == '&') {
				isHTML = true;
			} else if (temp == '>' && isCode) {
				n = n - 1;
				isCode = false;
			} else if (temp == ';' && isHTML) {
				isHTML = false;
			}
			try {
				if (!isCode && !isHTML) {
					n += String.valueOf(temp).getBytes("GBK").length;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (n <= length - 3) {
				result.append(temp);
			} else {
				result.append("...");
				break;
			}
		}
		// 取出截取字符串中的HTML标记
		String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)",
				"$1$2");
		// 去掉不需要结素标记的HTML标记
		temp_result = temp_result
				.replaceAll(
						"</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
						"");
		// 去掉成对的HTML标记
		temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>",
				"$2");
		// 用正则表达式取出标记
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
		Matcher m = p.matcher(temp_result);
		List<String> endHTML = new LinkedList<>();
		while (m.find()) {
			endHTML.add(m.group(1));
		}
		// 补全不成对的HTML标记
		for (int i = endHTML.size() - 1; i >= 0; i--) {
			result.append("</");
			result.append(endHTML.get(i));
			result.append(">");
		}
		return result.toString();
	}
	
	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val){
		if (val == null){
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val){
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val){
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val){
		return toLong(val).intValue();
	}
	

	/**
	 * 驼峰命名法工具
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld" 
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
	 * 驼峰命名法工具
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld" 
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    /**
	 * 驼峰命名法工具
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld" 
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }
 
    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     * @param objectString 对象串
     *   例如：row.user.id
     *   返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString){
    	StringBuilder result = new StringBuilder();
    	StringBuilder val = new StringBuilder();
    	String[] vals = split(objectString, ".");
    	for (int i=0; i<vals.length; i++){
    		val.append("." + vals[i]);
    		result.append("!"+(val.substring(1))+"?'':");
    	}
    	result.append(val.substring(1));
    	return result.toString();
    }

	public static String randomNumber(int len){
		String str = "0123456789";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < len; i++) {
			int num = random.nextInt(10);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}

	public static String randomString(int len){
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < len; i++) {
			int num = random.nextInt(62);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}



	public static String getMd5(String str){
		final Logger logger = LoggerFactory.getLogger(StringUtils.class);
		String value = null;
		try{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] input = str.getBytes();
			byte[] buff = md5.digest(input);
			value = bytesToHex(buff);
		} catch (Exception ex){
			logger.error("md5 Exception:{},{}",ex.getMessage(),ex);
		}
		return value;
	}

	public static String getBase64(String str)  {
//		byte[] bt = str.getBytes();
//		String rst = (new BASE64Encoder()).encodeBuffer(bt);
//		return rst.replace("\r","").replace("\n","");

		try {
			Base64.Encoder be = Base64.getEncoder();
			String b64Result = be.encodeToString(str.getBytes("utf-8"));
			return b64Result;
		} catch (Exception ex){
			return "";
		}
	}

	public static String fromBase64(String str)  {
//		byte[] bt = str.getBytes();
//		String rst = (new BASE64Encoder()).encodeBuffer(bt);
//		return rst.replace("\r","").replace("\n","");

		try {
			Base64.Decoder be = Base64.getDecoder();
			String b64DecodeResult = new String(be.decode(str.getBytes("utf-8")));
			return b64DecodeResult;
		} catch (Exception ex){
			return "";
		}
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();
		// 把数组每一字节换成16进制连成md5字符串
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			digital = bytes[i];

			if (digital < 0) {
				digital += 256;
			}
			if (digital < 16) {
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString();
	}

	//判断一个数字字符串是否为2的x次方
	public static boolean is2Power(String str){
		if(isNumeric(str)){
			return is2Power(toLong(str));
		}
		return false;
	}

	public static boolean is2Power(Long val){
		return ( val & (val - 1 ) ) == 0;
	}

	public static String getPingYin(String src){
		char[] t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i=0;i<t0;i++)
			{
				//判断是否为汉字字符
				if(Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
				{
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
					t4 += t2[0];
				}
				else{
					t4+= Character.toString(t1[i]);
				}
			}
			return t4;
		}
		catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return t4;
	}

	//返回中文的首字母
	public static String getPinYinHeadChar(String str) {
		char word;
		String convert = "";
		String[] pinyinArray = null;
		for (int x = 0; x < str.length(); x++) {
			word = str.charAt(x);
			pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			}else {
				convert += word;
			}
		}
		return convert;
	}

	public static long getCrc32(String str){
		CRC32 crc32 = new CRC32();
		crc32.update(str.getBytes());
		return crc32.getValue();
	}

	public static boolean isZero(Long val){
		return val == null || val == 0;
	}

	public static boolean isZero(Double val){
		return val == null || val == 0;
	}

	// 小于等于0,或者为null
	public static boolean isZero(BigDecimal val){
		return val == null || val.compareTo(new BigDecimal(0)) <= 0;
	}


	// 等于0,或者为null
	public static boolean isOnlyZero(BigDecimal val){
		return val == null || val.compareTo(new BigDecimal(0)) == 0;
	}

	public static boolean isZero(Integer val){
		return val == null || val == 0;
	}

//	public static String toEncodeString(byte[] bytes, String encoding) {
//		String str;
//		if (encoding != null && Charset.isSupported(encoding)) {
//			try {
//				str = new String(bytes, encoding);
//			} catch (UnsupportedEncodingException e) {
//				// Uses same func as Charset.isSupported (cannot happen)
//				str = new String(bytes);
//			}
//		} else {
//			str = new String(bytes);
//		}
//
//		return str;
//	}

	/**
	 * <br>
	 * StringUtils.isNotEmptyByTrim("") = true <br>
	 * StringUtils.isNotEmptyByTrim("  ") = true <br>
	 * StringUtils.isNotEmptyByTrim(null) = true <br>
	 * StringUtils.isNotEmptyByTrim("123") = false <br>
	 * StringUtils.isNotEmptyByTrim(" 123 ") = false
	 */
	public static boolean isEmptyByTrim(String text) {
		if (text == null)
			return true;

		if (text.trim().isEmpty())
			return true;

		return false;
	}

	public static boolean isNotEmptyByTrim(String text) {
		return !isEmptyByTrim(text);
	}

	public static boolean areNotEmpty(String[] values) {

		if((values == null) || (values.length == 0)) {
			return true;
		}

		for (String value : values) {
			if (isEmptyByTrim(value)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 手机号验证
	 *
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;//
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 判断字符串为数字
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isNumber(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}

	/**
	 * 格式化URL
	 * @param url
	 * @param urlMap
	 * @param split
	 * @return
	 */
	public static String formateUrl(String url, Map<String, String> urlMap, String split){
		int i = urlMap.size();
		String prefix =  url.substring(0, url.indexOf(split) + split.length());
		String value = url.substring(url.indexOf(split) + split.length());
		if(!urlMap.containsValue(prefix)){
			i++;
			String v = "u" + i;
			urlMap.put(v, prefix);
			return  v + "$"  + value;
		}else {
			Set<String> keys = urlMap.keySet();
			for (String key : keys) {
				if(urlMap.get(key).equals(prefix)){
					return key + "$" + value;
				}
			}
			return null;
		}
	}

	/**
	 * 获取指定长度随机数字或者字母字符串
	 * @param length
	 * @param numAndChar
	 * @return
	 */
	public static String getRandomString(int length, boolean numAndChar){
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			boolean b = random.nextBoolean();
			if (b && numAndChar) { // 字符串
				str += (char) (65 + random.nextInt(26));// 取得大写字母
			} else { // 数字
				str += String.valueOf(random.nextInt(10));
			}
		}
		return str;
	}


	public static String filterChineseAndEnCharAndNumber(String character) {
		if(character != null && character.length() > 0) {
			character = character.replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "");
		}
		return character;
	}

	public static String filterChinese(String character) {
		if(character != null && character.length() > 0) {
			character = character.replaceAll("[^\\u4e00-\\u9fa5]", "");
		}
		return character;
	}

	public static String filterEnChar(String character) {
		if(character != null && character.length() > 0) {
			character = character.replaceAll("[^a-zA-Z]", "");
		}
		return character;
	}


	public static String getURLDecoderString(String str) {
		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLDecoder.decode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getURLEncoderString(String str) {
		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
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

	public static void main (String [] args) {
		System.out.println(StringUtils.getMd5("<host prod=\"ThWeChat\" ver=\"1.0\" ip=\"192.168.1.1\" id=\"ThWeChat\" lang=\"zh_CN\" timezone=\"+8\" acct=\"tiptop\" timestamp=\"20170622191817001\"/><service prod=\"T100\" name=\"WXMemberInfoGet\" srvver=\"1.0\" ip=\"172.169.10.76\" id=\"topprd\"/>"));

	}

}
