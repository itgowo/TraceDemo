package com.itgowo.sport.trace.tracedemo.Other;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.itgowo.sport.trace.tracedemo.R;


/**
 * Created by lujianchao on 2016/11/16.
 *
 * @author lujianchao
 */

public abstract class BaseActivity extends AppCompatActivity {
    public final int ACTIVITY_CAMREA = 100, ACTIVITY_ALBUM = 101, ACTIVITY_CUT = 102;
    public final int ACTIVITY_RESULT_MatchSportActivity = 1001, ACTIVITY_MatchCitySearchActivity = 1002;
    public static final String USERID = "userid";
    public static final String MATCHID = "matchid";
    private String tag;
    public TitleBarView mTitleBarView;
    public Activity mContext;
    public float SrceenDensity, SrceenWidth, SrceenHeight;
    private SystemBarTintUtil tintManager;

    /**
     * 控制Log输出
     * 测试版isrelease=false，发行版为true
     */
    private ActivityStack myactivityStack = ActivityStack.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        myactivityStack.pushStack(this);
        tag = this.getClass().getSimpleName();
        SrceenDensity = getResources().getDisplayMetrics().density;
        SrceenWidth = getResources().getDisplayMetrics().widthPixels;
        SrceenHeight = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void setContentView(final int layoutResID) {
        super.setContentView(layoutResID);
        mTitleBarView = (TitleBarView) getWindow().getDecorView().findViewWithTag(TitleBarView.Tag);
        if (mTitleBarView != null) {
            initTitleBarData(mTitleBarView);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        tintManager = new SystemBarTintUtil(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        if (setTintColor() != -1) {
            tintManager.setTintColor(setTintColor());
        }
// set a custom navigation bar resource
        if (setNavigationBarTintResource() != -1) {
            tintManager.setNavigationBarTintResource(setNavigationBarTintResource());
        }

// set a custom status bar drawable
        if (setStatusBarTintDrawable() != null) {
            tintManager.setStatusBarTintDrawable(setStatusBarTintDrawable());
        }

    }

    /**
     * 设置沉浸式状态栏颜色
     *
     * @return
     */
    protected int setTintColor() {
        return getResources().getColor(R.color.ActivityTitleBackgroundColor);
    }

    /**
     * 设置导航栏颜色
     *
     * @return
     */
    protected int setNavigationBarTintResource() {
        return -1;
    }

    /**
     * 设置状态栏背景图
     *
     * @return
     */
    protected Drawable setStatusBarTintDrawable() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        myactivityStack.popStack(this);
        super.onDestroy();
        System.gc();
    }


    /**
     * @param mTitleBarView
     */
    protected abstract void initTitleBarData(TitleBarView mTitleBarView);


}
