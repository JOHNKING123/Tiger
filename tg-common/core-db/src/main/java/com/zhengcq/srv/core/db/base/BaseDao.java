/**
 * 
 */
package com.zhengcq.srv.core.db.base;

import com.zhengcq.srv.core.db.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author YoorstoreTech
 * 
 * @version 2016年6月3日 下午2:30:14
 * 
 */
@MyBatisDao
public interface BaseDao<T extends SuperBaseModel> extends com.baomidou.mybatisplus.mapper.BaseMapper<T> {

	List<Long> selectIdPage(@Param("cm") Map<String, Object> params);

	List<Long> selectIdPage(RowBounds rowBounds, @Param("cm") Map<String, Object> params);

}
