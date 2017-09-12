package com.itgowo.sport.trace.tracedemo.Trace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.itgowo.sport.trace.tracedemo.Other.BaseActivity;
import com.itgowo.sport.trace.tracedemo.Other.QKManager;
import com.itgowo.sport.trace.tracedemo.Other.Response;
import com.itgowo.sport.trace.tracedemo.Other.TitleBarView;
import com.itgowo.sport.trace.tracedemo.Other.XListView.XListView;
import com.itgowo.sport.trace.tracedemo.Other.app;
import com.itgowo.sport.trace.tracedemo.Other.onNetCallbackListener;
import com.itgowo.sport.trace.tracedemo.Other.qktool;
import com.itgowo.sport.trace.tracedemo.R;
import com.itgowo.sport.trace.tracedemo.Trace.Entity.EntityRecord;
import com.itgowo.sport.trace.tracedemo.Trace.Entity.Entity_GetRunRecordList;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.TimeZone;


public class TraceRecordActivity extends BaseActivity {
    /**
     * 从开始运动界面跳过来不允许再跳回，右上角图标消失隐藏
     */
    public static final String IsCanJump = "IsCanJump";
    public static final String SportItemType = "sportItemType";
    /**
     * 1：跑步  2：骑行
     */
    private int mSportType = 1;
    private XListView mXListView;
    private Entity_GetRunRecordList mRecordList;
    private RecordAdapter mRecordAdapter;
    private int pageindex = 0, pagesize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_record);
        mSportType = getIntent().getIntExtra(SportItemType, 1);
        mXListView = (XListView) findViewById(R.id.listView);
        mRecordAdapter = new RecordAdapter();
        mXListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }

            @Override
            public void onLoadMore() {
                loadData(false);

            }
        });
        mXListView.setAdapter(mRecordAdapter);

        loadData(true);
    }

    public static void StartActivity(Context mContext, boolean mIsCanJump, int mSportType) {
        Intent intent = new Intent(mContext, TraceRecordActivity.class);
        intent.putExtra(SportItemType, mSportType);
        intent.putExtra(IsCanJump, mIsCanJump);
        mContext.startActivity(intent);
    }

    private void loadData(final boolean isRefresh) {
        mXListView.setPullLoadEnable(false);
        mXListView.setPullRefreshEnable(false);
        if (isRefresh) {
            pageindex = 0;
        }
        TraceManager.GetRunRecordList(mSportType, isRefresh ? 0 : pageindex, pagesize, new onNetCallbackListener() {
            @Override
            public void onError(final Throwable throwable) {
                super.onError(throwable);
                mXListView.stopLoadMore();
                mXListView.stopRefresh();
                mXListView.ReFreshNodateStatus(null, true);
                mXListView.setPullRefreshEnable(true);
            }

            @Override
            public void onSuccess(final String requestStr, final String result) {
                Entity_GetRunRecordList mRecordList1 = JSON.parseObject(result, Entity_GetRunRecordList.class);
                mXListView.stopLoadMore();
                mXListView.stopRefresh();
                if (mRecordList1.getCode() == 200) {
                    if (mRecordList1.getData() != null && mRecordList1.getData().size() > 0) {
                        if (isRefresh) {
                            mRecordList = mRecordList1;
                            pageindex += pagesize;
                        } else {
                            pageindex += pagesize;
                            mRecordList.getData().addAll(mRecordList1.getData());
                        }
                        String lastdata = "";
                        int index = 0;
                        while (index < mRecordList.getData().size()) {
                            if (mRecordList.getData().get(index).getId() == 0) {
                                lastdata = qktool.time_getTimeYMD(mRecordList.getData().get(index).getEndTime());
                            } else {
                                if (!lastdata .equalsIgnoreCase(qktool.time_getTimeYMD(mRecordList.getData().get(index).getEndTime()))) {
                                    lastdata = qktool.time_getTimeYMD(mRecordList.getData().get(index).getEndTime());
                                    mRecordList.getData().add(index, new EntityRecord().setEndTime(mRecordList.getData().get(index).getEndTime()));
                                    index++;
                                }
                            }
                            index++;
                        }
                        mXListView.setPullLoadEnable(true);
                        mXListView.setPullRefreshEnable(true);
                        mRecordAdapter.notifyDataSetChanged();
                    } else {
                        mXListView.setPullLoadEnable(false);
                        mXListView.setPullRefreshEnable(true);
                    }
                } else {
                    qktool.ToastShout(mRecordList1.getMsg());
                }
                mXListView.ReFreshNodateStatus(null, true);
            }
        });
    }

    @Override
    protected void initTitleBarData(final TitleBarView mTitleBarView) {
        mTitleBarView.setTitle("运动记录(" + (getIntent().getIntExtra(SportItemType, 1) == 1 ? "跑步)" : "骑行)"));
        mTitleBarView.setDrawableLeft(R.drawable.fanhui, TitleBarView.TitleBarChildView.LEFT);
        if (getIntent().getBooleanExtra(IsCanJump, true)) {
            mTitleBarView.setDrawableLeft(R.drawable.paobuico, TitleBarView.TitleBarChildView.RIGHT);
        } else {
            mTitleBarView.getTitlebarRightbt().setEnabled(false);
        }
        mTitleBarView.setonTitlebarListener(new TitleBarView.onTitleBarListener() {
            @Override
            public void onTitleBarLeftOnClick(final View mView) {
                finish();
            }

            @Override
            public void onTitleBarRightOnClick(final View mView) {
                TraceMainActivity.StartActivity(mContext);
            }

            @Override
            public void onTitleBarTitleOnClick(final View mView) {

            }
        });
    }

    public class RecordAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mRecordList == null || mRecordList.getData() == null ? 0 : mRecordList.getData().size();
        }

        @Override
        public Object getItem(final int position) {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(final int position) {
            return mRecordList.getData().get(position).getId() == 0 ? 2 : 1;
        }

        @Override
        public long getItemId(final int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (getItemViewType(position) == 2) {
                TextView mTextView = (TextView) View.inflate(mContext, R.layout.listview_itemtypetitle, null);
                mTextView.setText(qktool.time_getTimeYMD(mRecordList.getData().get(position).getEndTime()));
                return mTextView;
            } else {
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.tracerecordlistitem, null);
                    convertView.setTag(new ViewHolder(convertView));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            TraceEndActivity.StartActivity(mContext, mRecordList.getData().get(position).getId(), app.sNickName, true);
                        }
                    });
                    convertView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(final View v) {
                            QKManager.ShowDialog_Normal(mContext, "是否删除本次记录?", new QKManager.onDialogListener() {
                                @Override
                                public void onPositive() {
                                   TraceManager.delRecord(mRecordList.getData().get(position).getId(), new onNetCallbackListener() {
                                       @Override
                                       public void onSuccess(String requestStr, String result) {
                                           Response mResponse=JSON.parseObject(result,Response.class);
                                           if (mResponse.getCode()==200){
                                               qktool.ToastShout("记录删除成功");
                                               mRecordList.getData().remove(position);
                                               mRecordAdapter.notifyDataSetChanged();
                                           }else {
                                               qktool.ToastShout(mResponse.getMsg());
                                           }
                                       }
                                   });
                                }

                                @Override
                                public void onNegative() {
                                    qktool.ToastShout("没做处理，onNegative");
                                }
                            });
                            return false;
                        }
                    });
                }
                ViewHolder mViewHolder = (ViewHolder) convertView.getTag();
                EntityRecord mBean = mRecordList.getData().get(position);
                if (mBean.getItemType() == 1) {
                    mViewHolder.type.setImageResource(R.drawable.tracetype_run);
                } else {
                    mViewHolder.type.setImageResource(R.drawable.tracetype_bike);
                }
                DecimalFormat formatVal = new DecimalFormat("##.##");
                mViewHolder.length.setText(formatVal.format(mBean.getMileage() / 1000) + " KM");
                mViewHolder.time.setText(qktool.time_getTimeHms(new Date(mBean.getFinishTime() * 1000 - TimeZone.getDefault().getRawOffset()).getTime()));
                int peisu = mBean.getPace();
                mViewHolder.peisu.setText((peisu / 60 < 10 ? "0" : "") + peisu / 60 + ":" + (peisu % 60 < 10 ? "0" : "") + peisu % 60);

            }
            return convertView;
        }

        public class ViewHolder {
            private ImageView type;
            private TextView length, time, peisu;

            public ViewHolder(View mView) {
                type = (ImageView) mView.findViewById(R.id.type);
                length = (TextView) mView.findViewById(R.id.length);
                time = (TextView) mView.findViewById(R.id.time);
                peisu = (TextView) mView.findViewById(R.id.peisu);
            }
        }
    }
}
