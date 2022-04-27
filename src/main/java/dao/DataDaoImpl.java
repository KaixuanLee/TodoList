package dao;

import entity.TodoInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DataDaoImpl {

    private static Class<? extends entity.TodoInfo> TodoInfo;

    public static int saveEntity(List<TodoInfo> todoInfoList, String path){
        System.out.println(path);
        int success = 0;
        String jsonStr="";
        try {
            jsonStr += objectToJson(todoInfoList);
            if(!StringUtils.isEmpty(path)){
                success = saveToFile(jsonStr,path);
            }else {
                success = saveToFile(jsonStr,"todoSave.txt");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static List<TodoInfo> loadEntity(String path) {
        System.out.println(path);
        List<TodoInfo> todoInfoList = new ArrayList<>();
        String jsonLIstString = "";
        try {
            if(!StringUtils.isEmpty(path)){
                jsonLIstString = readFromFile(path);
            }else {
                jsonLIstString = readFromFile("todoSave.txt");
            }

            todoInfoList = jsonToObject(jsonLIstString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todoInfoList;
    }

    public static int saveToFile(String data, String path) throws IOException {
        int success = 0;
        File file = new File(path);
        if (!file.exists())
            file.createNewFile();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
            success = 1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static String readFromFile(String path) throws IOException {
        String readData = "";
        FileInputStream fis = new FileInputStream(path);
        int size=0;
        byte[] buffer = new byte[1024];
        while((size=fis.read(buffer))!=-1){
            readData += new String(buffer, 0, size);
        }
        fis.close();
        return readData;
    }

    public static String objectToJson(List<TodoInfo> infoList) throws IOException {
        JSONArray jsons = new JSONArray();
        for(int i=0;i<infoList.size();i++){
            // Convert object to JSON string
            JSONObject jsonobject = JSONObject.fromObject(infoList.get(i));
            jsons.add(jsonobject);
        }
        return jsons.toString();
    }

    public static List<TodoInfo> jsonToObject(String jsonData) throws IOException {
        List<TodoInfo> todoInfoList = new ArrayList<>();
        // Convert JSON string to object
//        System.out.println(jsonData);
        JSONArray jsons = JSONArray.fromObject(jsonData);

        if(jsons.size()>0) {
            for (int i = 0; i < jsons.size(); i++) {
                JSONObject json = jsons.getJSONObject(i);
                TodoInfo todoInfo = (TodoInfo) JSONObject.toBean(json, TodoInfo.class);
                todoInfoList.add(todoInfo);
            }
        }
        return todoInfoList;
    }

}
