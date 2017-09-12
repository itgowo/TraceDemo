package com.itgowo.sport.trace.tracedemo.Other;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.itgowo.sport.trace.tracedemo.R;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpResponse;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;


import static org.xutils.x.http;

/**
 * Created by lujianchao on 2016/10/26.
 */

public class QKManager {

    /**
     * 统一请求框架最基础方法，更换第三方可以修改这里
     *
     * @param url              请求网址
     * @param head             请求头
     * @param body             请求体
     * @param callbackListener 请求回掉
     * @return
     */


    public static void http_post(String url, String head, Object body, final onNetCallbackListener callbackListener) {
        String string = "";
        if (body instanceof String) {
            string = (String) body;
        } else {
            string = JSON.toJSONString(body);
        }
        RequestParams requestParams = new RequestParams(url);
        requestParams.addHeader("", head);
        requestParams.setBodyContent(string);
        requestParams.setAsJsonContent(true);
        final String finalString = string;
        http().post(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(final String s) {

                if (callbackListener != null) {
                    callbackListener.onSuccess(finalString, s);
                }
            }

            @Override
            public void onError(final Throwable throwable, final boolean b) {
                if (callbackListener != null) {
                    callbackListener.onError(throwable);
                }
            }

            @Override
            public void onCancelled(final CancelledException e) {
                if (callbackListener != null) {
                    callbackListener.onCancelled();
                }
            }

            @Override
            public void onFinished() {
                if (callbackListener != null) {
                    callbackListener.onFinished();
                }
            }

        });
    }

    /**
     * 统一请求框架最基础方法，更换第三方可以修改这里
     * 同步方法
     *
     * @param url  请求网址
     * @param head 请求头
     * @param body 请求体
     * @return
     */

    public static String http_PostSync(String url, String head, Object body) {
        String string = "";
        if (body instanceof String) {
            string = (String) body;
        } else {
            string = JSON.toJSONString(body);
        }
        RequestParams requestParams = new RequestParams(url);
        requestParams.addHeader("", head);
        requestParams.addBodyParameter("", string);
        requestParams.setAsJsonContent(true);
        try {
            return x.http().postSync(requestParams, String.class);
        } catch (Throwable mThrowable) {
            mThrowable.printStackTrace();
        }
        return null;
    }

    @HttpResponse(parser = jsonnoParser.class)
    public class jsonnoParser implements ResponseParser {

        @Override
        public void checkResponse(final UriRequest mUriRequest) throws Throwable {

        }

        @Override
        public String parse(final Type mType, final Class<?> mClass, final String mS) throws Throwable {

            return mS;
        }
    }

    /**
     * 统一请求框架最基础方法，更换第三方可以修改这里
     *
     * @param url              请求网址
     * @param head             请求头
     * @param body             请求体
     * @param callbackListener 请求回掉
     * @return
     */
    public static void http_get(String url, String head, Object body, final onNetCallbackListener callbackListener) {
        String string = "";
        if (body instanceof String) {
            string = (String) body;
        } else {
            string = JSON.toJSONString(body);
        }
        RequestParams requestParams = new RequestParams(url);
        requestParams.addHeader("", head);
        requestParams.addBodyParameter("", string);
        final String finalString = string;
        http().get(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(final String s) {
                if (callbackListener != null) {
                    callbackListener.onSuccess(finalString, s);
                }
            }

            @Override
            public void onError(final Throwable throwable, final boolean b) {
                if (callbackListener != null) {
                    callbackListener.onError(throwable);
                }

            }

            @Override
            public void onCancelled(final CancelledException e) {
                if (callbackListener != null) {
                    callbackListener.onCancelled();
                }
            }

            @Override
            public void onFinished() {
                if (callbackListener != null) {
                    callbackListener.onFinished();
                }
            }

        });
    }


    /**
     * 加载图片
     *
     * @param uri
     * @param imageView
     */
    public static void image(Context mContext, String uri, ImageView imageView) {
        try {
            Glide.with(mContext).load(uri).dontAnimate().into(imageView);
        } catch (Exception mE) {

        }
    }

    /**
     * 加载图片
     *
     * @param mContext
     * @param uri
     * @param err
     * @param imageView
     */

    public static void image(Context mContext, String uri, @DrawableRes int err, ImageView imageView) {
        try {
            Glide.with(mContext).load(uri).error(err).into(imageView);
        } catch (Exception mE) {

        }

    }

    /**
     * 加载图片
     *
     * @param mContext
     * @param uri
     * @param err
     * @param imageView
     */

    public static void image(Context mContext, String uri, @DrawableRes int placeholder, @DrawableRes int err, ImageView imageView) {
        try {
            Glide.with(mContext).load(uri).error(err).placeholder(placeholder).into(imageView);
        } catch (Exception mE) {

        }

    }

    /**
     * 加载图片
     *
     * @param uri
     * @param imageView
     */
    public static void image(Context mContext, File uri, ImageView imageView) {
        try {
            Glide.with(mContext).load(uri).into(imageView);
        } catch (Exception mE) {

        }

    }

    /**
     * 加载图片
     *
     * @param uri
     * @param imageView
     */
    public static void image(Context mContext, Uri uri, ImageView imageView) {
        try {
            Glide.with(mContext).load(uri).into(imageView);
        } catch (Exception mE) {

        }

    }

    /**
     * 加载图片
     *
     * @param uri
     * @param imageView
     */
    public static void image(Context mContext, byte[] uri, ImageView imageView) {
        try {
            Glide.with(mContext).load(uri).into(imageView);
        } catch (Exception mE) {

        }

    }

    /**
     * 加载图片
     *
     * @param uri
     * @param imageView
     */
    public static void image(Context mContext, int uri, ImageView imageView) {
        try {
            Glide.with(mContext).load(uri).into(imageView);
        } catch (Exception mE) {

        }

    }

    /**
     * Wait   Dialog
     */
    private static AlertDialog mWaitDialog;

    public static void waitDialogToShow(Context mContext) {
        waitDialogToShow(mContext, null, null, true);
    }

    /**
     * 显示dialog
     *
     * @param mContext
     * @param title    标题 可空
     * @param content  内容 可空
     * @param canable  可否取消
     */
    public static void waitDialogToShow(Context mContext, @Nullable String title, @Nullable String content, boolean canable) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext, R.style.WaitDialog);
        if (title != null) {
            mBuilder.setTitle(title);
        }
        mBuilder.setCancelable(canable);
        View mView = View.inflate(mContext, R.layout.waitingdialog, null);
        TextView mTextView = (TextView) mView.findViewById(R.id.content);
//        SpinKitView mSpinKitView= (SpinKitView) mView.findViewById (R.id.skv);
        if (content != null) {
            mTextView.setText(content);
        }
        mBuilder.setView(mView);
        mWaitDialog = mBuilder.show();
    }

    /**
     * 关闭dialog
     */
    public static void waitDialogToClose() {
        if (mWaitDialog == null) {
            return;
        }
        mWaitDialog.dismiss();
    }
    public interface onDialogListener{
        public void onPositive();
        public void onNegative();
    }
    public static void ShowDialog_Normal(Context mContext, String mContent, final onDialogListener mListener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage(mContent).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onPositive();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onNegative();
            }
        }) .show();
    }
    public static String getLocalFileTemp(String ext) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
//            return  app.sApp.getExternalCacheDir() + qktool.time_getTimeYMDHm(System.currentTimeMillis())+ "." + ext;
            File mFile=new File("/sdcard/demo/");
            mFile.mkdirs();
            return  "/sdcard/demo/" + qktool.time_getTimeYMDHm(System.currentTimeMillis())+ "." + ext;
        }

        return  app.sApp.getCacheDir()  + qktool.time_getTimeYMDHm(System.currentTimeMillis())+"." + ext;
    }
}
