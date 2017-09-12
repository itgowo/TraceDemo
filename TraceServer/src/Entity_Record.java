public class Entity_Record {
    private int id;
    private double mileage; // 里程
    private long finishTime; // 用时
    private double avgSpeed; // 平均速度
    private double altitude; // 爬升
    private long startTime; // 开始时间
    private long endTime; // 结束时间
    private int pace; // 配速
    private int itemType; // 类型 1 跑步 2 骑行
    private String records; // 记录

    public int getId() {
        return id;
    }

    public Entity_Record setId(int mId) {
        id = mId;
        return this;
    }

    public double getMileage() {
        return mileage;
    }

    public Entity_Record setMileage(double mMileage) {
        mileage = mMileage;
        return this;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public Entity_Record setFinishTime(long mFinishTime) {
        finishTime = mFinishTime;
        return this;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public Entity_Record setAvgSpeed(double mAvgSpeed) {
        avgSpeed = mAvgSpeed;
        return this;
    }

    public double getAltitude() {
        return altitude;
    }

    public Entity_Record setAltitude(double mAltitude) {
        altitude = mAltitude;
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    public Entity_Record setStartTime(long mStartTime) {
        startTime = mStartTime;
        return this;
    }

    public long getEndTime() {
        return endTime;
    }

    public Entity_Record setEndTime(long mEndTime) {
        endTime = mEndTime;
        return this;
    }

    public int getPace() {
        return pace;
    }

    public Entity_Record setPace(int mPace) {
        pace = mPace;
        return this;
    }

    public int getItemType() {
        return itemType;
    }

    public Entity_Record setItemType(int mItemType) {
        itemType = mItemType;
        return this;
    }

    public String getRecords() {
        return records;
    }

    public Entity_Record setRecords(String mRecords) {
        records = mRecords;
        return this;
    }
}
