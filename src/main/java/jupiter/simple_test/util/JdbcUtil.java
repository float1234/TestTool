package jupiter.simple_test.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * JDBC工具类
 */
public final class JdbcUtil {
	
	//连接数据库服务器的四个必要的属性
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url;
	private static String user;
	private static String password;
	
	//加载资源文件中的数据库服务器属性
/*	static{
		try {
			InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("mysql.properties");
			Properties props = new Properties();
			props.load(is);
			driver = props.getProperty("driver");
			url = props.getProperty("url");
			user = props.getProperty("user");
			password = props.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("加载资源文件失败");
		}
	}
	*/
	//注册数据库服务器驱动
	static{
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("注册数据库驱动失败");
		}
	}

	private JdbcUtil(){}
	
	//获取数据库连
	public static Connection getConnection(String url,String user,String password) throws SQLException{
		try {
			return DriverManager.getConnection(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//关闭Connection对象
	public static void close(Connection conn) throws SQLException{
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}finally{
				conn = null;
			}
		}
	}
	
	//关闭Statement对象
	public static void close(Statement stmt) throws SQLException{
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}finally{
				stmt = null;
			}
		}
	}
	
	//关闭ResultSet对象
	public static void close(ResultSet rs) throws SQLException{
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}finally{
				rs = null;
			}
		}
	}
}
