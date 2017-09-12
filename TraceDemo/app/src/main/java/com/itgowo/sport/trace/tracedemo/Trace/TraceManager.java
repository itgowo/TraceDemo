package com.itgowo.sport.trace.tracedemo.Trace;


import com.itgowo.sport.trace.tracedemo.Other.QKManager;
import com.itgowo.sport.trace.tracedemo.Other.onNetCallbackListener;
import com.itgowo.sport.trace.tracedemo.Trace.Entity.RequestTrace;

/**
 * Created by lujianchao on 2017/1/4.
 *
 * @author lujianchao
 */

public class TraceManager {
    public static String ROOT_URL = "http://itgowo.com:8081/TraceServer/Trace"; // 用户轨迹操作
//    public static String ROOT_URL = "http://1.1.1.156:8080/Trace"; // 用户轨迹操作
    /**
     * 获取轨迹详情
     *
     * @param runRecordId
     * @param mListener
     */
    public static void GetRunRecord (int runRecordId, onNetCallbackListener mListener) {
        RequestTrace mRequestALL = new RequestTrace ().setId (runRecordId).setRequestType(RequestTrace.RequestDetails) ;
        QKManager.http_post (ROOT_URL, "", mRequestALL, mListener);

    }



    /**
     * 获取运动轨迹记录列表
     *
     * @param itemType  运动类型 1：跑步   2：骑行
     * @param mListener
     */
    public static void GetRunRecordList (int itemType, int index,int size, onNetCallbackListener mListener) {
        RequestTrace request = new RequestTrace ().setId (itemType).setPageindex (index).setPagesize(size).setRequestType(RequestTrace.RequestList);
        QKManager.http_post (ROOT_URL, "", request, mListener);
    }
    /**
     * 删除运动轨迹记录
     *
     * @param mListener
     */
    public static void delRecord(int id, onNetCallbackListener mListener) {
        RequestTrace request = new RequestTrace ().setId (id).setRequestType(RequestTrace.RequestDel);
        QKManager.http_post (ROOT_URL, "", request, mListener);
    }
    /**
     * 获取运动总信息，首页信息
     *
     * @param itemType
     * @param mListener
     */
    public static void GetRunRecordIndexPage (int itemType, onNetCallbackListener mListener) {
        RequestTrace request = new RequestTrace().setRequestType (RequestTrace.RequestInfo).setId(itemType);
        QKManager.http_post (ROOT_URL, "", request, mListener);
    }
}
