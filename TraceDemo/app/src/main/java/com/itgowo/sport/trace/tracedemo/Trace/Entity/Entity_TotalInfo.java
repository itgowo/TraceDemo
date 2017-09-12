package com.itgowo.sport.trace.tracedemo.Trace.Entity;

import com.itgowo.sport.trace.tracedemo.Other.Response;

/**
 * Created by hnvfh on 2017/5/16.
 */
public class Entity_TotalInfo extends Response {
    private TotalInfo mData;

    @Override
    public TotalInfo getData() {
        return mData;
    }

    public Entity_TotalInfo setData(TotalInfo mData) {
        this.mData = mData;
        return this;
    }

    public static class TotalInfo {
        private double totalmileage; // 里程
        private long totalTime; // 用时
        private int type; // 类型

        public int getType() {
            return type;
        }

        public TotalInfo setType(int mType) {
            type = mType;
            return this;
        }
        public double getTotalmileage() {
            return totalmileage;
        }

        public TotalInfo setTotalmileage(double mTotalmileage) {
            totalmileage = mTotalmileage;
            return this;
        }

        public long getTotalTime() {
            return totalTime;
        }

        public TotalInfo setTotalTime(long mTotalTime) {
            totalTime = mTotalTime;
            return this;
        }
    }
}
