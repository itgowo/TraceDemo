package com.itgowo.sport.trace.tracedemo.Other;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.itgowo.sport.trace.tracedemo.BuildConfig;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by lujianchao on 2015/10/14.
 *
 * @author lujianchao
 */

public class qktool {
    private static Application mApplication;
    private static String mTag = "QKTool";
    private static boolean debug;

    public static void initTool(Application mApp) {
        if (mApplication == null) {
            mApplication = mApp;
        }
    }


    /**
     * log 开始
     */

    public static void logD(String str) {
        StackTraceElement[] s = new Throwable().getStackTrace();
        if (s.length < 2 || !debug) {
            return;
        }
        Log.d(mTag, s[1].getFileName().substring(0, s[1].getFileName().length() - 4) + s[1].getMethodName() + " " + s[1].getLineNumber() + " : " + str);
    }

    public static void logE(String str) {
        StackTraceElement[] s = new Throwable().getStackTrace();
        if (s.length < 2 || !debug) {
            return;
        }
        Log.e(mTag, s[1].getFileName().substring(0, s[1].getFileName().length() - 4) + s[1].getMethodName() + " " + s[1].getLineNumber() + " : " + str);
    }

    public static void logW(String str) {
        StackTraceElement[] s = new Throwable().getStackTrace();
        if (s.length < 2 || !debug) {
            return;
        }
        Log.w(mTag, s[1].getFileName().substring(0, s[1].getFileName().length() - 4) + s[1].getMethodName() + " " + s[1].getLineNumber() + " : " + str);
    }

    public static void logV(String str) {
        StackTraceElement[] s = new Throwable().getStackTrace();
        if (s.length < 2 || !debug) {
            return;
        }
        Log.v(mTag, s[1].getFileName().substring(0, s[1].getFileName().length() - 4) + s[1].getMethodName() + " " + s[1].getLineNumber() + " : " + str);
    }

    public static void logI(String str) {
        StackTraceElement[] s = new Throwable().getStackTrace();
        if (s.length < 2 || !debug) {
            return;
        }
        Log.i(mTag, s[1].getFileName().substring(0, s[1].getFileName().length() - 4) + s[1].getMethodName() + " " + s[1].getLineNumber() + " : " + str);
    }

    public static void Toastlong(String msg) {
        boolean mB = false;
        if (Looper.myLooper() == null) {
            mB = true;
            Looper.prepare();
        }
        Toast.makeText(mApplication, msg, Toast.LENGTH_LONG).show();
        if (mB) {
            Looper.loop();
        }
    }

    public static void Toastlong(@StringRes int msg) {
        boolean mB = false;
        if (Looper.myLooper() == null) {
            mB = true;
            Looper.prepare();
        }
        Toast.makeText(mApplication, msg, Toast.LENGTH_LONG).show();
        if (mB) {
            Looper.loop();
        }
    }

    public static void ToastShout(String msg) {
        boolean mB = false;
        StackTraceElement[] s = new Throwable().getStackTrace();
        if (Looper.myLooper() == null) {
            mB = true;
            Looper.prepare();
        }
        Toast.makeText(mApplication, msg, Toast.LENGTH_SHORT).show();
        if (mB) {
            Looper.loop();
        }
    }

    public static void ToastShout(@StringRes int msg) {
        boolean mB = false;
        if (Looper.myLooper() == null) {
            mB = true;
            Looper.prepare();
        }
        Toast.makeText(mApplication, msg, Toast.LENGTH_SHORT).show();
        if (mB) {
            Looper.loop();
        }
    }

    public static void ToastShout(@StringRes int msg, int gravity) {
        boolean mB = false;
        if (Looper.myLooper() == null) {
            mB = true;
            Looper.prepare();
        }
        Toast toast = Toast.makeText(mApplication, msg, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
        if (mB) {
            Looper.loop();
        }
    }
    /**
     * log 结束
     */

    /**
     * 获取view截图
     *
     * @param v
     * @return
     */
    public static Bitmap bitmap_getViewBackground(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(),
                    View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                    v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(),
                    (int) v.getX() + v.getMeasuredWidth(),
                    (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return b;
    }
    /**
     * 生成圆角图片
     *
     * @param bitmap
     * @return
     */

    public static Bitmap bitmap_GetRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * 按指定大小缩放图片
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap bitmap_ResizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }


    /**
     * 将bitmap压缩保存到文件中,整型compress表示压缩率，如果填100，表示不压缩，填80，表示压缩20%
     *
     * @param bmp
     * @param file
     * @param mFileMaxSize 最大文件大小,建议300,单位KB
     */
    public static void bitmap_CompressBmpToFile(Bitmap bmp, File file, int mFileMaxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;// 个人喜欢从80开始,此处的80表示压缩率，表示压缩20%，如果不压缩，就是100，表示压缩率是0
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > mFileMaxSize) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 返回格式化时间，43:03:09，两位
     *
     * @param mL
     * @return
     */
    public static String time_getTimeHms(long mL) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mL);
        String created = calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + (calendar.get(Calendar.MINUTE) < 10 ? "0" : "") + calendar.get(Calendar.MINUTE) + ":" + (calendar.get(Calendar.SECOND) < 10 ? "0" : "") + calendar.get(Calendar.SECOND);
        return created;
    }


    /**
     * 获取2017年1月1日 12：00
     *
     * @return
     */
    public static String time_getTimeYMDHm(long mL) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mL);
        String created = calendar.get(Calendar.YEAR) + "年"
                + (calendar.get(Calendar.MONTH) + 1) + "月"//从0计算
                + calendar.get(Calendar.DAY_OF_MONTH) + "日 "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + (calendar.get(Calendar.MINUTE) < 10 ? "0" : "") + calendar.get(Calendar.MINUTE);
        return created;
    }
    public static String time_getTimeYMD(long mL) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mL);
        String created = calendar.get(Calendar.YEAR) + "年"
                + (calendar.get(Calendar.MONTH) + 1) + "月"//从0计算
                + calendar.get(Calendar.DAY_OF_MONTH) + "日 ";
        return created;
    }
    public static long time_getTimeYMDLong(long mL) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mL);
        calendar.set(calendar.get(Calendar.YEAR),Calendar.MONTH,Calendar.DATE);

        return calendar.getTimeInMillis();
    }
    /**
     * UI   开始
     * ---------------------------------------------------------------------------------------------------------
     */
    /**
     * 获取屏幕高,需要先执行initTool，否则会报错
     *
     * @return
     */
    public static int ui_getScreenHeight() {
        WindowManager mWindowManager = (WindowManager) mApplication.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽,需要先执行initTool，否则会报错
     *
     * @return
     */
    public static int ui_getScreenWidth() {
        WindowManager mWindowManager = (WindowManager) mApplication.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    /**
     * 需要先执行initTool，否则会报错
     *
     * @return
     */
    public static int ui_dip2px(int dip) {
        final float scale = mApplication.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 需要先执行initTool，否则会报错
     *
     * @return
     */
    public static int ui_px2dip(int px) {
        final float scale = mApplication.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 需要先执行initTool，否则会报错
     *
     * @return
     */
    public static int ui_sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, mApplication.getResources().getDisplayMetrics());
    }

    /**
     * 需要先执行initTool，否则会报错
     *
     * @return
     */
    public static float ui_px2sp(float pxVal) {
        return (pxVal / mApplication.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     */
    public static String time_getStandardDate(long mTime) {

        StringBuffer sb = new StringBuffer();
        //long time = System.currentTimeMillis() - (t*1000);
        long time = System.currentTimeMillis() - mTime;
        long mill = (long) Math.ceil(time / 1000);//秒前
        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前
        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时
        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前
        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append("刚刚");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    /**
     * 打开app授权页面
     *
     * @param mContext
     */
    public static void app_showAppDetailsSettings(Activity mContext,int requestcode) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", mApplication.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", mApplication.getPackageName());
        }
        mContext.startActivityForResult(localIntent,requestcode);
    }
}
