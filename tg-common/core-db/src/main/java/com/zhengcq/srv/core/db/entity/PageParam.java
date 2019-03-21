package com.zhengcq.srv.core.db.entity;

public class PageParam<T> {

    private Page page;
    private T params;
    private boolean noPager = false;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }

    public boolean isNoPager() {
        return noPager;
    }

    public void setNoPager(boolean noPager) {
        this.noPager = noPager;
    }

    public PageParam() {
//		super();
        // TODO Auto-generated constructor stub
    }

    public PageParam(Page page, T params) {
        super();
        this.page = page;
        this.params = params;
    }

//    @JsonIgnore
//    public PageParam toPageParam(){
//        PageParam pm = new PageParam();
//        pm.putAll(params);
//        pm.setPage(page);
//        return pm;
//    }


}