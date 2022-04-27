package service;

import dao.DataDaoImpl;
import entity.TodoInfo;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class TodoInfoImpl {
    private static final SimpleDateFormat sdfday = new SimpleDateFormat("MM/dd/yyyy");

    public static List<TodoInfo> loadEntity(String path){
        List<TodoInfo> todoInfoList = DataDaoImpl.loadEntity(path);
        return todoInfoList;
    }

    public static void saveEntity(List<TodoInfo>  todoInfoList, String path){
        int result = DataDaoImpl.saveEntity(todoInfoList, path);
    }

    public static List<TodoInfo> search(String keywords, String type, String date, String category, String path){
        System.out.println("keywords:"+keywords+" type:"+type+" date:"+date+" category:"+category);
        List<TodoInfo> todoInfoList = DataDaoImpl.loadEntity(path);
        List<TodoInfo> resultList1 = new ArrayList<>();
        if(!StringUtils.isEmpty(keywords)){
            for(int i = 0; i < todoInfoList.size(); i++){
                if(todoInfoList.get(i).getContent().contains(keywords)){
                    resultList1.add(todoInfoList.get(i));
                }
            }
        }else {
            resultList1 = todoInfoList;
        }

        List<TodoInfo> resultList2 = new ArrayList<>();
        if(!StringUtils.isEmpty(type)){
            if(type.equals("Today")){
                for(int i = 0; i < resultList1.size(); i++){
                    if(sdfday.format(resultList1.get(i).getRemindTime()).equals(sdfday.format(new Date()))){
                        resultList2.add(todoInfoList.get(i));
                    }
                }
            }else if(type.equals("Scheduled")) {
                for(int i = 0; i < resultList1.size(); i++){
                    if(resultList1.get(i).getRemindTime() > new Date().getTime() ){
                        resultList2.add(todoInfoList.get(i));
                    }
                }
            }else if(type.equals("All")){
                resultList2 = resultList1;
            }
        } else {
            if(!StringUtils.isEmpty(date)){
                for(int i = 0; i < resultList1.size(); i++){
                    try {
                        if(sdfday.format(new Date(resultList1.get(i).getRemindTime())).equals(sdfday.format(sdfday.parse(date)))){
                            resultList2.add(todoInfoList.get(i));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                resultList2 = resultList1;
            }
        }

        List<TodoInfo> resultList3 = new ArrayList<>();
        if(!StringUtils.isEmpty(category)){
            for(int i = 0; i < resultList2.size(); i++){
                if(category.equals(resultList2.get(i).getCategory())){
                    resultList3.add(resultList2.get(i));
                }
            }
        }else{
            resultList3 = resultList2;
        }

        return resultList3;
    }

    public static DefaultListModel resetList(List<TodoInfo> todoInfoList){
        Date date = new Date();

        DefaultListModel defaultListModel = new DefaultListModel();
        for (int i = 0; i < todoInfoList.size(); i++) {
            //auto delay
            if (todoInfoList.get(i).getCheckoffStatus() != 1 && todoInfoList.get(i).getAutoDelaySign() == 1) {
                //check time
                try {
                    if(todoInfoList.get(i).getRemindTime() < sdfday.parse(sdfday.format(date.getTime())).getTime()){
                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(date);
                        calendar.add(calendar.DATE,+7);
                        todoInfoList.get(i).setRemindTime(calendar.getTime().getTime());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE,-1);
            String listElement = "";
            if (todoInfoList.get(i).getCheckoffStatus() == 1) {
                listElement += "   √   (";
            } else if (todoInfoList.get(i).getRemindTime() < calendar.getTime().getTime()) {
                listElement += "   ×    (";
            } else {
                listElement += "   o    (";
            }
            listElement += sdfday.format(todoInfoList.get(i).getRemindTime()) + ")   ";
            listElement += String.format("%-80s", todoInfoList.get(i).getContent()); //.substring(10)
            defaultListModel.addElement(listElement);

        }
        return defaultListModel;
    }

}
