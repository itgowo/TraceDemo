package com.itgowo.sport.trace.tracedemo.Trace.Entity;

/**
 * Created by hnvfh on 2017/5/15.
 */
public class RequestTrace {
    public static final int RequestNo = 0, RequestDetails = 1, RequestList = 2, RequestUpload = 3, RequestInfo = 4,RequestDel=5;
    private int requestType = 0;  //0;不请求数据  1：根据id获取详情（id）    2：获取轨迹列表(pageindex,pagesize)   3:上传轨迹   4:获取统计信息  关联运动类型，用id传
    private int id;
    private int pageindex = 1;
    private int pagesize = 20;
    private Object entity;

    public int getRequestType() {
        return requestType;
    }

    public RequestTrace setRequestType(int mRequestType) {
        requestType = mRequestType;
        return this;
    }

    public int getId() {
        return id;
    }

    public RequestTrace setId(int mId) {
        id = mId;
        return this;
    }

    public int getPageindex() {
        return pageindex;
    }

    public RequestTrace setPageindex(int mPageindex) {
        pageindex = mPageindex;
        return this;
    }

    public int getPagesize() {
        return pagesize;
    }

    public RequestTrace setPagesize(int mPagesize) {
        pagesize = mPagesize;
        return this;
    }

    public Object getEntity() {
        return entity;
    }

    public RequestTrace setEntity(Object mEntity) {
        entity = mEntity;
        return this;
    }
}
