package jupiter.simple_test;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.StringUtils;
import jupiter.simple_test.util.CSVUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dextry on 2017/5/5.
 */
public class exportExcel {
    private String resultTable;
    private String downloadPath;
    private String createResultTableSql;
    private String fileName;

    static int lport;
    static String rhost;
    static int rport;

    private static Connection connection;

    static {
        try {
            go();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("An example for updating a Row from Mysql Database!");
        //Connection con = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + rhost + ":" + lport + "/";
        String db = "wcpproddb";
        String dbUser = "wcp%pgadmin";
        String dbPasswd = "nXYs464wks*G98Q";
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url + db, dbUser, dbPasswd);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void go() {
        String user = "wcpadmin";
        String password = "Z6u0S*pLK2Vc";
        String host = "s2wcp.chinacloudapp.cn";
        int port = 22201;
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            lport = 4321;
            rhost = "localhost";
            rport = 3306;
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            int assinged_port = session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    public exportExcel(String[] args) {
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
            throw new RuntimeException("文件名没有配置");
        }

    }

    public static void main(String[] args)throws SQLException, IOException  {
        countAll countAll = new countAll(args);

        countAll.countAll();
    }

    private void countAll() throws SQLException, IOException {
        //1. drop result table
        System.out.println("\n//1. drop result table " + this.resultTable);
        this.dropResultTable();
        //2. create result table
        System.out.println("\n//2. create result table " + this.resultTable);
        this.createResultTable();
        //3. export data
        System.out.println("\n//3. export data to " + this.downloadPath);
        this.exportData();

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
