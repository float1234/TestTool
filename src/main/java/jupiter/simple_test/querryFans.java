package jupiter.simple_test;


import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.RequestBodyEntity;
import jupiter.simple_test.util.CSVUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Created by dextry on 2017/5/16.
 */
public class querryFans {
    private   HashMap<Integer, String> companyMap;
    private String endTime;
    private String startTime;
    private int companyid;
    private Token token;
    String downloadPath = "C:\\QueryGrid";
    String fileName ;
     {
        HashMap<Integer, String> companyMap = new HashMap();
        companyMap.put(4,"pampers");
        companyMap.put(5,"oralb");
        companyMap.put(6,"braun");
        companyMap.put(7,"pantene");
        companyMap.put(8,"head-shoulders");
        companyMap.put(9,"rejoice");
        companyMap.put(100,"skii");
        companyMap.put(101,"olay");
        companyMap.put(102,"vs");
        companyMap.put(103,"tampax");
        companyMap.put(105,"gillette");
        companyMap.put(106,"ariel");
        companyMap.put(107,"tide");
        companyMap.put(108,"whisper");
        companyMap.put(109,"OlayPCC");
        companyMap.put(110,"safeguard");
        companyMap.put(111,"olayprox");
        companyMap.put(20,"cnla");
        this.companyMap = companyMap;
    }


    public querryFans(String[] args) {
        this.startTime = args[0];
        this.endTime = args[1];
        this.companyid = Integer.parseInt(args[2]);

        this.fileName = args[0]+"—"+args[1] + "—"+this.companyMap.get(companyid);


        if (org.apache.commons.lang3.StringUtils.isBlank(this.startTime)) {
            throw new RuntimeException("startTime 没有配置");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(this.endTime)) {
            throw new RuntimeException("endTime 没有配置");
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        querryFans querryFans = new querryFans(args);
        try {
            querryFans.countAll();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

    protected void countAll() throws SQLException, IOException, UnirestException {
        this.token = new Token(companyid);
        //1. getusersummary
        this.getusersummary();
        //2 email out.
        System.out.println("\n// email to somebody...");
        String[] parameters = new String[]{
                downloadPath, fileName
        };
        JavaMailWithAttachment.main(parameters);

    }


    private void getusersummary() {

        try {
            JSONObject object = new JSONObject();
            object.put("begin_date", this.startTime);
            object.put("end_date", this.endTime);
            RequestBodyEntity body = Unirest.post("https://api.weixin.qq.com/datacube/getusersummary")
                    .queryString("access_token", this.token.getValue()).body(object);
            RequestBodyEntity body2 = Unirest.post("https://api.weixin.qq.com/datacube/getusercumulate")
                    .queryString("access_token", this.token.getValue()).body(object);

            HttpResponse<JsonNode> response = body.asJson();
            HttpResponse<JsonNode> response2 = body2.asJson();
            if (response.getBody().toString().contains("errcode")){
                System.out.println(response.getBody().toString());
            }
            String listStr = response.getBody().getObject().get("list").toString();

            if (response2.getBody().toString().contains("errcode")){
                System.out.println(response.getBody().toString());
            }
            String listStr2 = response2.getBody().getObject().get("list").toString();
            System.out.println(listStr);
            System.out.println(listStr2);

            if (listStr != null) {

                List<dayInfoBean> dayInfoBeanArr = JSON.parseArray(listStr, dayInfoBean.class);
                List<dayinfocumulateBean> dayinfoCumulateArr = JSON.parseArray(listStr2, dayinfocumulateBean.class);
                ArrayList<Object> head = new ArrayList<Object>();
                head.add("时间");
                head.add("新关注人数");
                head.add("取消关注人数");
                head.add("净关注人数");
                head.add("累积注人数");

                Map<String, Integer> count = new LinkedHashMap();
                for (dayInfoBean infobean : dayInfoBeanArr) {
                    int c = count.containsKey(infobean.getRef_date()) ? count.get(infobean.getRef_date()) : 0;
                    count.put(infobean.getRef_date(), c + 1);
                }

                List<List<Object>> arrayLists = new ArrayList<List<Object>>();
                int base = 0;
                int j = 0;
                for (Map.Entry<String, Integer> m : count.entrySet()) {
                    int new_user = 0;
                    int cancel_user = 0;
                    String ref_date = null;

                    for (int i = 0; i < dayInfoBeanArr.size(); i++) {
                        if (i >= base && i < base + m.getValue()) {
                            dayInfoBean dayInfoBean = dayInfoBeanArr.get(i);
                            new_user += dayInfoBean.getNew_user();
                            cancel_user += dayInfoBean.getCancel_user();
                            ref_date = dayInfoBean.getRef_date();
                        }
                    }
                    base += m.getValue();
                    ArrayList<Object> content = new ArrayList<Object>();
                    content.add(ref_date);
                    content.add(new_user);
                    content.add(cancel_user);
                    content.add(new_user - cancel_user);
                    content.add(dayinfoCumulateArr.get(j++).getCumulate_user());

                    arrayLists.add(content);

                }


                CSVUtils.createCSVFile(head, arrayLists, downloadPath, fileName);
                System.out.println(Calendar.getInstance().getTime() + " - " + "write ");

            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

}
