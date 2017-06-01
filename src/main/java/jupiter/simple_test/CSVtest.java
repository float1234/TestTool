package jupiter.simple_test;

import jupiter.simple_test.util.CSVUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dextry on 2017/4/21.
 */
public class CSVtest {
    public static void main(String[] args) {
        ArrayList<Object> head = new ArrayList<Object>();
        head.add("场景类型");
        head.add("场景值");
        head.add("sceneID");
        head.add("会员数量");
        ArrayList<Object> content = new ArrayList<Object>();
        content.add("场景类型");
        content.add("场景值");
        content.add("sceneID");
        content.add("会员数量");
        List<List<Object>> arrayLists = new ArrayList<List<Object>>();
        arrayLists.add(content);
        String outputPath = "C:\\operationfiles";
        String fileName = "testcsv";



    CSVUtils.createCSVFile(head,arrayLists,outputPath,fileName);
}
}
