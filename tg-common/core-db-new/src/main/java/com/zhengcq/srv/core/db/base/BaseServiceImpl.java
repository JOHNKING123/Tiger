package com.zhengcq.srv.core.db.base;

import com.baomidou.mybatisplus.entity.TableInfo;
import com.baomidou.mybatisplus.enums.SqlMethod;
import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.*;
import com.zhengcq.srv.core.db.entity.Page;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

//import com.baomidou.mybatisplus.plugins.Page;

/**
 * Created by clude on 3/13/17.
 */
public class BaseServiceImpl<M extends BaseDao<T>, T extends SuperBaseModel> implements BaseService<T> {
//    protected static final Log logger = LogFactory.getLog(BaseServiceImpl.class);
    protected static final Logger logger   = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired
    protected M baseDao;

    @SuppressWarnings("unchecked")
    protected Class<T> currentModleClass() {
        return ReflectionKit.getSuperClassGenricType(getClass(), 1);
    }

    /**
     * <p>
     * 批量操作 SqlSession
     * </p>
     */
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(currentModleClass());
    }

    /**
     * 获取SqlStatement
     *
     * @param sqlMethod
     * @return
     */
    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModleClass()).getSqlStatement(sqlMethod.getMethod());
    }

    /**
     * <p>
     * 判断数据库操作是否成功
     * </p>
     * <p>
     * 注意！！ 该方法为 Integer 判断，不可传入 int 基本类型
     * </p>
     *
     * @param result
     *            数据库操作返回影响条数
     * @return boolean
     */
    protected static boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    /**
     * <p>
     * TableId 注解存在更新记录，否插入一条记录
     * </p>
     *
     * @param entity
     *            实体对象
     * @return boolean
     */
    public boolean insertOrUpdate(T entity) {
        entity.preUpdate();
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal)) {
                    return insert(entity);
                } else {
					/*
					 * 更新成功直接返回，失败执行插入逻辑
					 */
                    boolean rlt = updateById(entity);
                    if (!rlt) {
                        return insert(entity);
                    }
                    return rlt;
                }
            } else {
                throw new MybatisPlusException("Error:  Can not execute. Could not find @TableId.");
            }
        }
        return false;
    }

    public boolean insert(T entity) {
        entity.preInsert();
        return retBool(baseDao.insert(entity));
    }

    public boolean insertBatch(List<T> entityList) {
        return insertBatch(entityList, 30);
    }

    public boolean insertOrUpdateBatch(List<T> entityList) {
        return insertOrUpdateBatch(entityList, 30);
    }

    public boolean insertOrUpdateBatch(List<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        try {
            SqlSession batchSqlSession = sqlSessionBatch();
            int size = entityList.size();
            for (int i = 0; i < size; i++) {
                insertOrUpdate(entityList.get(i));
                if (i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Exception e) {
            logger.warn("Error: Cannot execute insertOrUpdateBatch Method. Cause:" + e);
            return false;
        }
        return true;
    }

    /**
     * 批量插入
     *
     * @param entityList
     * @param batchSize
     * @return
     */
    public boolean insertBatch(List<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        SqlSession batchSqlSession = sqlSessionBatch();
        try {
            int size = entityList.size();
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), entityList.get(i));
                if (i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Exception e) {
            logger.warn("Error: Cannot execute insertBatch Method. Cause:" + e);
            return false;
        }
        return true;

    }

    public boolean deleteById(Serializable id) {
        return retBool(baseDao.deleteById(id));
    }

    public boolean disableById(Serializable id) {
        T obj = baseDao.selectById(id);
        if(obj instanceof BaseSoftRemoveModel){
            ((BaseSoftRemoveModel) obj).setEnable(0);
            return retBool(baseDao.updateById(obj));
        }
        return false;
    }

    public boolean deleteByMap(Map<String, Object> columnMap) {
        if (MapUtils.isEmpty(columnMap)) {
            throw new MybatisPlusException("deleteByMap columnMap is empty.");
        }
        return retBool(baseDao.deleteByMap(columnMap));
    }

    public boolean delete(Wrapper<T> wrapper) {
        return retBool(baseDao.delete(wrapper));
    }

    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        return retBool(baseDao.deleteBatchIds(idList));
    }

    public boolean updateById(T entity) {
        entity.preUpdate();
        return retBool(baseDao.updateById(entity));
    }

    public boolean update(T entity, Wrapper<T> wrapper) {
        entity.preUpdate();
        return retBool(baseDao.update(entity, wrapper));
    }

    public boolean updateBatchById(List<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        SqlSession batchSqlSession = sqlSessionBatch();
        try {
            int size = entityList.size();
            for (int i = 0; i < size; i++) {
                batchSqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), entityList.get(i));
                if (i % 30 == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Exception e) {
            logger.warn("Error: Cannot execute insertBatch Method. Cause:" + e);
            return false;
        }
        return true;
    }

    public T selectById(Serializable id) {
        return baseDao.selectById(id);
    }

    public List<T> selectBatchIds(List<? extends Serializable> idList) {
        return baseDao.selectBatchIds(idList);
    }

    public List<T> selectByMap(Map<String, Object> columnMap) {
        return baseDao.selectByMap(columnMap);
    }

    public T selectOne(Wrapper<T> wrapper) {
        return SqlHelper.getObject(baseDao.selectList(wrapper));
    }

    public Map<String, Object> selectMap(Wrapper<T> wrapper) {
        return SqlHelper.getObject(baseDao.selectMaps(wrapper));
    }

    public Object selectObj(Wrapper<T> wrapper) {
        return SqlHelper.getObject(baseDao.selectObjs(wrapper));
    }

    public int selectCount(Wrapper<T> wrapper) {
        return SqlHelper.retCount(baseDao.selectCount(wrapper));
    }

    public List<T> selectList(Wrapper<T> wrapper) {
        return baseDao.selectList(wrapper);
    }

    public Page<T> selectPage(Page<T> page) {
        page.setRecords(baseDao.selectPage(page, null));
        return page;
    }

    public List<Map<String, Object>> selectMaps(Wrapper<T> wrapper) {
        return baseDao.selectMaps(wrapper);
    }

    public List<Object> selectObjs(Wrapper<T> wrapper) {
        return baseDao.selectObjs(wrapper);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Page<Map<String, Object>> selectMapsPage(Page page, Wrapper<T> wrapper) {
        SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseDao.selectMapsPage(page, wrapper));
        return page;
    }

    public Page<T> selectPage(Page<T> page, Wrapper<T> wrapper) {
        SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseDao.selectPage(page, wrapper));
        return page;
    }

    public Page<T> selectPage(Page<T> page, T entity) {
        EntityWrapper<T> ew = new EntityWrapper<T>(entity);
        page.setRecords(baseDao.selectPage(page, ew));
        return page;
    }
}
