package jupiter.simple_test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;

/**
 * Created by theone on 2016/12/9.
 */
public class Token {
    private String value;
    private int companyid;
    private   HashMap<Integer, String> brandidMap;
    private   HashMap<Integer, String> apiMap;
    {
        HashMap<Integer, String> brandidMap = new HashMap();
        brandidMap.put(4,"874fb164b7f968c734d3ca2b1ca1c479");
        brandidMap.put(5,"4874176a08afeece3970865565ee28c3");
        brandidMap.put(6,"bf8bf60ab8a618063cc7108a1d1a6195");
        brandidMap.put(7,"6af4851541c815b8a672f1990d3d8aea");
        brandidMap.put(8,"25300e6fdfe5544c88eec211f0007829");
        brandidMap.put(9,"a582eec70315f53b96f202ee62744e67");
        brandidMap.put(100,"37870250e9f7872dea6a834439c5ff35");
        brandidMap.put(101,"56a39c8504e4c7e2741f86f55c4de116");
        brandidMap.put(102,"b770dab9462a2a91d46dcc39107c22f4");
        brandidMap.put(103,"1231377affefb5fb55e4ca74bba86697");
        brandidMap.put(105,"98135f8417b2f3ecb51ab8b173a5b337");
        brandidMap.put(106,"40a05386d966f96ba060c4d7d4fb76ac");
        brandidMap.put(107,"1c362932279632647f8eaf6d0004b99f");
        brandidMap.put(108,"7864243bac79dfe305829b4762651c7c");
        brandidMap.put(109,"d6e1fd46b1d501d109cb8b1dcae27c66");
        brandidMap.put(110,"60edd17c0b52fc62f413503251edc85a");
        brandidMap.put(111,"d0217ddec1aad4742dc1c127deac7874");
        brandidMap.put(20,"5d0b6dbb6bf82a6912528f79b327751c");

        HashMap<Integer, String> apiMap = new HashMap();
        apiMap.put(4,"ad293bb8429ab9e516807ea5e9f93d56");
        apiMap.put(5,"05ae9c40001ba0c124c1e62804e8453b");
        apiMap.put(6,"bfd45887b43ef8354e85c51c0d2e59b1");
        apiMap.put(7,"cd1a84404e2bc65eb966dcbb066edfea");
        apiMap.put(8,"ff0ced1fad8558b02293e1cf84b41feb");
        apiMap.put(9,"fcb01148e5f0aae6d1c69da820d263e0");
        apiMap.put(100,"e6b02ed935c8768605d15abfedf212f7");
        apiMap.put(101,"d6f682cb1c885db0b2c6d3ff1bf929d7");
        apiMap.put(102,"12e5db8928efc4adddcda6ec9251580d");
        apiMap.put(103,"ef3075e52d8a57baeaea81b46a11bdcd");
        apiMap.put(105,"5a31bc053eee6e61bc49d67e90a9011d");
        apiMap.put(106,"a976acebf0f95991a4c5c9891f7f1a3b");
        apiMap.put(107,"47451bc901a2b4b174fe991b680ca36b");
        apiMap.put(108,"7aee061091263356f5b191ec48c9717f");
        apiMap.put(109,"3f1a00ad6517e0598869676be655a7d6");
        apiMap.put(110,"8e01767d6cb5affa3c459d209c586649");
        apiMap.put(111,"b6d53049a762a28d5a05575d8ebe6e14");
        apiMap.put(20,"e8ca2c1ccb40b7affc92d8f44747d9e8");

        this.brandidMap = brandidMap;
        this.apiMap = apiMap;
    }

    public Token(int companyid) throws UnirestException {
        String brandid = this.brandidMap.get(companyid);
        String apisecret = this.apiMap.get(companyid);
       this.companyid = companyid;
        this.newToken(brandid, apisecret);
    }

    public synchronized String getValue() {
        return this.value;
    }

    public synchronized void renew(String oldValue) throws UnirestException {
        if (this.value.equals(oldValue)) {
            this.newToken(brandidMap.get(companyid),apiMap.get(companyid));
        }
    }

    private void newToken(String brandid,String apiSecret) throws UnirestException {
        Long timestamp = Long.valueOf((new Date()).getTime() / 1000L);
        String nonce= getFixLenthString(6);
        ArrayList list = new ArrayList();
        list.add(apiSecret);
        list.add(brandid);
        list.add(String.valueOf(timestamp));
        list.add(nonce);
        String sha1str = this.encrypt(list);
        System.out.println("PROURL: http://acl.pg.com.cn/acl/api/token?brandId=" + brandid + "&signature=" + sha1str + "&timestamp=" + timestamp + "&nonce=" + nonce);
        HttpResponse<JsonNode> response = Unirest.get("http://acl.pg.com.cn/acl/api/token?brandId=" + brandid + "&signature=" + sha1str + "&timestamp=" + timestamp + "&nonce=" + nonce)
                .asJson();
        System.out.println(response.getBody().getObject().toString());
        this.value = response.getBody().getObject().getString("access_token");
        System.out.println(this.getValue());
    }
    public static String encrypt(List<String> list) {
        Collections.sort(list);
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append(s);
        }
        String encryptString = DigestUtils.sha1Hex(sb.toString());
        return encryptString;
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
}
