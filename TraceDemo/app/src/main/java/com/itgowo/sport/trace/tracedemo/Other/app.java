package com.itgowo.sport.trace.tracedemo.Other;

import android.app.Application;
import android.os.Build;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

/**
 * Created by hnvfh on 2017/5/15.
 */

public class app extends Application {
    public static Application sApp;
    public static String sNickName= "晴空的一滴雨 itgowo.com";
    @Override
    public void onCreate() {
        super.onCreate();
        sApp=this;
        x.Ext.init(this);
        SDKInitializer.initialize(this);
        qktool.initTool(sApp);
    }

}
