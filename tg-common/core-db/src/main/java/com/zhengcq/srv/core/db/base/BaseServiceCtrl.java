package com.zhengcq.srv.core.db.base;//package com.zhengcq.core.db.base;
//
//import com.baomidou.mybatisplus.plugins.Page;
//import com.zhengcq.core.Constants;
//import com.zhengcq.core.support.Assert;
//import com.zhengcq.core.util.CacheUtil;
//import com.zhengcq.core.utils.web.WebUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * @author YoorstoreTech
// * @version 2016年5月20日 下午3:47:58
// */
//public abstract class BaseServiceCtrl<P extends BaseProvider<T>, T extends BaseModel> {
//	protected static Logger logger = LoggerFactory.getLogger(BaseServiceCtrl.class);
//	protected P provider;
//
//	/** 修改 */
//	public void update(T record) {
//		Long uid = WebUtils.getCurrentUser();
//		if (record.getId() == null) {
//			record.setCreateBy(uid == null ? 1 : uid);
//		}
//		record.setUpdateBy(uid == null ? 1 : uid);
//		provider.update(record);
//	}
//
//	/** 删除 */
//	public void del(Long id) {
//		Assert.notNull(id, "ID");
//		provider.del(id, WebUtils.getCurrentUser());
//	}
//
//	/** 删除 */
//	public void delete(Long id) {
//		Assert.notNull(id, "ID");
//		provider.delete(id);
//	}
//
//	/** 根据Id查询 */
//	@SuppressWarnings("unchecked")
//	public T queryById(Long id) {
//		Assert.notNull(id, "ID");
//		StringBuilder sb = new StringBuilder(Constants.CACHE_NAMESPACE);
//		String className = this.getClass().getSimpleName().replace("Service", "");
//		sb.append(className.substring(0, 1).toLowerCase()).append(className.substring(1));
//		sb.append(":").append(id);
//		T record = (T) CacheUtil.getCache().get(sb.toString());
//		if (record == null) {
//			record = provider.queryById(id);
//		}
//		return record;
//	}
//
//	/** 条件查询 */
//	public Page<T> query(Map<String, Object> params) {
//		return provider.query(params);
//	}
//
//	/** 条件查询 */
//	public List<T> queryList(Map<String, Object> params) {
//		return provider.queryList(params);
//	}
//
//	/** 条件查询 */
//	public Page<Map<String, Object>> queryMap(Map<String, Object> params) {
//		return provider.queryMap(params);
//	}
//}
