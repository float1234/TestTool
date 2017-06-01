package sqlserver;
import java.sql.*;

/**
 * Created by dextry on 2017/5/31.
 */
public class Main {


    public static void main(String [] args)

    {

        String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";

        //String dbURL="jdbc:sqlserver://127.0.0.1:1433;test?useOldAliasMetadataBehavior=true&useSSL=false
        String dbURL="jdbc:sqlserver://127.0.0.1:1433;test?useOldAliasMetadataBehavior=true&useSSL=false";

        String userName="admin";

        String userPwd="admin";

        try

        {

            Class.forName(driverName);

            Connection connection = DriverManager.getConnection(dbURL, userName, userPwd);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT  * from test.dbo.product");

            while (rs.next()){
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
            }


            System.out.println("连接数据库成功");

        }

        catch(Exception e)

        {

            e.printStackTrace();

            System.out.print("连接失败");

        }

    }
}
