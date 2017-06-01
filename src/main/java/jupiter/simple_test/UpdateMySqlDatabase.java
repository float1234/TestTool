package jupiter.simple_test;

/**
 * Created by dextry on 2017/5/5.
 */
import java.sql.*;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class UpdateMySqlDatabase {
    static int lport;
    static String rhost;
    static int rport;
    public static void go(){
        String user = "wcpadmin";
        String password = "Z6u0S*pLK2Vc";
        String host = "s2wcp.chinacloudapp.cn";
        int port=22201;
        try
        {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            lport = 3306;
            rhost = "wcp.mysqldb.chinacloudapi.cn";
            rport = 3306;
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            String boundaddress ="42.159.235.75";
            int assinged_port=session.setPortForwardingL(boundaddress,lport, rhost, rport);
            System.out.println("localhost:"+assinged_port+" -> "+rhost+":"+rport);
        }
        catch(Exception e){System.err.print(e);}
    }
    public static void main(String[] args) {
        try{
            go();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println("An example for updating a Row from Mysql Database!");
        Connection con = null;
        String driver = "com.mysql.jdbc.Driver";

        String url = "jdbc:mysql://" + rhost +":" + lport + "/";
        System.out.println(url);
        String db = "test";
        String dbUser = "wcp%pgadmin";
        String dbPasswd = "nXYs464wks*G98Q";
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url+db, dbUser, dbPasswd);
            try{
                Statement st = con.createStatement();
                String sql = "create table blog_user\n" +
                        "(\n" +
                        "  user_Name char(15) not null check(user_Name !=''),\n" +
                        "  user_Password char(15) not null,\n" +
                        "  user_emial varchar(20) not null unique,\n" +
                        "  primary key(user_Name)         \n" +
                        " \n" +
                        ") ";

                boolean execute = st.execute(sql);
                if(execute == true){
                    System.out.println("创建表成功.");
                }
                else{
                    System.out.println("创建表失败.");
                }
            }
            catch (SQLException s){
                System.out.println("SQL statement is not executed!");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}