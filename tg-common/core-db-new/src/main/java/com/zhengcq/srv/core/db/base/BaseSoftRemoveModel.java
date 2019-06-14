package com.zhengcq.srv.core.db.base;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseSoftRemoveModel extends BaseModel {
	@TableField("enable")
	private Integer enable;
	@TableField("remark")
	private String remark;

	/**
	 * @return the enable
	 */
	public Integer getEnable() {
		return enable;
	}

	/**
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

}
