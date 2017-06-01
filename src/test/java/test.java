import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dextry on 2017/5/11.
 */
public class test {
    @org.junit.Test
    public  void test(){
        String haha = haha();
        System.out.println(haha);


    }
    @org.junit.Test
    public  void test1(){
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        System.out.println(format);



    }
    @org.junit.Test
    public  void test3(){
        System.out.println(getFixLenthString(6));



    }
    private static String getFixLenthString(int strLength) {

        Random rm = new Random();

        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    @org.junit.Test
    public  void test2(){
        Map<String, Integer> map = new LinkedHashMap<String, Integer>();
        map.put("2017-05-08", 5);
        map.put("2017-05-09", 6);
        map.put("2017-05-10", 6);
        map.put("2017-05-11",6);
        map.put("2017-05-12",5);
     /*   map.put("d", 7);
        map.put("c", 6);
        map.put("b", 6);
        map.put("a",6);
        map.put("e",7);
*/
        List<Map.Entry<String, Integer>> infoIds =
                new ArrayList<Map.Entry<String, Integer>>(map.entrySet());


        //排序
      /*  Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                //return (o2.getValue() - o1.getValue());
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });*/

        for (Map.Entry<String, Integer> m : map.entrySet()){
       System.out.println(m.getKey()+"--------"+m.getValue());
   }



    }

    @org.junit.Test
    public  void test5(){

        String host = "127.1.1.1";
        String port = "3306";
        String engine = "mysql";
        String dbname = "cnla";
       // String user = (String)conjson.get("user");
       // String password = (String)conjson.get("password");
        StringBuffer reportCon = new StringBuffer();
        if ("mysql".equals(engine)) {
            reportCon.append("jdbc:mysql://");
        }else if ("sqlserver".equals(engine)){
            // reportCon.append("jdbc:mysql://");
        }
        //"jdbc:mysql://127.0.0.1:3306/cnla?autoReconnect=true"
        reportCon.append(host).append(":").append(port).append("/").append(dbname);
        System.out.println(reportCon);

    }

    @org.junit.Test
    public  void test6(){
        ArrayList arrayList = new ArrayList();
        arrayList.add("haa");
        arrayList.add("d");
        arrayList.add("a");
        arrayList.add("e");
        for (Object x :arrayList){
            System.out.println(x);
        }

    }
    @org.junit.Test
    public  void test7(){
       String jsonstr = "{\"database\":3,\"type\":\"native\",\"native\":{\"query\":\"SELECT q.category as '类别', q.remark as ' 标志',q.id,q.sceneId as '场景值' from wx_qrcode q LIMIT 50\"}}";
        JSONObject queryjson = new JSONObject(jsonstr);
        JSONObject aNative = queryjson.getJSONObject("native");
        String query = aNative.get("query").toString();

        System.out.println(query);

    }
    @org.junit.Test
    public  void test8(){
       //            {schedule:'*/5 * * * * ', email:'huang.st@pg.com'}
        String s  = "{schedule:'0 * * * * *' ,email: 'huang.st@pg.com;geoffrey@epam.com'}";
        JSONObject schedulejson = new JSONObject(s);

    }


    public static String haha(){
        return "hello \n world"; // 返回“hello”换行“world”
    }
}
