import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hnvfh on 2017/5/15.
 */
@WebServlet(name = "Trace")
public class Trace extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        BufferedReader mReader = request.getReader();
        StringBuilder out = new StringBuilder();
        String data;
        while ((data = mReader.readLine()) != null) {
            out.append(data + "\r\n");
        }
        data = out.toString().trim();
        Entity_RequestTrace mRequestTrace = null;
        try {
            mRequestTrace = JSON.parseObject(data, Entity_RequestTrace.class);
        } catch (Exception mE) {
            mE.printStackTrace();
            response.getWriter().write(new Response().setCode(ErrorEnum.Json解析异常).setMsg(ErrorEnum.Code_201).setData(mE.getMessage()).toJson());
        }
        try {
            switch (mRequestTrace.getRequestType()) {
                case Entity_RequestTrace.RequestDetails:
                    Entity_Record mRecord = DBManager.getTraceRecord(mRequestTrace.getId());
                    response.getWriter().write(new Response().setData(mRecord).toJson());
                    break;
                case Entity_RequestTrace.RequestList:
                    List<Entity_Record> mRecords = DBManager.getTraceRecordList(mRequestTrace.getId(),mRequestTrace.getPageindex(), mRequestTrace.getPagesize());
                    response.getWriter().write(new Response().setData(mRecords).toJson());
                    break;
                case Entity_RequestTrace.RequestUpload:
                    int mI = DBManager.addTraceRecord(mRequestTrace.getEntity());
                    if (mI > 0) {
                        response.getWriter().write(new Response().setData(new Entity_Record().setId(mI)).toJson());
                    } else {
                        response.getWriter().write(new Response().setCode(ErrorEnum.数据库写入异常).setMsg(ErrorEnum.Code_204).toJson());
                    }
                    break;
                case Entity_RequestTrace.RequestInfo:
                    Entity_TotalInfo mInfo = DBManager.getTraceTotalInfo(mRequestTrace.getId());
                    response.getWriter().write(new Response().setData(mInfo).toJson());
                    break;
                    case Entity_RequestTrace.RequestDel:
                        int mI1=DBManager.deleteTraceRecord(mRequestTrace.getId());
                        if (mI1>0){
                            response.getWriter().write(new Response().toJson());
                        }else {
                            response.getWriter().write(new Response().setCode(ErrorEnum.数据库写入异常).setMsg(ErrorEnum.Code_204).toJson());
                        }
                        break;
                default:
                    response.getWriter().write(new Response().setCode(ErrorEnum.参数错误).setMsg(ErrorEnum.Code_207).toJson());
                    break;
            }
        } catch (Exception mE) {
            mE.printStackTrace();
            response.getWriter().write(new Response().setCode(ErrorEnum.数据库异常).setMsg(ErrorEnum.Code_202).setData(mE.getMessage()).toJson());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(new Response().setCode(ErrorEnum.请求方式错误toPost).setMsg(ErrorEnum.Code_101).toJson());
    }
}
