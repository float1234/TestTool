package jupiter.simple_test;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.sql.*;

/**
 * Created by dextry on 2017/4/21.
 */
public class scatertest {

    private static final String URL = "jdbc:mysql://wcp.mysqldb.chinacloudapi.cn:3306/wcpproddb?autoReconnect=true";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "wcp%pgadmin";
    private static final String PASSWORD = "nXYs464wks*G98Q";
    private static String username = "azureuser";
    private static String host = "alphabetcs.chinacloudapp.cn";
    private static  int port = 22;
    private static String pwd = "wVJkHfsmQ1";

    public static void main(String[] args)  {
        try {
            //1、加载驱动
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //2、创建连接
        Connection conn = null;
        Connection conn2 = null;
      /*  try {

            conn2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            getData(conn2);
        } catch (SQLException e) {
            System.out.println("未连接上数据库");
            e.printStackTrace();
        }*/

        try{
            System.out.println("=============");
            go();
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            getData(conn);


        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
    private static void getData(Connection conn) throws SQLException {

        // 获取所有表名
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement
                .executeQuery("select * from 20170421_bruan_fans");
        // 获取列名
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            // resultSet数据下标从1开始
            String columnName = metaData.getColumnName(i + 1);
            int type = metaData.getColumnType(i + 1);
            if (Types.INTEGER == type) {
                // int
            } else if (Types.VARCHAR == type) {
                // String
            }
            System.out.print(columnName + "\t");
        }
        System.out.println();
        // 获取数据
        while (resultSet.next()) {
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                // resultSet数据下标从1开始
                System.out.print(resultSet.getString(i + 1) + "\t");
            }
            System.out.println();

        }
        statement.close();
        conn.close();
    }

    public static void go() {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, port);
            session.setPassword(pwd);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            System.out.println(session.getServerVersion());//这里打印SSH服务器版本信息

            //ssh -L 192.168.0.102:5555:192.168.0.101:3306 yunshouhu@192.168.0.102  正向代理
           // int assinged_port = session.setPortForwardingL("192.168.0.101",5555, "192.168.0.101", 3306);//端口映射 转发

            //System.out.println("localhost:" + assinged_port);

            //ssh -R 192.168.0.102:5555:192.168.0.101:3306 yunshouhu@192.168.0.102
            //session.setPortForwardingR("192.168.0.102",5555, "192.168.0.101", 3306);
            // System.out.println("localhost:  -> ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
