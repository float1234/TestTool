package jupiter.simple_test;

import jupiter.simple_test.util.CSVUtils;
import jupiter.simple_test.util.LoggerUtil;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dextry on 2017/5/25.
 */
public class MyJob implements Job {
    private String downloadPath = "C:\\operationfiles\\csvfile";
    private static LoggerUtil logger = LoggerUtil.getInstance(MyJob.class);
    private static int count;

    public void execute(JobExecutionContext jobContext) throws JobExecutionException {

        System.out.println("--------------------------------------------------------------------");

        System.out.println("MyJob start: " + jobContext.getFireTime());

        JobDetail jobDetail = jobContext.getJobDetail();
        System.out.println("MyJob end: " + jobContext.getJobRunTime() + ", key: " + jobDetail.getKey());
        System.out.println("MyJob next scheduled time: " + jobContext.getNextFireTime());
        Connection connection = (Connection) jobDetail.getJobDataMap().get("connection");
        String sql = (String) jobDetail.getJobDataMap().get("sql");
        String email = (String) jobDetail.getJobDataMap().get("email");
        String filenameSuffix = (String) jobDetail.getJobDataMap().get("filenameSuffix");
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);


            int count = 0;
            List<List<Object>> arrayLists = new ArrayList<List<Object>>();
            ArrayList<Object> head = new ArrayList<Object>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                // resultSet数据下标从1开始
                head.add(metaData.getColumnName(i + 1));
            }
            while (resultSet.next()) {
                ArrayList<Object> content = new ArrayList<Object>();
                int j = 0;
                String column;
                while (j < metaData.getColumnCount()) {
                    column = resultSet.getString(j + 1);
                    content.add(column);
                    j++;
                }
                arrayLists.add(content);
                count++;
            }
            String filename = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + "_" + filenameSuffix;
            CSVUtils.createCSVFile(head, arrayLists, downloadPath, filename);
            System.out.println(Calendar.getInstance().getTime() + " - " + "write " + count);

            String[] parameters = new String[]{
                    downloadPath, filename, email
            };
            JavaMailWithAttachment.main(parameters);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("myJob.execute()",e);
        }


        System.out.println("--------------------------------------------------------------------");
  }
}

