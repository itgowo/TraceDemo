package com.itgowo.sport.trace.tracedemo.Trace.Entity;

/**
 * Created by lujianchao on 2017/1/8.
 *
 * @author lujianchao
 */

public class LocationInfo {

        private double latitude;
        private double longitude;
        private int status;//1 正常轨迹   2.暂停状态轨迹

        public double getLatitude () {
                return latitude;
        }

        public LocationInfo setLatitude (final double mLatitude) {
                latitude = mLatitude;
                return this;
        }

        public double getLongitude () {
                return longitude;
        }

        public LocationInfo setLongitude (final double mLongitude) {
                longitude = mLongitude;
                return this;
        }

        public int getStatus () {
                return status;
        }

        public LocationInfo setStatus (final int mStatus) {
                status = mStatus;
                return this;
        }
}
