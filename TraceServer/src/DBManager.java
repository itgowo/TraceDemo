import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hnvfh on 2017/5/15.
 */
public class DBManager {
    private static boolean isFirstRun = true;
    private static String Url = "jdbc:sqlite:record.db";
    private static String CreatTableSql = "CREATE TABLE  record (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"mileage\"  REAL,\n" +
            "\"finishTime\"  INTEGER,\n" +
            "\"avgSpeed\"  REAL,\n" +
            "\"altitude\"  REAL,\n" +
            "\"startTime\"  INTEGER,\n" +
            "\"endTime\"  INTEGER,\n" +
            "\"pace\"  INTEGER,\n" +
            "\"itemType\"  INTEGER,\n" +
            "\"records\"  TEXT\n" +
            ")";

    public synchronized static void init() {
        Connection connection = null;
        // load the sqlite-JDBC driver using the current class loader
        try {
            Class.forName("org.sqlite.JDBC");
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:record.db");
            if (isFirstRun) {
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);  // set timeout to 30 sec.
                ResultSet mSet = statement.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='record'");
                int count = 0;
                while (mSet.next()) {
                    count = mSet.getInt(1);
                }
                if (count == 0) {
                    statement.executeUpdate(CreatTableSql);
                }
                statement.close();
                connection.close();
                isFirstRun = false;
            }
        } catch (Exception e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
    }

    static {
        init();
    }

    /**
     * 添加一条数据
     *
     * @param mEntity_record
     * @return
     */
    public synchronized static int addTraceRecord(Entity_Record mEntity_record) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(Url);
            int result;
            String sql = "insert into record (mileage,finishTime,avgSpeed,altitude,startTime,endTime,pace,itemType,records) values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstmt;
            pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, mEntity_record.getMileage());
            pstmt.setLong(2, mEntity_record.getFinishTime());
            pstmt.setDouble(3, mEntity_record.getAvgSpeed());
            pstmt.setDouble(4, mEntity_record.getAltitude());
            pstmt.setLong(5, mEntity_record.getStartTime());
            pstmt.setLong(6, mEntity_record.getEndTime());
            pstmt.setInt(7, mEntity_record.getPace());
            pstmt.setInt(8, mEntity_record.getItemType());
            pstmt.setString(9, mEntity_record.getRecords());
            result = pstmt.executeUpdate();
            pstmt.close();
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from record order by id desc limit 0,1");
            if (rs.next()) {
                result = rs.getInt(1);
            }
            statement.close();
            connection.close();
            return result;
        } catch (Exception mE) {
            mE.printStackTrace();
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException mE1) {
                    mE1.printStackTrace();
                }
            }
        }
        return -1;
    }

    /**
     * 根据ID查轨迹记录
     *
     * @param id
     * @return
     */
    public synchronized static Entity_Record getTraceRecord(int id) {
        Connection connection = null;
        Entity_Record mEntity_record = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(Url);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from record WHERE id=" + id);
            while (rs.next()) {
                mEntity_record = new Entity_Record();
                mEntity_record.setId(rs.getInt("id"));
                mEntity_record.setMileage(rs.getDouble("mileage"));
                mEntity_record.setFinishTime(rs.getLong("finishTime"));
                mEntity_record.setAvgSpeed(rs.getDouble("avgSpeed"));
                mEntity_record.setAltitude(rs.getDouble("altitude"));
                mEntity_record.setStartTime(rs.getLong("startTime"));
                mEntity_record.setEndTime(rs.getLong("endTime"));
                mEntity_record.setPace(rs.getInt("pace"));
                mEntity_record.setItemType(rs.getInt("itemType"));
                mEntity_record.setRecords(rs.getString("records"));
            }
            statement.close();
            connection.close();
            return mEntity_record;
        } catch (Exception mE) {
            mE.printStackTrace();
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException mE1) {
                    mE1.printStackTrace();
                }
            }
        }
        return mEntity_record;
    }

    /**
     * 获取某一项运动类型的总数据
     *
     * @param type 1:跑步   2：骑行
     * @return
     */
    public synchronized static Entity_TotalInfo getTraceTotalInfo(int type) {

        Entity_TotalInfo mEntity_totalInfo = new Entity_TotalInfo();
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(Url);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select sum(mileage),sum(finishTime) from record WHERE itemType=" + type);

            while (rs.next()) {
                mEntity_totalInfo.setTotalmileage(rs.getDouble(1));
                mEntity_totalInfo.setTotalTime(rs.getLong(2));
                mEntity_totalInfo.setType(type);
            }
            statement.close();
            connection.close();
            return mEntity_totalInfo;
        } catch (Exception mE) {
            mE.printStackTrace();
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException mE1) {
                    mE1.printStackTrace();
                }
            }
        }
        return mEntity_totalInfo;
    }

    /**
     * 轨迹记录列表
     *
     * @param pageindex
     * @param pagesize
     * @return
     */
    public synchronized static List<Entity_Record> getTraceRecordList(int type, int pageindex, int pagesize) {
        List<Entity_Record> mRecords = new ArrayList<>();
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(Url);
            PreparedStatement statement = connection.prepareStatement("SELECT record.id,record.mileage,record.finishTime,record.avgSpeed,record.altitude,record.startTime,record.endTime,record.pace,record.itemType FROM record WHERE itemType=? ORDER BY endTime DESC LIMIT ?,?");
            statement.setInt(1, type);
            statement.setInt(2, pageindex);
            statement.setInt(3, pagesize);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Entity_Record mEntity_record = new Entity_Record();
                mEntity_record.setId(rs.getInt("id"));
                mEntity_record.setMileage(rs.getDouble("mileage"));
                mEntity_record.setFinishTime(rs.getLong("finishTime"));
                mEntity_record.setAvgSpeed(rs.getDouble("avgSpeed"));
                mEntity_record.setAltitude(rs.getDouble("altitude"));
                mEntity_record.setStartTime(rs.getLong("startTime"));
                mEntity_record.setEndTime(rs.getLong("endTime"));
                mEntity_record.setPace(rs.getInt("pace"));
                mEntity_record.setItemType(rs.getInt("itemType"));
//                mEntity_record.setRecords(rs.getString("records"));
                mRecords.add(mEntity_record);
            }
            statement.close();
            connection.close();
            return mRecords;
        } catch (Exception mE) {
            mE.printStackTrace();
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException mE1) {
                    mE1.printStackTrace();
                }
            }
        }
        return mRecords;
    }

    /**
     * 删除轨迹记录
     *
     * @param id
     * @return
     */
    public synchronized static int deleteTraceRecord(int id) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(Url);
            Statement statement = connection.createStatement();
            int mI = statement.executeUpdate("DELETE from record where id=" + id);
            return mI;
        } catch (Exception mE) {
            mE.printStackTrace();
        }
        return -1;
    }
}
