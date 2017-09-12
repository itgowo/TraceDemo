import com.alibaba.fastjson.JSON;

/**
 * Created by hnvfh on 2017/5/15.
 */
public class Response {
    private int code=200;
    private String msg="成功";
    private Object mData;

    public int getCode() {
        return code;
    }

    public Response setCode(int mCode) {
        code = mCode;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Response setMsg(String mMsg) {
        msg = mMsg;
        return this;
    }

    public Object getData() {
        return mData;
    }

    public Response setData(Object mData) {
        this.mData = mData;
        return this;
    }
    public String toJson(){
        return JSON.toJSONString(this);
    }
}
