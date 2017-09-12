package com.itgowo.sport.trace.tracedemo.Trace.Entity;


import com.itgowo.sport.trace.tracedemo.Other.Response;

import java.util.List;

/**
 * Created by lujianchao on 2017/2/10.
 *
 * @author lujianchao
 */

public class Entity_GetRunRecord extends Response {
    private EntityRecord data;

    public EntityRecord getData() {
        return data;
    }

    public void setData(EntityRecord data) {
        this.data = data;
    }
}
