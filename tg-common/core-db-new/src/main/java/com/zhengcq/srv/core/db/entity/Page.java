package com.zhengcq.srv.core.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by clude on 3/13/17.
 */
@JsonIgnoreProperties({ "searchCount", "optimizeCount", "offsetCurrent", "condition", "limit", "offset"})
public class Page<T> extends com.baomidou.mybatisplus.plugins.Page<T> {

    private static final long serialVersionUID = 1L;

    public Page() {
		/* 注意，传入翻页参数 */
    }

    public Page(int current, int size) {
        super(current, size);
    }

    public Page(int current, int size, String orderByField) {
        super(current, size);
        this.setOrderByField(orderByField);
    }

    public void setCurrent(int current) {
        super.setCurrent(current);
        this.restRowBounds();
    }
    public void setSize(int size) {
        super.setSize(size);
        this.restRowBounds();
    }

    private void restRowBounds(){
        super.setLimit(this.getSize());
        super.setOffset(offsetCurrent(this.getCurrent(), this.getSize()));
    }

}
