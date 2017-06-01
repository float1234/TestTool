package jupiter.simple_test;

import jupiter.simple_test.util.CSVUtils;
import jupiter.simple_test.util.JdbcUtil;
import jupiter.simple_test.util.LoggerUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by dextry on 2017/5/23.
 */
public class ReadConfig {
    private String resultTable;
    private String downloadPath;

    private static LoggerUtil logger = LoggerUtil.getInstance(ReadConfig.class);


    private static final String URL = "jdbc:mysql://127.0.0.1:3306/matabase?autoReconnect=true&useSSL=true";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static Connection connection;

    static {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ReadConfig() {
        this.resultTable = "20170523_test"; //args[0];
        this.downloadPath = "C:\\operationfiles\\csvfile"; //args[1];
    }

    public static void main(String[] args) throws SQLException, IOException {
        ReadConfig readConfig = new ReadConfig();

        readConfig.countAll();


    }
   /*

    */
    protected void countAll() throws IOException {
        //1. read info
        System.out.println("1, read info");
        logger.info("countAll()", "1. read info");
        try {
            HashMap<String, ArrayList> connectionMap = this.readinfo();
            //2 export data
            System.out.println("export data to  " + this.downloadPath + " and email");
            logger.info("countAll()", "2. export data to  " + this.downloadPath + " and email");
            this.exportData(connectionMap);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("countAll()", e);
        }
    }

    private HashMap<String, ArrayList> readinfo() throws SQLException {
        String sql = "SELECT\n" +
                "\tcard.dataset_query,\n" +
                "\tcard.description,\n" +
                "\tbase.`engine`,\n" +
                "\tbase.details,\n" +
                "\tcard.`name`\n" +
                "FROM\n" +
                "\treport_card card\n" +
                "JOIN metabase_database base\n" +
                "WHERE\n" +
                "\tcard.`name` in ('sqlserver' )\n" +
                "AND card.database_id = base.id;";

        HashMap<String, ArrayList> connectionMap = this.execute(sql);
        return connectionMap;


    }

    private HashMap<String, ArrayList> execute(String sql) throws SQLException {
        System.out.println(Calendar.getInstance().getTime() + " - " + sql);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        HashMap<String, ArrayList> connectionMap = new HashMap();
        while (rs.next()) {
            ArrayList queryAttribute = new ArrayList();

            String queryStr = rs.getString(1);
            String scheduleStr = rs.getString(2);
            String engine = rs.getString(3);
            String conStr = rs.getString(4);
            String filenameSuffix = rs.getString(5);

            String reportSql = null;
            String host = null;
            String port = null;
            String dbname = null;
            String user = null;
            String password = null;
            try {
                JSONObject queryjson = new JSONObject(queryStr);
                JSONObject conjson = new JSONObject(conStr);
                JSONObject schedulejson = new JSONObject(scheduleStr);

                JSONObject aNative = queryjson.getJSONObject("native");
                reportSql = aNative.get("query").toString();

                host = conjson.get("host").toString();
                port = conjson.get("port").toString();
                dbname = conjson.get("dbname").toString();
                user = conjson.get("user").toString();
                password = conjson.get("password").toString();

                String schedule = schedulejson.get("schedule").toString();

                queryAttribute.add(schedule);
                String email = schedulejson.get("email").toString();
                queryAttribute.add(email);

            } catch (JSONException e) {
                e.printStackTrace();
                logger.error("execute()", e);
                continue;
            }


            StringBuffer reportCon = new StringBuffer();
            String reportURL = null;
            if ("mysql".equals(engine)) {
                reportCon.append("jdbc:mysql://");
                 reportURL = reportCon.append(host).append(":").append(port).append("/").append(dbname).append("?useOldAliasMetadataBehavior=true").append("&useSSL=false").toString();
            } else if ("sqlserver".equals(engine)) {
                reportCon.append("jdbc:sqlserver://");
                 reportURL = reportCon.append(host).append(":").append(port).append(";").append(dbname).append("?useOldAliasMetadataBehavior=true").append("&useSSL=false").toString();
            }
            //"jdbc:mysql://127.0.0.1:3306/cnla?autoReconnect=true"

            Connection connection = null;

            try {
                connection = JdbcUtil.getConnection(reportURL, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("execute()", e);
                continue;
            }
            queryAttribute.add(connection);
            queryAttribute.add(filenameSuffix);

            //将一条查询的所有属性存入map
            connectionMap.put(reportSql, queryAttribute);
        }
        statement.close();
        return connectionMap;
    }

    /*传入连接和查询map信息，连接，查询，生成表格，邮件*/
    protected void exportData(HashMap<String, ArrayList> connectionMap) throws SQLException {
        int count = 0;
        for (String key : connectionMap.keySet()) {
            System.out.println("key= " + key);
            logger.info("exportData()", "key= " + key);
            ArrayList queryAttribute = connectionMap.get(key);

            String schedule = (String) queryAttribute.get(0);
            String email = (String) queryAttribute.get(1);
            Connection connection = (Connection) queryAttribute.get(2);
            String filenameSuffix = (String) queryAttribute.get(3);

            //定时器执行
            try {
                // 首先，必需要取得一个Scheduler的引用
                SchedulerFactory sf = new StdSchedulerFactory();
                Scheduler sched = sf.getScheduler();
                //jobs可以在scheduled的sched.start()方法前被调用
                JobDataMap data = new JobDataMap();
                data.put("connection", connection);
                data.put("sql", key);
                data.put("email", email);
                data.put("filenameSuffix", filenameSuffix);


                JobDetail job = newJob(MyJob.class).withIdentity("job" + count, "group" + count).usingJobData(data).build();
                CronTrigger trigger = null;
                try {
                    trigger = newTrigger().withIdentity("trigger" + count, "group" + count).withSchedule(cronSchedule(schedule)).build();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("ReadConfig.export()",e);
                    continue;
                }
                ++count;
                Date ft = sched.scheduleJob(job, trigger);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
                System.out.println(job.getKey() + " 已被安排执行于: " + sdf.format(ft) + "，并且以如下重复规则重复执行: " + trigger.getCronExpression());
                sched.start();

            } catch (SchedulerException e) {
                e.printStackTrace();
                logger.error("exportData()", e);
                continue;
            }
        }
        try {
            //主线程等待一分钟
            Thread.sleep(60L * 1000L);
        } catch (Exception e) {
        }
        //关闭定时调度，定时器不再工作
      //  sched.shutdown(true);
    }
}
