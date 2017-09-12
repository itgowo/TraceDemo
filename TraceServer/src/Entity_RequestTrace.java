/**
 * Created by hnvfh on 2017/5/15.
 */
public class Entity_RequestTrace {
    public static final int RequestNo = 0, RequestDetails = 1, RequestList = 2, RequestUpload = 3, RequestInfo = 4,RequestDel=5;
    private int requestType = 0;  //0;不请求数据  1：根据id获取详情（id）    2：获取轨迹列表(pageindex,pagesize)   3:上传轨迹   4:获取统计信息  关联运动类型，用id传
    private int id;
    private int pageindex = 1;
    private int pagesize = 20;
    private Entity_Record entity;

    public int getRequestType() {
        return requestType;
    }

    public Entity_RequestTrace setRequestType(int mRequestType) {
        requestType = mRequestType;
        return this;
    }

    public int getId() {
        return id;
    }

    public Entity_RequestTrace setId(int mId) {
        id = mId;
        return this;
    }

    public int getPageindex() {
        return pageindex;
    }

    public Entity_RequestTrace setPageindex(int mPageindex) {
        pageindex = mPageindex;
        return this;
    }

    public int getPagesize() {
        return pagesize;
    }

    public Entity_RequestTrace setPagesize(int mPagesize) {
        pagesize = mPagesize;
        return this;
    }

    public Entity_Record getEntity() {
        return entity;
    }

    public Entity_RequestTrace setEntity(Entity_Record mEntity) {
        entity = mEntity;
        return this;
    }
}
