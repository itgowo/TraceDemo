package com.itgowo.sport.trace.tracedemo.Trace.Entity;

import com.itgowo.sport.trace.tracedemo.Other.Response;

import java.util.List;


/**
 * Created by lujianchao on 2017/2/8.
 *
 * @author lujianchao
 */

public class Entity_GetRunRecordList extends Response {

    private List<EntityRecord> data;

    public List<EntityRecord> getData() {
        return data;
    }

    public void setData(List<EntityRecord> data) {
        this.data = data;
    }

}
