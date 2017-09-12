package com.itgowo.sport.trace.tracedemo.Trace.Entity;

import com.itgowo.sport.trace.tracedemo.Other.Response;

/**
 * Created by hnvfh on 2017/5/16.
 */

public class Entity_UploadResponse extends Response {
    private EntityRecord mData;

    @Override
    public EntityRecord getData() {
        return mData;
    }

    public Entity_UploadResponse setData(EntityRecord mData) {
        this.mData = mData;
        return this;
    }
}
