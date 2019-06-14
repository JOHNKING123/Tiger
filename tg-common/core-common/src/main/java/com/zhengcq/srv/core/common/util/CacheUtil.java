package com.zhengcq.srv.core.common.util;

import com.zhengcq.srv.core.common.Constants;
import com.zhengcq.srv.core.common.support.cache.CacheManager;
import com.zhengcq.srv.core.common.support.cache.RedissonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CacheUtil {
	private static Logger logger = LoggerFactory.getLogger(CacheUtil.class);

	private CacheUtil() {
	}

	public static CacheManager getCache() {
		return new RedissonHelper();
	}

	/** 获取锁 */
	public static boolean getLock(String key) {
		if (!getCache().exists(key)) {
			synchronized (CacheUtil.class) {
				if (!getCache().exists(key)) {
					if (getCache().setnx(key, String.valueOf(System.currentTimeMillis()))) {
						return true;
					}
				}
			}
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			logger.error(Constants.Exception_Head, e);
		}
		return getLock(key);
	}

	public static void unlock(String key) {
		getCache().unlock(key);
	}
}