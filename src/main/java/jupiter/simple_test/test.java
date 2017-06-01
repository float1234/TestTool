package jupiter.simple_test;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.nutz.json.Json;
import sun.applet.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static javafx.application.Platform.exit;

/**
 * Created by dextry on 2017/2/17.
 */
public class test {

    public static void main(String[] args) throws IOException, InterruptedException, SQLException, ConfigurationException {
        if (args.length == 0) {
            System.out.println("参数不正确。");
        }
        String command = args[0];
        if (command.equals("-check")) {
            System.out.println("you are the fool");
        }else if(command.equals("-list")){
            System.out.println("################################");
            System.out.println("1. total fans count");
            System.out.println("2 new compagin fans count");
            System.out.println("3. cat");
            System.out.println("#####################################");
            Scanner scanner = new Scanner(System.in);
            if (scanner.nextInt() == 1){
                System.out.println("it is a tiger");
            }else if (scanner.nextInt() == 2){
                System.out.println("it is a mouse");
            }else if (scanner.nextInt() == 3){
                System.out.println("it is a cat");
            }
        }else if (command.equals("-queryfans")){
            if ( args[1].equals(null)){
                System.out.println("请输入开始时间 格式：yyyy-MM-dd");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
            String startTime = args[1];
            String endTime = args[2];
            try {
                sdf.parse(startTime);
                sdf.parse(endTime);
            } catch (ParseException e) {
                System.out.println("请输入正确的时间格式：yyyy-MM-dd");
                System.exit(-1);
            }
            String companyid = args[3];
            String[] pamaters = new String[]{
                    startTime, endTime, companyid
            };
            querryFans.main(pamaters);


        }else{
            File file = new File(command);
            if (file.isFile() && file.getName().endsWith(".xml")) {
                XMLConfiguration configuration = new XMLConfiguration(file);
                String resultTable = StringUtils.join(configuration.getStringArray("resultTable"), ",");
                String downloadPath = StringUtils.join(configuration.getStringArray("downloadPath"), ",");
                String exportSql = StringUtils.join(configuration.getStringArray("createResultTableSql"), ",");
                String fileName = StringUtils.join(configuration.getStringArray("fileName"), ",");
                String[] pamaters = new String[]{
                        resultTable, downloadPath, exportSql, fileName
                };
                System.out.println(Json.toJson(pamaters));
                Thread.sleep(10000);
                countAll.main(pamaters);
            }
        }
    }
}
