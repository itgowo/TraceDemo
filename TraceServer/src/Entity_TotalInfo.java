/**
 * Created by hnvfh on 2017/5/16.
 */
public class Entity_TotalInfo   {
    private double totalmileage; // 里程
    private long totalTime; // 用时
    private int type; // 类型

    public int getType() {
        return type;
    }

    public Entity_TotalInfo setType(int mType) {
        type = mType;
        return this;
    }

    public double getTotalmileage() {
        return totalmileage;
    }

    public Entity_TotalInfo setTotalmileage(double mTotalmileage) {
        totalmileage = mTotalmileage;
        return this;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public Entity_TotalInfo setTotalTime(long mTotalTime) {
        totalTime = mTotalTime;
        return this;
    }
}
