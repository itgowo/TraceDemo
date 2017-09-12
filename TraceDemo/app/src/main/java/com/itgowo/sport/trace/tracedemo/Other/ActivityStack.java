package com.itgowo.sport.trace.tracedemo.Other;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lu on 2015/10/21.
 * 创建Activity栈，提供父类通用方法，方便管理
 *
 * @author lujianchao
 */
public class ActivityStack {
    private static ActivityStack activityStack = new ActivityStack ();

    /**
     * 单例——ActivityStack
     */
    private ActivityStack() {
    }

    public static ActivityStack getInstance () {
        return activityStack;
    }

    /**
     * Activity栈
     */
    private List<Activity> activities = new ArrayList<Activity> ();

    /**
     * add a activity to Stack
     * 添加activity到栈
     *
     * @param activity
     */
    public void pushStack (Activity activity) {
        activities.add (activity);
    }

    /**
     * remove a activity to Stack
     * 从栈移出一个activity
     *
     * @param activity
     */
    public void popStack (Activity activity) {
        activities.remove (activity);
    }

    /**
     * 销毁Activity
     * destroy activity
     *
     * @param activityClass
     */

    public void killActivity (Class activityClass) {
        for (Activity a : activities) {
            if (a.getClass ().getName ().equals (activityClass.getName ())) {
                a.finish ();
            }
        }
    }

    /**
     * 销毁Activity
     * destroy activity
     *
     * @param activityClassName
     */
    public void killActivity (String activityClassName) {
        for (Activity a : activities) {
            if (a.getClass ().getName ().equals (activityClassName)) {
                a.finish ();
            }
        }
    }

    /**
     * 完全退出
     * Exit  Application
     */
    public void exit () {
        for (Activity a : activities) {
            a.finish ();
        }
        System.exit (0);
    }

    /**
     * 判断activity栈里是否有对应的activity
     * @param mActivity
     * @return
     */
    public boolean getActivityIsLive (Class mActivity) {
        for (Activity a : activities) {
            if (a.getClass ().getName () == mActivity.getName ()) {
                return true;
            }
        }
        return false;
    }
    /**
     * 获取activity栈里对应的activity
     * @param mActivity
     * @return
     */
    public Activity getActivity  (Class mActivity) {
        for (Activity a : activities) {
            if (a.getClass ().getName () == mActivity.getName ()) {
                return a;
            }
        }
        return null;
    }
    /**
     * 关闭所有界面
     */
    public void FinishAllActivity () {
        for (Activity a : activities) {
            a.finish ();
        }

    }
}
