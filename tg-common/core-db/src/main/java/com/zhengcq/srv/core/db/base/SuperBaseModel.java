package com.zhengcq.srv.core.db.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zhengcq.srv.core.db.json.IdWorkerDeserializer;
//import com.zhengcq.srv.core.model.constants.GlobalConstant;
//import com.zhengcq.srv.core.util.ThreadLocalMap;
import com.zhengcq.srv.core.common.utils.StringUtils;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuperBaseModel implements Serializable {
    @JsonSerialize(using=ToStringSerializer.class)
    @JsonDeserialize(using=IdWorkerDeserializer.class)
    private Long createdBy;

    private Date createdTime;

    @JsonSerialize(using=ToStringSerializer.class)
    @JsonDeserialize(using=IdWorkerDeserializer.class)
    private Long updatedBy;

    private Date updatedTime;

    /**
     * @return the createBy
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     *            the createBy to set
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createTime
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     *            the createTime to set
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return the updatedBy
     */
    public Long getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy
     *            the updateBy to set
     */
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * @return the updateTime
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     *            the updateTime to set
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @JsonIgnore
    public void preInsert(){
        if(this.getCreatedTime() == null){
            this.setCreatedTime(new Date());
        }

        if(this.getUpdatedTime() == null){
            this.setUpdatedTime(new Date());
        }

        if(this.createdBy!= null && this.updatedBy == null ){
            this.updatedBy = this.createdBy;
        }

        if (StringUtils.isZero(this.createdBy)) {
            this.setCreatedBy(getOperatorId());
        }

        if (StringUtils.isZero(this.updatedBy)) {
            this.setUpdatedBy(this.createdBy);
        }
    }

    @JsonIgnore
    public void preUpdate(){
        if(this.getUpdatedTime() == null){
            this.setUpdatedTime(new Date());
        }

        if (StringUtils.isZero(this.updatedBy)) {
            this.setUpdatedBy(getOperatorId());
        }
    }


//    /**
//     * 最终操作用户ID
//     * 为了兼容现有的方法，减少改造难度，这里直接从ThreadLocal里获取登录用户ID
//     * 合理的做法应该是改造方法，从web controller传登录用户信息给service， 然后service 在设置相应操作用户ID
//     * @return
//     */
    private Long getOperatorId() {
//        try{
//            Long userId = (Long) ThreadLocalMap.get(GlobalConstant.Sys.CURRENT_AUTH_ID);
//            if (userId == null) {
//                return 0L;
//            }
//            return userId;
//        } catch (Exception e) {
//            // 加try / catch以防万一，不影响主流程， 暂时不记录error, 直接吃掉
//        }
        return 0L;

    }
}
