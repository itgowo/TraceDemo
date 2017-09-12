package com.itgowo.sport.trace.tracedemo.Trace;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.itgowo.sport.trace.tracedemo.BuildConfig;
import com.itgowo.sport.trace.tracedemo.Other.BaseActivity;
import com.itgowo.sport.trace.tracedemo.Other.DragButton.SlidingButtonLayout;
import com.itgowo.sport.trace.tracedemo.Other.QKManager;
import com.itgowo.sport.trace.tracedemo.Other.TitleBarView;
import com.itgowo.sport.trace.tracedemo.Other.app;
import com.itgowo.sport.trace.tracedemo.Other.circleprogress.DonutProgress;
import com.itgowo.sport.trace.tracedemo.Other.onNetCallbackListener;
import com.itgowo.sport.trace.tracedemo.Other.qktool;
import com.itgowo.sport.trace.tracedemo.R;
import com.itgowo.sport.trace.tracedemo.Trace.Entity.RequestTrace;
import com.itgowo.sport.trace.tracedemo.Trace.Entity.Entity_TotalInfo;
import com.itgowo.sport.trace.tracedemo.Trace.Entity.Entity_UploadResponse;
import com.itgowo.sport.trace.tracedemo.Trace.Entity.LocationInfo;
import com.itgowo.sport.trace.tracedemo.Trace.Entity.EntityRecord;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.itgowo.sport.trace.tracedemo.Trace.TraceManager.ROOT_URL;

@RuntimePermissions
public class TraceMainActivity extends BaseActivity {
    public static final String SportItemType = "sportItemType";
    public static final int ACTION_LOCATION_SOURCE_SETTINGS = 8001;//app定位权限
    public static final int APPLICATION_DETAILS_SETTINGS = 8002;//app授权页面
    public static final int TraceMinLengthCanUpload = 1;//运动数据达到这个值之后才可以上传，单位米

    /**
     * 1：跑步  2：骑行
     */
    private int mSportType = 1;
    private Entity_TotalInfo.TotalInfo mPage_Run, mPage_Bike;
    private List<LocationInfo> mLocationInfos = new ArrayList<>();//轨迹信息
    private List<LatLng> points = new ArrayList<>();//坐标点

    private long mStartDateTime, mEndDateTime;//运动开始结束时间
    private double mAltitude;//海拔
    private boolean enableGps;//如果没有定位权限，则不能开始跑步
    private boolean isPause = false;//是不是暂停
    private boolean isStartSport = false;//是否开始运动
    private boolean isCancel_Lock = false;
    private boolean isStop_EndBT = false;//记录是不是结束按钮结束的运动
    private long mSportTime;//毫秒
    private long mAniTime = 300;//毫秒
    private double mSportAllDistance;//总里程 米
    private int mBottomStartedheight;//运动开始后下面布局高度
    private int mRepeatCountNum = 2;//开始运动动画倒计时数字

    private MapView mMapView;
    private LocationClient mLocationClient;
    private MyLocationListenner mMyLocationListenner = new MyLocationListenner();
    private DecimalFormat df = new DecimalFormat("######0.00");

    private TextView mGpsSignalTip, mGpsMsgTip;//左上角Gps信息状态展示
    private ImageView mSportTypeRun, mSportTypeBike;//选择运动类型按钮
    private RelativeLayout mBottomLayout, mBottomLayoutStarted;//运动开始前和开始后下部显示布局
    private TextView mTotalMileageTv, mStartSportTV, mAllTimeTv;//未开始情况下的控制显示按钮，三个圆形按钮
    private LinearLayout mBottomLayoutToplayout, mFloatlayout;
    private RelativeLayout mLockLayout;//锁屏遮罩布局
    private SlidingButtonLayout mLockButton;//滑动解锁按钮
    private FrameLayout mCountLayout;//开始倒计时遮罩布局
    private TextView mCountTimer;//开始倒计时遮罩布局中显示数字的

    private TextView mBottomBt_Pause, mBottomBt_End;//运动开始后三个控制按钮
    private DonutProgress mBottomBt_Lock;
    private TextView mBottomTV_Distance, mBottomTv_Speed, mBottomTv_AverageSpeed, mBottomTv_Peisu, mBottomTv_Altitude;//运动开始后显示textview
    private Chronometer mBottomTv_Time;//运动开始后显示textview
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            if (msg.what == 1) {
                if (isCancel_Lock) {
                    mBottomBt_Lock.setProgress(0);
                } else {
                    if (mBottomBt_Lock.getMax() == mBottomBt_Lock.getProgress()) {
                        mLockLayout.setVisibility(View.VISIBLE);
                        mBottomBt_Lock.setVisibility(View.INVISIBLE);
                        mBottomBt_Pause.setVisibility(View.INVISIBLE);
                        mBottomBt_End.setVisibility(View.INVISIBLE);
                        mTitleBarView.setClickable(false);
                        isCancel_Lock = true;
                    } else {
                        mBottomBt_Lock.setProgress(mBottomBt_Lock.getProgress() + 1);
                        mHandler.sendEmptyMessageDelayed(1, 50);
                    }
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_main);
        initView();
        mMapView.onCreate(this, savedInstanceState);
        initListener();
        checkGps();
        TraceMainActivityPermissionsDispatcher.checkLoctionPermissionWithCheck(this);
        RefreshInfo();
        initMapConfig();

    }

    private void initListener() {
        mSportTypeRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSportTypeRun.setImageResource(R.drawable.trance_run);
                mSportTypeBike.setImageResource(R.drawable.trance_bike2);
                mSportType = 1;
                initData(mSportType);
            }
        });
        mSportTypeBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSportTypeRun.setImageResource(R.drawable.trance_run2);
                mSportTypeBike.setImageResource(R.drawable.trance_bike);
                mSportType = 2;
                initData(mSportType);
            }
        });
        mStartSportTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimateStartSport();
            }
        });


        mBottomBt_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomBt_End.setClickable(false);
                mBottomBt_Lock.setClickable(false);
                mBottomBt_Pause.setClickable(false);
                isPause = !isPause;
                mBottomBt_Pause.setText(isPause ? "继续" : "暂停");
                if (isPause) {
                    mBottomTv_Time.stop();
                    StartAnimateX(0, mBottomBt_Pause.getLeft() - mBottomBt_Lock.getLeft());
                } else {
                    mBottomTv_Time.setBase(SystemClock.elapsedRealtime() - mSportTime);
                    mBottomTv_Time.start();
                    StartAnimateX(mBottomBt_Pause.getLeft() - mBottomBt_Lock.getLeft(), 0);
                }
            }
        });
        mBottomTv_Time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                mSportTime = SystemClock.elapsedRealtime() - chronometer.getBase();
            }
        });

        mBottomBt_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStop_EndBT = true;
                onStopSport();
            }
        });
        mBottomLayoutToplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFloatlayout.getLayoutParams().height != 0) {
                    ((RelativeLayout.LayoutParams) mMapView.getLayoutParams()).bottomMargin = mBottomLayoutToplayout.getHeight();
                    StartAnimateY(mBottomStartedheight - qktool.ui_dip2px(80), 0);
                } else {
                    mMapView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((RelativeLayout.LayoutParams) mMapView.getLayoutParams()).bottomMargin = mBottomStartedheight;
                        }
                    }, mAniTime);
                    StartAnimateY(0, mBottomStartedheight - qktool.ui_dip2px(80));
                }

            }
        });

        mBottomBt_Lock.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    isCancel_Lock = true;
                    mHandler.sendEmptyMessageDelayed(1, 50);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isCancel_Lock = false;
                    mHandler.sendEmptyMessageDelayed(1, 50);
                }
                return true;
            }
        });
        mLockButton.setOnFinshDragListener(new SlidingButtonLayout.OnFinshDragListener() {
            @Override
            public void onFinshDragDone() {
                mLockLayout.setVisibility(View.GONE);
                mBottomBt_Lock.setVisibility(View.VISIBLE);
                mBottomBt_Pause.setVisibility(View.VISIBLE);
                mBottomBt_End.setVisibility(View.VISIBLE);
                mTitleBarView.setClickable(true);
            }
        });
    }

    /**
     * 开始运动后信息布局移动动画
     *
     * @param y1
     * @param y2
     */
    private void StartAnimateY(int y1, int y2) {
        ValueAnimator mAnimator = ValueAnimator.ofInt(y1, y2);

        mBottomTv_Time.stop();
        mBottomLayoutStarted.setClickable(false);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFloatlayout.getLayoutParams().height = (int) animation.getAnimatedValue();
                mFloatlayout.requestLayout();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mBottomLayoutStarted.setClickable(true);
                if (!isPause) {
                    mBottomTv_Time.start();
                }
            }
        });
        mAnimator.setDuration(mAniTime).start();
    }

    /**
     * 过场动画
     */
    private void startAnimateStartSport() {
        mCountLayout.setVisibility(View.VISIBLE);
        ScaleAnimation scaleAnimation = new ScaleAnimation(3f, 0.5f, 3f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setStartTime(100);
        scaleAnimation.setRepeatCount(2);
        mCountTimer.startAnimation(scaleAnimation);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCountLayout.setVisibility(View.GONE);
                mRepeatCountNum = 2;
                mCountTimer.setText("3");
                startSport();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                mCountTimer.setText(mRepeatCountNum + "");
                mRepeatCountNum--;
            }
        });

    }

    /**
     * 开始运动后，暂停按钮触发的锁屏按钮的收起恢复动画
     *
     * @param x1
     * @param x2
     */
    private void StartAnimateX(int x1, int x2) {
        ValueAnimator mAnimator = ValueAnimator.ofInt(x1, x2);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBottomBt_Lock.setTranslationX((int) animation.getAnimatedValue());
                mBottomBt_Lock.requestLayout();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mBottomBt_End.setClickable(true);
                mBottomBt_Lock.setClickable(true);
                mBottomBt_Pause.setClickable(true);
            }
        });
        mAnimator.setDuration(mAniTime).start();
    }

    /**
     * 开始运动
     */

    private void startSport() {
        checkGps();
        TraceMainActivityPermissionsDispatcher.checkLoctionPermissionWithCheck(this);
        if (!enableGps) {
            return;
        }
        isStartSport = true;
        mStartDateTime = System.currentTimeMillis();
        mLocationInfos.clear();
        points.clear();
        isStartSport = true;
        mBottomTv_Time.setBase(SystemClock.elapsedRealtime());
        mBottomTv_Time.start();
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        if (mSportType == 1) {
            mTitleBarView.setTitle("跑步中");
        } else {
            mTitleBarView.setTitle("骑行中");
        }
        mTitleBarView.getTitlebarRightbt().setVisibility(View.GONE);
        mTitleBarView.getTitlebarRightOtherbt().setVisibility(View.GONE);
        mBottomLayoutStarted.setVisibility(View.VISIBLE);
        mBottomLayout.setVisibility(View.GONE);
        if (mBottomStartedheight == 0) {
            mBottomStartedheight = mBottomLayoutStarted.getBottom() - mBottomLayoutStarted.getTop();
        }
        ((RelativeLayout.LayoutParams) mMapView.getLayoutParams()).bottomMargin = mBottomStartedheight;

    }


    @Override
    protected void initTitleBarData(final TitleBarView mTitleBarView) {
        mTitleBarView.setTitle("开始运动");
        mTitleBarView.setDrawableLeft(R.drawable.fanhui, TitleBarView.TitleBarChildView.LEFT);
        mTitleBarView.setDrawableRight(R.drawable.ranking, TitleBarView.TitleBarChildView.RIGHT);
        mTitleBarView.setDrawableRight(R.drawable.list, TitleBarView.TitleBarChildView.RIGHTOTHER);
        mTitleBarView.getTitlebarRightOtherbt().setVisibility(View.VISIBLE);
        mTitleBarView.getTitlebarRightOtherbt().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                TraceRecordActivity.StartActivity(mContext, false, mSportType);
            }
        });
        mTitleBarView.setonTitlebarListener(new TitleBarView.onTitleBarListener() {
            @Override
            public void onTitleBarLeftOnClick(final View mView) {
                onBackPressed();
            }

            @Override
            public void onTitleBarRightOnClick(final View mView) {
                qktool.ToastShout("运动排行啥的，精简掉了O(∩_∩)O哈！");
            }

            @Override
            public void onTitleBarTitleOnClick(final View mView) {

            }
        });
    }

    private void upLoadData() {
        mEndDateTime = System.currentTimeMillis();
        QKManager.waitDialogToShow(this);
        final EntityRecord request = new EntityRecord().setItemType(mSportType).setMileage(mSportAllDistance).setStartTime(mStartDateTime)
                .setEndTime(mEndDateTime).setFinishTime(mSportTime / 1000).setAvgSpeed(3600 * mSportAllDistance / mSportTime).setAltitude(mAltitude)
                .setPace((int) Math.abs(mSportTime / mSportAllDistance)).setRecords(JSON.toJSONString(mLocationInfos));
        RequestTrace mEntity_requestTrace=new RequestTrace().setRequestType(RequestTrace.RequestUpload).setEntity(request);
        QKManager.http_post(ROOT_URL, "", mEntity_requestTrace, new onNetCallbackListener() {
            @Override
            public void onSuccess(String requestStr, String result) {
                Entity_UploadResponse response = JSON.parseObject(result, Entity_UploadResponse.class);
                if (response.getCode() == 200) {
                    qktool.ToastShout("提交成功");
                    if (isStop_EndBT) {
                        TraceEndActivity.StartActivity(mContext,  response.getData() .getId() , app.sNickName, true);
                        finish();
                    } else {
                        if (mSportAllDistance > TraceMinLengthCanUpload) {
                            finish();
                        } else {
                            reinitTvInfo();
                        }
                    }
                } else {
                    qktool.ToastShout(response.getMsg());
                    onUpdataErr();
                }
                QKManager.waitDialogToClose();
            }

            @Override
            public void onError(Throwable throwable) {
                QKManager.waitDialogToClose();
                onUpdataErr();
                throwable.printStackTrace();
            }
        });
    }

    private void onUpdataErr() {
        QKManager.ShowDialog_Normal(mContext, "上传失败了,是否放弃本次记录?", new QKManager.onDialogListener() {
            @Override
            public void onPositive() {
                reinitTvInfo();
            }

            @Override
            public void onNegative() {

            }
        });
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void checkLoctionPermission() {
        enableGps = true;
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void ShowPermissionTipForLocation(PermissionRequest request) {

    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void OnPermissionDenied() {
        enableGps = false;
        QKManager.ShowDialog_Normal(this, "轨迹功能需要您授权访问手机位置信息", new QKManager.onDialogListener() {
            @Override
            public void onPositive() {
                qktool.app_showAppDetailsSettings(mContext, APPLICATION_DETAILS_SETTINGS);
            }

            @Override
            public void onNegative() {

            }
        });
        }

    private void initMapConfig() {
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.map_icon_end);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker);
        mMapView.getMap().setMyLocationConfigeration(config);


        // 开启定位图层
        mMapView.getMap().setMyLocationEnabled(true);
        mMapView.getMap().setBuildingsEnabled(true);
        mMapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(18).build()));
        // 定位初始化
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(mMyLocationListenner);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
//        option.setScanSpan(2000);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(false);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(false);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(BuildConfig.DEBUG);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setOpenAutoNotifyMode(0, 3, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        /**
         * public void setOpenAutoNotifyMode(int minTimeInterval,
         int minDistance,
         int locSensitivity)
         设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
         参数:
         minTimeInterval - 最短定位时间间隔，单位毫秒，最小值0，开发者可以在设置希望的位置回调最短时间间隔
         minDistance - 最短定位距离间隔，单位米，最小值0，开发者可以设置希望的位置回调距离间隔
         locSensitivity - 定位变化敏感程度,LOC_SENSITIVITY_HIGHT,LOC_SENSITIVITY_MIDDLE,LOC_SENSITIVITY_LOW
         */
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkGps();
    }

    private void checkGps() {
        if (!((LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            QKManager.ShowDialog_Normal(this, "为了正常记录你的运动数据,派队需要你开启GPS定位功能", new QKManager.onDialogListener() {
                @Override
                public void onPositive() {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, ACTION_LOCATION_SOURCE_SETTINGS);
                }

                @Override
                public void onNegative() {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        mMapView.getMap().clear();
        mMapView.getMap().setMyLocationEnabled(false);
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(mMyLocationListenner);
        }
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

    private void RefreshInfo() {
        if (mSportType == 1) {
            if (mPage_Run == null) {
                initData(1);
            } else {
                mTotalMileageTv.setText(df.format(mPage_Run.getTotalmileage() / 1000) + "KM\n总里程");
                mAllTimeTv.setText(qktool.time_getTimeHms(new Date(mPage_Run.getTotalTime() * 1000 - TimeZone.getDefault().getRawOffset()).getTime()) + "\n总时间");
            }
            //// TODO: 2017/2/10
        } else if (mSportType == 2) {
            if (mPage_Bike == null) {
                initData(2);
            } else {
                mTotalMileageTv.setText(df.format(mPage_Bike.getTotalmileage() / 1000) + "KM\n总里程");
                mAllTimeTv.setText(qktool.time_getTimeHms(new Date(mPage_Bike.getTotalTime() * 1000 - TimeZone.getDefault().getRawOffset()).getTime()) + "\n总时间");
            }
            //// TODO: 2017/2/10
        }
    }

    private void initData(final int itemtype) {
        TraceManager.GetRunRecordIndexPage(itemtype, new onNetCallbackListener() {
            @Override
            public void onSuccess(final String requestStr, final String result) {
                Entity_TotalInfo mPage = JSON.parseObject(result, Entity_TotalInfo.class);
                if (mPage.getCode() == 200 && mPage.getData() != null) {
                    if (itemtype == 1) {
                        mPage_Run =  mPage.getData();
                    } else {
                        mPage_Bike =  mPage.getData();
                    }
                    RefreshInfo();
                } else {
                    qktool.ToastShout(mPage.getMsg());
                }
            }

            @Override
            public void onError(final Throwable throwable) {
                super.onError(throwable);
                qktool.ToastShout("总里程总时间等信息获取失败，如有需要请重试");
            }
        });
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.showZoomControls(false);
        RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mMapView.getLayoutParams();
        mLayoutParams.bottomMargin = qktool.ui_dip2px(200);
        mMapView.setLayoutParams(mLayoutParams);
        mGpsSignalTip = (TextView) findViewById(R.id.gpsImg);
        mGpsMsgTip = (TextView) findViewById(R.id.gpsTip);

        //运动开始前布局
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        mFloatlayout = (LinearLayout) findViewById(R.id.floatlayout);
        mSportTypeRun = (ImageView) findViewById(R.id.SportTypeRun);
        mSportTypeBike = (ImageView) findViewById(R.id.SportTypeBike);
        mTotalMileageTv = (TextView) findViewById(R.id.TotalMileageTv);
        mStartSportTV = (TextView) findViewById(R.id.StartSportTV);
        mAllTimeTv = (TextView) findViewById(R.id.AllTimeTv);


        mCountLayout = (FrameLayout) findViewById(R.id.CountLayout);
        mCountTimer = (TextView) findViewById(R.id.CountTimer);

        //运动开始后布局
        mBottomLayoutStarted = (RelativeLayout) findViewById(R.id.bottomLayoutStarted);
        mBottomTV_Distance = (TextView) findViewById(R.id.bottomTV_Distance);
        mBottomTv_Speed = (TextView) findViewById(R.id.bottomTv_Speed);
        mBottomTv_AverageSpeed = (TextView) findViewById(R.id.bottomTv_AverageSpeed);
        mBottomTv_Peisu = (TextView) findViewById(R.id.bottomTv_Peisu);
        mBottomTv_Altitude = (TextView) findViewById(R.id.bottomTv_Altitude);
        mBottomTv_Time = (Chronometer) findViewById(R.id.bottomTv_Time);
        mBottomBt_Pause = (TextView) findViewById(R.id.bottomBt_Pause);
        mBottomBt_End = (TextView) findViewById(R.id.bottomBt_End);
        mBottomBt_Lock = (DonutProgress) findViewById(R.id.bottomBt_Lock);
        mBottomLayoutToplayout = (LinearLayout) findViewById(R.id.bottomLayoutToplayout);


        mLockLayout = (RelativeLayout) findViewById(R.id.locklayout);
        mLockButton = (SlidingButtonLayout) findViewById(R.id.lockBT);

    }

    public static void StartActivity(Context mContext) {
        Intent intent = new Intent(mContext, TraceMainActivity.class);
        mContext.startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TraceMainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation location) {
            if (location.getLocType() != 61 && location.getLocType() != 161) {
                return;
            }

            if (location == null || mMapView == null) {
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshGpsSignalAndTips(location.getGpsAccuracyStatus());
                }
            });
            MyLocationData locData = new MyLocationData.Builder().accuracy(0).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mMapView.getMap().setMyLocationData(locData);
            if (!isStartSport) {
                return;
            }
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            points.add(point);

            if (points.size() < 2) {
                return;
            }
            OverlayOptions options = null;
            if (isPause) {
                // 画虚线
                if (points.size() > 2) {
                    List<LatLng> latLngs = points.subList(points.size() - 2, points.size());
                    options = new PolylineOptions().color(getResources().getColor(R.color.v180_color6)).width(10).dottedLine(true).points(latLngs);
                    mMapView.getMap().addOverlay(options);
                }
                mLocationInfos.add(new LocationInfo().setLatitude(location.getLatitude()).setLongitude(location.getLongitude()).setStatus(2));
            } else {
                if (points.size() > 2) {
                    List<LatLng> latLngs = points.subList(points.size() - 2, points.size());
                    options = new PolylineOptions().color(getResources().getColor(R.color.v180_color7)).width(10).points(latLngs);

                }
                mLocationInfos.add(new LocationInfo().setLatitude(location.getLatitude()).setLongitude(location.getLongitude()).setStatus(1));
            }
            mMapView.getMap().addOverlay(options);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshTvInfo(location);
                }
            });

        }

        @Override
        public void onConnectHotSpotMessage(String mS, int mI) {

        }
    }

    /**
     * 刷新gps信号状态
     *
     * @param gpsAccuracyStatus
     */
    private void refreshGpsSignalAndTips(int gpsAccuracyStatus) {
        switch (gpsAccuracyStatus) {
            case 0://无法获知gps状态
                mGpsMsgTip.setText("建议到空旷地带");
                Drawable mDrawable = getResources().getDrawable((R.drawable.gps_signal_0));
                mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
                mGpsSignalTip.setCompoundDrawables(null, null, mDrawable, null);
                break;
            case 1://信号好
                mGpsMsgTip.setText("信号强");
                Drawable mDrawable1 = getResources().getDrawable((R.drawable.gps_signal_3));
                mDrawable1.setBounds(0, 0, mDrawable1.getMinimumWidth(), mDrawable1.getMinimumHeight());
                mGpsSignalTip.setCompoundDrawables(null, null, mDrawable1, null);
                break;
            case 2://信号中
                mGpsMsgTip.setText("信号中");
                Drawable mDrawable2 = getResources().getDrawable((R.drawable.gps_signal_2));
                mDrawable2.setBounds(0, 0, mDrawable2.getMinimumWidth(), mDrawable2.getMinimumHeight());
                mGpsSignalTip.setCompoundDrawables(null, null, mDrawable2, null);
                break;
            case 3://信号差
                mGpsMsgTip.setText("信号弱");
                Drawable mDrawable3 = getResources().getDrawable((R.drawable.gps_signal_1));
                mDrawable3.setBounds(0, 0, mDrawable3.getMinimumWidth(), mDrawable3.getMinimumHeight());
                mGpsSignalTip.setCompoundDrawables(null, null, mDrawable3, null);
                break;
        }
    }

    /**
     * 界面记录信息清零
     */
    private void reinitTvInfo() {
        mBottomTv_Peisu.setText("00:00");
        mBottomTv_Altitude.setText("0.00");//海拔
        mBottomTv_Speed.setText("0.00");//即时速度
        mBottomTV_Distance.setText("0.00KM");
        mBottomTv_AverageSpeed.setText("0.00"); // 平均速度
        mBottomTv_Time.stop();
        mSportAllDistance = 0;
        mSportTime = 0;

        mBottomLayout.setVisibility(View.VISIBLE);
        mBottomLayoutStarted.setVisibility(View.GONE);
        isPause = false;
        isStartSport = false;

        mMapView.getMap().clear();
        mLocationClient.stop();

        mTitleBarView.setTitle("开始运动");
        mBottomBt_Pause.setText("暂停");
        mBottomBt_Lock.setTranslationX(0);
        mBottomBt_Lock.requestLayout();

        mTitleBarView.getTitlebarRightbt().setVisibility(View.VISIBLE);
        mTitleBarView.getTitlebarRightOtherbt().setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mMapView.getLayoutParams();
        mLayoutParams.bottomMargin = qktool.ui_dip2px(200);
        mMapView.setLayoutParams(mLayoutParams);
    }

    /**
     * 刷新状态
     *
     * @param mLocation
     */
    private void refreshTvInfo(BDLocation mLocation) {
        mAltitude = mLocation.getAltitude();
        mBottomTv_Altitude.setText(df.format(mLocation.getAltitude()));//海拔
        if (mLocation.hasSpeed()) {
            mBottomTv_Speed.setText(df.format(Math.abs(mLocation.getSpeed())));//即时速度
        }
        mSportAllDistance += DistanceUtil.getDistance(points.get(points.size() - 2), points.get(points.size() - 1));//计算总路程
        mBottomTV_Distance.setText((df.format((mSportAllDistance) / 1000)) + "KM");
        mBottomTv_AverageSpeed.setText(df.format(3600 * mSportAllDistance / mSportTime)); // 平均速度
        // 求配速
        if (mLocation.hasSpeed() && mLocation.getSpeed() != 0) {
            double pace = (3600 * 1 / Math.abs(mLocation.getSpeed()));
            int remainder = (int) (pace % 60);
            mBottomTv_Peisu.setText((int) (pace / 60) + ":" + (remainder > 9 ? remainder : ("0" + remainder)));
        } else {
            mBottomTv_Peisu.setText("00:00");
        }
    }

    private void onStopSport() {
        QKManager.ShowDialog_Normal(mContext, mSportAllDistance > TraceMinLengthCanUpload ? "确定结束本次跑步吗？" : "此次距离太短不会上传记录，是否结束？", new QKManager.onDialogListener() {
            @Override
            public void onPositive() {
                if (mSportAllDistance > TraceMinLengthCanUpload) {
                    upLoadData();
                } else {
                    reinitTvInfo();
                }
            }

            @Override
            public void onNegative() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isStartSport) {
            if (mLockLayout.getVisibility()==View.VISIBLE){
                return;
            }
            isStop_EndBT = false;
            onStopSport();
        } else {
            finish();
        }
    }
}
