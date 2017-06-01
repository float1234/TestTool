package jupiter.simple_test;


import com.mysql.jdbc.StringUtils;
import jupiter.simple_test.util.CSVUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dextry on 2017/4/20.
 */
public class countAll {
    private String resultTable;
    private String downloadPath;
    private String createResultTableSql;
    private  String fileName;

 /*   private static final String URL = "jdbc:mysql://wcp.mysqldb.chinacloudapi.cn:3306/wcpproddb?autoReconnect=true";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "wcp%pgadmin";
    private static final String PASSWORD = "nXYs464wks*G98Q";*/

    private static final String URL = "jdbc:mysql://cnla20161207.mysqldb.chinacloudapi.cn:3306/cnla?autoReconnect=true";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "cnla20161207%db";
    private static final String PASSWORD = "FCOaYqm61jKQ";
    private static Connection connection;

    static {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public countAll(String[] args) {
        this.resultTable = args[0];
        this.downloadPath = args[1];
        this.createResultTableSql = args[2];
        this.fileName = args[3];

        if (org.apache.commons.lang3.StringUtils.isBlank(this.resultTable)) {
            throw new RuntimeException("resultTable没有配置");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(this.downloadPath)) {
            throw new RuntimeException("downloadPath没有配置");
        }
        if (!new File(this.downloadPath).getParentFile().exists()) {
            throw new RuntimeException("下载目录不存在 " + this.downloadPath);
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(this.fileName)) {
            throw new RuntimeException("文件名没有配置" );
        }

    }

    public static void main(String[] args) throws SQLException, IOException {
        countAll countAll = new countAll(args);

        countAll.countAll();

    }

    protected void countAll() throws SQLException, IOException {
        //1. drop result table
        System.out.println("\n//1. drop result table " + this.resultTable);
        this.dropResultTable();
        //2. create result table
        System.out.println("\n//2. create result table " + this.resultTable);
        this.createResultTable();
        //3. export data
        System.out.println("\n//3. export data to " + this.downloadPath);
        this.exportData();

        //4 email out.
        System.out.println("\n//4. email to somebody");
        String[] parameters = new String[]{
                 downloadPath, fileName
        };
         JavaMailWithAttachment.main(parameters);



    }

    private void dropResultTable() {
        String sql = "drop table " + this.resultTable;
        try {
            this.execute(sql);
        } catch (SQLException e) {
            //Ignored
        }
    }

    private void execute(String sql) throws SQLException {
        System.out.println(Calendar.getInstance().getTime() + " - " + sql);
        Statement statement = connection.createStatement();
        statement.execute(sql);
        statement.close();
    }

    private void createResultTable() throws SQLException {

        List<String> sqls = StringUtils.split(this.createResultTableSql, ";", true);
        for (String sql : sqls) {
            if (!StringUtils.isEmptyOrWhitespaceOnly(sql)) {
                if (sql.toLowerCase().contains("drop")) {
                    try {
                        this.execute(sql);
                        continue;
                    } catch (Exception e) {
                        //
                    }
                } else {
                    this.execute(sql);
                }
            }
        }

       /* String sql = "create  index idx_1 on " + this.tmpTableResult + " (openid);";
        this.execute(sql);*/
    }

    private void exportData() throws SQLException, IOException {
        String sql = "select * from " + this.resultTable;
        System.out.println(Calendar.getInstance().getTime() + " - " + sql);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        File file = new File(this.downloadPath  + File.separator + fileName + ".csv");
        if (file.exists()) {
            file.delete();
        }
        int count = 0;
        List<List<Object>> arrayLists = new ArrayList<List<Object>>();
        ArrayList<Object> head = new ArrayList<Object>();
        ResultSetMetaData metaData = rs.getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            // resultSet数据下标从1开始
            head.add(metaData.getColumnName(i + 1));
        }
        while (rs.next()) {
            ArrayList<Object> content = new ArrayList<Object>();
            int j = 0;
            String column;
            while (j <  metaData.getColumnCount()) {
                 column = rs.getString(j+1);
                 content.add(column);
                j++;
            }
                arrayLists.add(content);
                count++;
        }
        CSVUtils.createCSVFile(head,arrayLists,downloadPath,fileName);
        System.out.println(Calendar.getInstance().getTime() + " - " + "write " + count);
        statement.close();
    }
}
