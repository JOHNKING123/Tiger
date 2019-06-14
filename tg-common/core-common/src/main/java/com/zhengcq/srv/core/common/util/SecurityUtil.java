package com.zhengcq.srv.core.common.util;

import com.zhengcq.srv.core.common.support.security.BASE64Encoder;
import com.zhengcq.srv.core.common.support.security.coder.*;

/**
 * 数据加密辅助类(默认编码UTF-8)
 *
 * @author YoorstoreTech
 * @since 2011-12-31
 */
public final class SecurityUtil {
	private SecurityUtil() {
	}

	/**
	 * 默认算法密钥
	 */
	private static final byte[] ENCRYPT_KEY = { -81, 0, 105, 7, -32, 26, -49, 88 };

	public static final String CHARSET = "UTF-8";

	/**
	 * BASE64解码
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static final byte[] decryptBASE64(String key) {
		try {
			return new BASE64Encoder().decode(key);
		} catch (Exception e) {
			throw new RuntimeException("解密错误，错误信息：", e);
		}
	}

	/**
	 * BASE64编码
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static final String encryptBASE64(byte[] key) {
		try {
			return new BASE64Encoder().encode(key);
		} catch (Exception e) {
			throw new RuntimeException("加密错误，错误信息：", e);
		}
	}

	/**
	 * 数据解密，算法（DES）
	 *
	 * @param cryptData 加密数据
	 * @return 解密后的数据
	 */
	public static final String decryptDes(String cryptData) {
		return decryptDes(cryptData, ENCRYPT_KEY);
	}

	/**
	 * 数据加密，算法（DES）
	 *
	 * @param data 要进行加密的数据
	 * @return 加密后的数据
	 */
	public static final String encryptDes(String data) {
		return encryptDes(data, ENCRYPT_KEY);
	}

	/**
	 * 基于MD5算法的单向加密
	 *
	 * @param strSrc 明文
	 * @return 返回密文
	 */
	public static final String encryptMd5(String strSrc) {
		String outString = null;
		try {
			outString = encryptBASE64(MDCoder.encodeMD5(strSrc.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new RuntimeException("加密错误，错误信息：", e);
		}
		return outString;
	}

	/**
	 * SHA加密
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static final String encryptSHA(String data) {
		try {
			return encryptBASE64(SHACoder.encodeSHA256(data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new RuntimeException("加密错误，错误信息：", e);
		}
	}

	/**
	 * HMAC加密
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static final String encryptHMAC(String data) {
		return encryptHMAC(data, ENCRYPT_KEY);
	}

	/**
	 * 数据解密，算法（DES）
	 *
	 * @param cryptData 加密数据
	 * @return 解密后的数据
	 */
	public static final String decryptDes(String cryptData, byte[] key) {
		String decryptedData = null;
		try {
			// 把字符串解码为字节数组，并解密
			decryptedData = new String(DESCoder.decrypt(decryptBASE64(cryptData), key));
		} catch (Exception e) {
			throw new RuntimeException("解密错误，错误信息：", e);
		}
		return decryptedData;
	}

	/**
	 * 数据加密，算法（DES）
	 *
	 * @param data 要进行加密的数据
	 * @return 加密后的数据
	 */
	public static final String encryptDes(String data, byte[] key) {
		String encryptedData = null;
		try {
			// 加密，并把字节数组编码成字符串
			encryptedData = encryptBASE64(DESCoder.encrypt(data.getBytes(), key));
		} catch (Exception e) {
			throw new RuntimeException("加密错误，错误信息：", e);
		}
		return encryptedData;
	}

	/**
	 * HMAC加密
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static final String encryptHMAC(String data, byte[] key) {
		try {
			return encryptBASE64(HmacCoder.encodeHmacSHA512(data.getBytes(CHARSET), key));
		} catch (Exception e) {
			throw new RuntimeException("加密错误，错误信息：", e);
		}
	}

	/**
	 * RSA签名
	 *
	 * @param data 原数据
	 * @return
	 */
	public static final String signRSA(String data, String privateKey) {
		try {
			return encryptBASE64(RSACoder.sign(data.getBytes(CHARSET), decryptBASE64(privateKey)));
		} catch (Exception e) {
			throw new RuntimeException("签名错误，错误信息：", e);
		}
	}

	/**
	 * RSA验签
	 *
	 * @param data 原数据
	 * @return
	 */
	public static final boolean verifyRSA(String data, String publicKey, String sign) {
		try {
			return RSACoder.verify(data.getBytes(CHARSET), decryptBASE64(publicKey), decryptBASE64(sign));
		} catch (Exception e) {
			throw new RuntimeException("验签错误，错误信息：", e);
		}
	}

	/**
	 * 数据加密，算法（RSA）
	 *
	 * @param data 数据
	 * @return 加密后的数据
	 */
	public static final String encryptRSAPrivate(String data, String privateKey) {
		try {
			return encryptBASE64(RSACoder.encryptByPrivateKey(data.getBytes(CHARSET), decryptBASE64(privateKey)));
		} catch (Exception e) {
			throw new RuntimeException("解密错误，错误信息：", e);
		}
	}

	/**
	 * 数据解密，算法（RSA）
	 *
	 * @param cryptData 加密数据
	 * @return 解密后的数据
	 */
	public static final String decryptRSAPublic(String cryptData, String publicKey) {
		try {
			// 把字符串解码为字节数组，并解密
			return new String(RSACoder.decryptByPublicKey(decryptBASE64(cryptData), decryptBASE64(publicKey)));
		} catch (Exception e) {
			throw new RuntimeException("解密错误，错误信息：", e);
		}
	}


	/**
	 * generate password，generate random 16bit salt and process 1024 times sha-1
	 * hash
	 */
	public static String encryptPassword(String plainPassword) {
		return encryptMd5(SecurityUtil.encryptSHA(plainPassword));
	}

	/**
	 * validate password
	 *
	 * @param plainPassword
	 * @param password
	 *            -- encrypted password
	 * @return true/false
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		return password.equals(encryptPassword(plainPassword));
	}

	public static void main(String[] args) throws Exception {
		System.out.println(encryptBASE64("<?xml version=\"1.0\" encoding=\"gbk\"?><business id=\"FPKJ\" comment=\"发票开具\"><HTJS_DZFPKJ class=\"HTJS_DZFPKJ\"><COMMON_FPKJ_FPT class=\"COMMON_FPKJ_FPT\"><FPQQLSH>151623408</FPQQLSH><KPLX>0</KPLX><BMB_BBH>0010</BMB_BBH><ZSFS>0</ZSFS><XSF_NSRSBH>554433221100000</XSF_NSRSBH><XSF_MC>广东百望测试1</XSF_MC><XSF_DZDH>测试1111</XSF_DZDH><XSF_YHZH>测试111</XSF_YHZH><XSF_LXFS></XSF_LXFS><GMF_NSRSBH></GMF_NSRSBH><GMF_MC>彭小雄</GMF_MC><GMF_DZDH>测试1111</GMF_DZDH><GMF_YHZH>测试1111</GMF_YHZH><GMF_LXFS>kesl@genechem.com.cn</GMF_LXFS><KPR>开票人</KPR><SKR>收款人</SKR><FHR>复核人</FHR><YFP_DM></YFP_DM><YFP_HM></YFP_HM><JSHJ>200</JSHJ><HJJE>172.41</HJJE><HJSE>27.59</HJSE><KCE></KCE><BZ></BZ><GMF_LXFS>kesl@genechem.com.cn</GMF_LXFS></COMMON_FPKJ_FPT><COMMON_FPKJ_XMXXS class=\"COMMON_FPKJ_XMXX\" size=\"1\"><COMMON_FPKJ_XMXX><FPHXZ>0</FPHXZ><SPBM>3040502020200000000</SPBM><ZXBM></ZXBM><YHZCBS></YHZCBS><LSLBS></LSLBS><ZZSTSGL></ZZSTSGL><XMMC>92#</XMMC><GGXH>1*1000</GGXH><DW>升</DW><XMSL>27.43</XMSL><XMDJ>6.285454</XMDJ><XMJE>172.41</XMJE><SL>0.16</SL><SE>27.59</SE></COMMON_FPKJ_XMXX></COMMON_FPKJ_XMXXS></HTJS_DZFPKJ></business>".getBytes(CHARSET)));
		System.out.println(new String(decryptBASE64("pdZU2wM74CvO3SBL20Q9msUMi9bB2CnLzgBKzPQ9zSjHi0Y+pgjR3SBKzxnPigBApsjgueJki9b02SRJzwVQpslB0VhDF10BLidB8239p0NiveFtxQr1rBbls9b02gfP3PQ9sfrkuRZewAzqsQE9p0NdtQRntQV6rBblsBZgufq7ySN83TmZiAnptuRptBZgueJkxQzqvci+pezquvfmuQ7+mtuNn0iPndaUpcZguffrtfnip0NlueNyp0aYlQJqtf7+pejnqBZcqA7+mdaNmdMLqARcxQjcsdUYwBnguPUMpcZ1uQztp0NyuQz6tBnsuQjip0uRndqPmPiOmteMmdaMmdMLwfngxQVtuBncsdUYwfngxQRdpKwVL+sUDo5zLK13C+1R9+9LBteYlR8trBZnqPUYwfngxQr1re7+VHwlWk+vmteNmtMLwfngxQr1re7+pf8trBZzsfFipK1R9+9LBteNmtMLwfngxRBiwA7+pf8trBZmweztp0MLwfngxQNyrBm+pe4nrBZouRjtqA7+pcZhtuz6tBnsuQjip0Nhtuz6tum+V2SJV2cpWzKepcZhtuz6tum+pe4nrBZewAripK1R9+9LBteNmteYlQ4nrBZewArip0Nhtuz6wu81sdXCJyLEHVuNmteNpcZhtuz6wu81sdUYrQRgxQNyrBm+1SvP2ebDzwVByS8B2sV02SQKySUYlQ4nrBZmweztp0Nlufi+V2OaVWwEVlGWpcZlufi+pfnlu0XCBl2CHlXAKHEYlRnlu0UYrA8spKwA0517KosWK0MLrA8sp0NzrBb6reQ+pcZzrBb6reQ+pfBgufZittUYlRBgufZittUYsBnis0UOmdaYlQFtseE+pe8ksAu+mt3Ol0qNpcZisAFfp0NisBnfp0iTl0uVpcZisBnfp0NlqQu+pcZlqQu+pej1p0MLqBE+pe4nrBZmweztpCJB3SNazSvKzwnEzwQKySZJlCnKpcZhtuz6tf8guPUYlQnptuRptBZgueJkxQzqvdUYqQZntuZoxQzqsQF6weRywfm7ySN83TmZiAnptuRptBZgueJkxR8nwf79ihnF5CuZi0e9p0NdtQRntQV6rBblsBZytv8yp0Ngue8yw0UMpcZgue8yw0UYuRbcttUPmdqMntaOmdiMm0aMmdaMmdaMpcZtuejnp0N1wejnp0MLwB8cttUYwu81qQjtp0MLwu81qQjtp0NmuQNcuPUYlQNttejtp0N1wBnuuQ4mp0MLwBFtvfnhtdUYweRnqPUVm9mYlR8ntum+pe4hwe7+msENmdaMpcZhrR8ip0NevPXB0y3YlQrxp0Nytvnmp0iTl0qPpcZytvnmp0Nyturkp0yKm07RnduQpcZyturkp0NytuFfp0eTm9UQmtMLweRkrtUYuQM+mcUNn0MLuQM+pfnfp0iTl0uVpcZtrtUYlQnptuRptBZgueJkxR8nwf7+pcZdtQRntQV6rBblsBZytv8yuPUYlQ8usBn6rfFgueJkp0MLyDvP1wVB3Tm+"),CHARSET));
// 		System.out.println(encryptDes("SHJR"));
//		System.out.println(decryptDes("INzvw/3Qc4q="));
//		System.out.println(encryptMd5("SHJR"));
//		System.out.println(encryptSHA("1"));
//		Map<String, Object> key = RSACoder.initKey();
//		String privateKey = encryptBASE64(RSACoder.getPrivateKey(key));
//		String publicKey = encryptBASE64(RSACoder.getPublicKey(key));
//		System.out.println(privateKey);
//		System.out.println(publicKey);
//		String sign = signRSA("132", privateKey);
//		System.out.println(sign);
//		String encrypt = encryptRSAPrivate("132", privateKey);
//		System.out.println(encrypt);
//		String org = decryptRSAPublic(encrypt, publicKey);
//		System.out.println(org);
//		System.out.println(verifyRSA(org, publicKey, sign));

		// System.out.println("-------列出加密服务提供者-----");
		// Provider[] pro = Security.getProviders();
		// for (Provider p : pro) {
		// System.out.println("Provider:" + p.getName() + " - version:" +
		// p.getVersion());
		// System.out.println(p.getInfo());
		// }
		// System.out.println("");
		// System.out.println("-------列出系统支持的消息摘要算法：");
		// for (String s : Security.getAlgorithms("MessageDigest")) {
		// System.out.println(s);
		// }
		// System.out.println("-------列出系统支持的生成公钥和私钥对的算法：");
		// for (String s : Security.getAlgorithms("KeyPairGenerator")) {
		// System.out.println(s);
		// }
	}
}
