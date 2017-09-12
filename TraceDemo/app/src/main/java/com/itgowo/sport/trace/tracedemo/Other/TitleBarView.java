package com.itgowo.sport.trace.tracedemo.Other;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itgowo.sport.trace.tracedemo.R;


/**
 * Created by lujianchao on 2016/11/16.
 *
 * @author lujianchao
 *         <p>
 *         titlebar，不解释
 */

public class TitleBarView extends LinearLayout {
    public static final String Tag = "TitleBarView";
    private RelativeLayout layout;
    private TextView titlebarLeftbt;
    private TextView titlebarTitle;
    private TextView titlebarRightbt;
    private TextView titlebar_rightotherbt;
    private OnClickListener mOnClickListener;
    private onTitleBarListener mOnTitleBarListener;


    public TitleBarView(final Context context, final AttributeSet attrs) {
        super (context, attrs);
        inflate (context, R.layout.titlebar_viewlayout, this);
        this.setTag (Tag);
        initView ();
        initListener ();
    }


    /**
     * 获取Titlebar左边textview
     *
     * @return TextView
     */
    public TextView getTitlebarLeftbt () {
        return titlebarLeftbt;
    }

    /**
     * 获取Titlebar左边textview
     *
     * @return TextView
     */
    public TextView getTitlebarTitle () {
        return titlebarTitle;
    }


    /**
     * 设置Title
     *
     * @param mTitle
     */
    public void setTitle (String mTitle) {
        titlebarTitle.setText (mTitle);
    }

    public void setMsgRight (String mMsgRight) {
        titlebarRightbt.getLayoutParams ().width = LayoutParams.WRAP_CONTENT;
        titlebarRightbt.setText (mMsgRight);
    }

    public void setMsgLeft (String mMsgLeft) {
        titlebarLeftbt.getLayoutParams ().width = LayoutParams.WRAP_CONTENT;
        titlebarLeftbt.setText (mMsgLeft);
    }

    /**
     * 获取Titlebar左边textview
     *
     * @return TextView
     */
    public TextView getTitlebarRightbt () {
        return titlebarRightbt;
    }

    /**
     * 获取Titlebar左边textview
     *
     * @return TextView
     */
    public TextView getTitlebarRightOtherbt () {
        return titlebar_rightotherbt;
    }

    private void initListener () {
        mOnClickListener = new OnClickListener () {
            @Override
            public void onClick (final View mView) {
                if (mOnTitleBarListener != null) {
                    switch (mView.getId ()) {
                        case R.id.titlebar_leftbt:
                            mOnTitleBarListener.onTitleBarLeftOnClick (mView);
                            break;
                        case R.id.titlebar_rightbt:
                            mOnTitleBarListener.onTitleBarRightOnClick (mView);
                            break;
                        case R.id.titlebar_title:
                            mOnTitleBarListener.onTitleBarTitleOnClick (mView);
                            break;
                    }
                }
            }
        };
        titlebarTitle.setOnClickListener (mOnClickListener);
        titlebarRightbt.setOnClickListener (mOnClickListener);
        titlebarLeftbt.setOnClickListener (mOnClickListener);
    }

    private void initView () {
        layout = (RelativeLayout) findViewById (R.id.layout);
        titlebarLeftbt = (TextView) findViewById (R.id.titlebar_leftbt);
        titlebarTitle = (TextView) findViewById (R.id.titlebar_title);
        titlebarRightbt = (TextView) findViewById (R.id.titlebar_rightbt);
        titlebar_rightotherbt = (TextView) findViewById (R.id.titlebar_rightotherbt);
    }

    public RelativeLayout getLayoutView () {
        return layout;
    }

    /**
     * 设置TitleBar监听
     */
    public void setonTitlebarListener (onTitleBarListener mTitlebarListener) {
        mOnTitleBarListener = mTitlebarListener;
    }

    public enum TitleBarChildView {
        LEFT, RIGHT, CENTER, RIGHTOTHER;
    }

    /**
     * 设置TitleBar中三个textview的图片用drawableleif形式
     *
     * @param res   资源ID
     * @param index 指定被操作的textview
     */
    public void setDrawableLeft (int res, TitleBarChildView index) {
        Drawable mDrawable = getResources ().getDrawable (res);
        mDrawable.setBounds (0, 0, mDrawable.getMinimumWidth (), mDrawable.getMinimumHeight ());
        if (index == TitleBarChildView.LEFT) {
            titlebarLeftbt.setCompoundDrawables (mDrawable, null, null, null);
        } else if (index == TitleBarChildView.RIGHT) {
            titlebarRightbt.setCompoundDrawables (mDrawable, null, null, null);
        } else if (index == TitleBarChildView.RIGHTOTHER) {
            titlebar_rightotherbt.setCompoundDrawables (mDrawable, null, null, null);
        } else {
            titlebarTitle.setCompoundDrawables (mDrawable, null, null, null);
        }
    }

    /**
     * 设置TitleBar中三个textview的图片用drawableright形式
     *
     * @param res   资源ID
     * @param index 指定被操作的textview
     */
    public void setDrawableRight (@Nullable int res, TitleBarChildView index) {
        Drawable mDrawable = getResources ().getDrawable (res);
        mDrawable.setBounds (0, 0, mDrawable.getMinimumWidth (), mDrawable.getMinimumHeight ());
        if (index == TitleBarChildView.LEFT) {
            titlebarLeftbt.setCompoundDrawables (null, null, mDrawable, null);
        } else if (index == TitleBarChildView.RIGHT) {
            titlebarRightbt.setCompoundDrawables (null, null, mDrawable, null);
        } else if (index == TitleBarChildView.RIGHTOTHER) {
            titlebar_rightotherbt.setCompoundDrawables (null, null, mDrawable, null);
        } else {
            titlebarTitle.setCompoundDrawables (null, null, mDrawable, null);
        }
    }

    public interface onTitleBarListener {
        public void onTitleBarLeftOnClick(View mView);

        public void onTitleBarRightOnClick(View mView);

        public void onTitleBarTitleOnClick(View mView);
    }

}
