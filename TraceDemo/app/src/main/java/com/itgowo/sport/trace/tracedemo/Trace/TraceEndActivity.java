package com.itgowo.sport.trace.tracedemo.Trace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.itgowo.sport.trace.tracedemo.Other.BaseActivity;
import com.itgowo.sport.trace.tracedemo.Other.QKManager;
import com.itgowo.sport.trace.tracedemo.Other.TitleBarView;
import com.itgowo.sport.trace.tracedemo.Other.onNetCallbackListener;
import com.itgowo.sport.trace.tracedemo.Other.qktool;
import com.itgowo.sport.trace.tracedemo.R;
import com.itgowo.sport.trace.tracedemo.Trace.Entity.Entity_GetRunRecord;
import com.itgowo.sport.trace.tracedemo.Trace.Entity.LocationInfo;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TraceEndActivity extends BaseActivity {
    public static final String RecordID = "recordid";
    public static final String ListData = "listdata";
    public static final String TYPE = "type";
    public static final String ENDTIME = "endtime";
    public static final String CANSHARE = "canshare";
    public static final String NICKNAME = "NICKNAME";
    /**
     * 距离 米
     */
    public static final String LENGTH = "length";
    /**
     * 运动时间 秒
     */
    public static final String TIME = "time";
    /**
     * 配速秒没千米
     */
    public static final String PEISU = "peisu";
    /**
     * 速度 千米每小时
     */
    public static final String SPEED = "speed";
    /**
     * 海拔  米
     */
    public static final String ALTITUDE = "altitude";
    private long starttime = System.currentTimeMillis();
    /**
     * 分享图片
     */
    private File mSharePic, mSharePic2;
    private MapView mMapView;
    private WalkShareTop mWalkShareTop;
    private RelativeLayout mBottom;
    private LinearLayout statusview;
    private TextView mLength, mTime, mSpeed, mPeisu, mAltitude, mNickname2, tip;
    private List<LatLng> mLatLngs = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initMapline();
            }
        }
    };

    public static void StartActivity(Context mContext, int mRunRecordId, String mNickName, boolean isCanShare) {
        Intent mIntent = new Intent(mContext, TraceEndActivity.class);
        mIntent.putExtra(NICKNAME, mNickName);
        mIntent.putExtra(RecordID, mRunRecordId);
        mIntent.putExtra(TraceEndActivity.CANSHARE, isCanShare);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_end);

        initView();
        mMapView.onCreate(this, savedInstanceState);
        mHandler.sendEmptyMessageDelayed(1, 1000);

    }

    private void initData() {

        mWalkShareTop.sharehead.setImageResource(R.mipmap.ic_launcher);
        double length = getIntent().getDoubleExtra(LENGTH, 0);
        DecimalFormat formatVal = new DecimalFormat("##.##");
        mLength.setText(formatVal.format(length / 1000) + " KM");
        long time = getIntent().getLongExtra(TIME, 0);
        mTime.setText(qktool.time_getTimeHms(new Date(time * 1000 - TimeZone.getDefault().getRawOffset()).getTime()));

        mSpeed.setText(formatVal.format(getIntent().getDoubleExtra(SPEED, 0)) + "");
        mWalkShareTop.sharelength.setText(formatVal.format(getIntent().getDoubleExtra(LENGTH, 0) / 1000) + "\nKM");


        mAltitude.setText(formatVal.format(getIntent().getDoubleExtra(ALTITUDE, 0)) + "");
        int peisu = getIntent().getIntExtra(PEISU, 0);
        mPeisu.setText(peisu / 60 + ":" + (peisu % 60 < 10 ? "0" : "") + peisu % 60);

        long endtime = getIntent().getLongExtra(ENDTIME, 0);
        String mS = qktool.time_getTimeYMDHm(endtime);
        tip.setText(getIntent().getIntExtra(TYPE, 1) == 1 ? "跑步  " + mS : "骑行  " + mS);


        mNickname2.setText(getIntent().getStringExtra(NICKNAME));
        mWalkShareTop.sharename.setText(getIntent().getStringExtra(NICKNAME));
    }

    private void initMapline() {
        if (getIntent().getIntExtra(RecordID, 0) != 0) {
            TraceManager.GetRunRecord(getIntent().getIntExtra(RecordID, 0), new onNetCallbackListener() {
                @Override
                public void onSuccess(final String requestStr, final String result) {
                    Entity_GetRunRecord mRecordId = JSON.parseObject(result, Entity_GetRunRecord.class);
                    if (mRecordId.getCode() == 200) {
                        if (!TextUtils.isEmpty(mRecordId.getData().getRecords())) {
                            toShowMap(mRecordId.getData().getRecords());
                        }
                        getIntent().putExtra(LENGTH, mRecordId.getData().getMileage());
                        getIntent().putExtra(TIME, mRecordId.getData().getFinishTime());
                        getIntent().putExtra(SPEED, mRecordId.getData().getAvgSpeed());
                        getIntent().putExtra(ALTITUDE, mRecordId.getData().getAltitude());
                        getIntent().putExtra(TYPE, mRecordId.getData().getItemType());
                        getIntent().putExtra(ENDTIME, mRecordId.getData().getEndTime());
                        getIntent().putExtra(PEISU, mRecordId.getData().getPace());
                        if (getIntent().getBooleanExtra(CANSHARE, false)) {
                            mTitleBarView.setDrawableRight(R.mipmap.fenxiang, TitleBarView.TitleBarChildView.RIGHT);
                            mTitleBarView.getTitlebarRightbt().setClickable(true);
                        } else {
                            mTitleBarView.getTitlebarRightbt().setClickable(false);
                        }
                        initData();
                    } else {
                        qktool.ToastShout(mRecordId.getMsg());
                    }
                }

                @Override
                public void onError(final Throwable throwable) {
                    throwable.printStackTrace();
                    qktool.ToastShout("网络异常");
                }
            });
        } else {
            String mS = getIntent().getStringExtra(ListData);
            if (TextUtils.isEmpty(mS)) {
                qktool.ToastShout("网络异常，获取数据失败");
                return;
            }
            initData();
            toShowMap(mS);
        }


    }

    private void toShowMap(String result) {
        List<LocationInfo> locationInfoList = JSON.parseArray(result, LocationInfo.class);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        List<LatLng> mLatLngstemp = new ArrayList<>();
        boolean dottedLine = false;
        for (int mI = 0; mI < locationInfoList.size(); mI++) {

            LatLng lat = new LatLng(locationInfoList.get(mI).getLatitude(), locationInfoList.get(mI).getLongitude());
            if (mI == 0) {
                dottedLine = locationInfoList.get(mI).getStatus() == 1 ? false : true;
            }
            //构建用户绘制多边形的Option对象
            if (locationInfoList.get(mI).getStatus() == 1) {
                if (dottedLine) {
                    mLatLngstemp.add(lat);
                    if (mLatLngstemp.size() > 1) {
                        final OverlayOptions polygonOption = new PolylineOptions().points(mLatLngstemp).color(getResources().getColor(R.color.v180_color6)).dottedLine(true).width(15);
                        mMapView.getMap().addOverlay(polygonOption);
                    }
                    dottedLine = false;
                    mLatLngstemp.clear();

                } else {
                    mLatLngstemp.add(lat);
                }
            } else {
                if (!dottedLine) {
                    mLatLngstemp.add(lat);
                    if (mLatLngstemp.size() > 1) {
                        final OverlayOptions polygonOption = new PolylineOptions().points(mLatLngstemp).color(getResources().getColor(R.color.v180_color7)).dottedLine(false).width(15);
                        mMapView.getMap().addOverlay(polygonOption);
                    }
                    dottedLine = true;
                    mLatLngstemp.clear();
                } else {
                    mLatLngstemp.add(lat);
                }
            }

            mLatLngs.add(lat);
            builder = builder.include(lat);
        }

        if (mLatLngstemp.size() > 1) {
            final OverlayOptions polygonOption = new PolylineOptions().points(mLatLngstemp).color(getResources().getColor(dottedLine ? R.color.v180_color6 : R.color.v180_color7)).dottedLine(dottedLine).width(15);
            mMapView.getMap().addOverlay(polygonOption);
        }


        LatLngBounds latlngBounds = builder.build();
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds, mMapView.getWidth(), mMapView.getHeight());
        mMapView.getMap().animateMapStatus(u);
        mMapView.getMap().setMapStatus(u);

// 添加起点,终点坐标
        BitmapDescriptor bitmapStart = BitmapDescriptorFactory.fromResource(R.drawable.tance_icon_start);
        BitmapDescriptor bitmapEnd = BitmapDescriptorFactory.fromResource(R.drawable.tance_icon_end);
        MarkerOptions markerOptionStart = new MarkerOptions().position(mLatLngs.get(0)).icon(bitmapStart);
        mMapView.getMap().addOverlay(markerOptionStart);
        MarkerOptions markerOptionsEnd = new MarkerOptions().position(mLatLngs.get(mLatLngs.size() - 1)).icon(bitmapEnd);
        mMapView.getMap().addOverlay(markerOptionsEnd);

    }


    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.mapview);
        mWalkShareTop = (WalkShareTop) findViewById(R.id.sharetop);
        mBottom = (RelativeLayout) findViewById(R.id.bottom);
        mLength = (TextView) findViewById(R.id.length);
        mTime = (TextView) findViewById(R.id.time);
        mSpeed = (TextView) findViewById(R.id.speed);
        mPeisu = (TextView) findViewById(R.id.peisu);
        mAltitude = (TextView) findViewById(R.id.altitude);
        statusview = (LinearLayout) findViewById(R.id.statusview);
        mNickname2 = (TextView) findViewById(R.id.nickname2);
        tip = (TextView) findViewById(R.id.tip);

    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mMapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initTitleBarData(TitleBarView mTitleBarView) {

        mTitleBarView.setTitle("运动记录");
        mTitleBarView.setDrawableLeft(R.drawable.fanhui, TitleBarView.TitleBarChildView.LEFT);

        mTitleBarView.getLayoutView().setBackgroundColor(getResources().getColor(R.color.v180_color16));
        mTitleBarView.setonTitlebarListener(new TitleBarView.onTitleBarListener() {
            @Override
            public void onTitleBarLeftOnClick(final View mView) {
                finish();
                System.gc();
            }

            @Override
            public void onTitleBarRightOnClick(final View mView) {
                saveBitmap();
            }

            @Override
            public void onTitleBarTitleOnClick(final View mView) {
            }
        });
    }

    private void saveBitmap() {
        QKManager.waitDialogToShow(this);
        mMapView.getMap().snapshot(new BaiduMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap mBitmap) {
                int gap = qktool.ui_dip2px(10);

                //画顶部头像文字和公里数
                Bitmap mBitmapTop = qktool.bitmap_getViewBackground(mWalkShareTop);
                //底部
                Bitmap mBitmapBottom = qktool.bitmap_getViewBackground(mBottom);
                //中间部分绘制
                Bitmap mBitmapTip = qktool.bitmap_getViewBackground(statusview);
                Bitmap centerbottom = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight() + mBitmapBottom.getHeight(), Bitmap.Config.ARGB_4444);
                Canvas mCanvasCenterbottom = new Canvas(centerbottom);
                mCanvasCenterbottom.drawBitmap(mBitmap, 0, 0, null);
                mCanvasCenterbottom.drawBitmap(mBitmapBottom, 0, mBitmap.getHeight(), null);
                mCanvasCenterbottom.drawBitmap(mBitmapTip, statusview.getLeft(), statusview.getTop(), null);
                mBitmapTip.recycle();
                centerbottom = qktool.bitmap_GetRoundedCornerBitmap(qktool.bitmap_ResizeImage(centerbottom, centerbottom.getWidth() - gap * 2, centerbottom.getHeight()), 25);
                Bitmap mBitmapAll = Bitmap.createBitmap(mBitmapTop.getWidth(), mBitmap.getHeight() + mBitmapBottom.getHeight() + gap * 5 + mBitmapTop.getHeight() * 2, Bitmap.Config.ARGB_4444);
                mBitmapBottom.recycle();
                mSharePic2 = new File(QKManager.getLocalFileTemp("jpg"));
                qktool.bitmap_CompressBmpToFile(mBitmap, mSharePic2, Integer.MAX_VALUE);
                mBitmap.recycle();
                Canvas mCanvasAll = new Canvas(mBitmapAll);
                mCanvasAll.drawColor(getResources().getColor(R.color.ActivityBackgroundColorWhite));
                mCanvasAll.drawBitmap(mBitmapTop, 0, 0, null);
                mCanvasAll.drawBitmap(centerbottom, gap, mBitmapTop.getHeight() + gap, null);
                int height3 = centerbottom.getHeight() + mBitmapTop.getHeight() + gap * 2;
//                Bitmap mBitmapQc = BitmapFactory.decodeFile (QRCodeUtil.getQRShare ().getAbsolutePath ());
//                mCanvasAll.drawBitmap (mBitmapQc, mBitmapAll.getWidth () - gap * 2 - mBitmapQc.getWidth (), height3, null);
                mBitmapTop.recycle();
                centerbottom.recycle();
//                mBitmapQc.recycle ();
                Bitmap mBitmaplog = BitmapFactory.decodeResource(getResources(), R.drawable.logo1);
                mCanvasAll.drawBitmap(mBitmaplog, gap * 2,height3+ gap * 2, null);
                mSharePic = new File(QKManager.getLocalFileTemp("jpg"));
                qktool.bitmap_CompressBmpToFile(mBitmapAll, mSharePic, 32000);
                mBitmapAll.recycle();
                System.gc();
                QKManager.waitDialogToClose();
                qktool.ToastShout("分享图片已保存在:" + mSharePic.getAbsolutePath());


            }
        });
    }
}