package util;

import entity.TodoInfo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyRenderer extends DefaultListCellRenderer {

        private Font font1;
        private List<TodoInfo> todoInfoList;

        public MyRenderer() {
            this.font1 = getFont();
        }

        public MyRenderer(List<TodoInfo> todoInfoList) {
            this.todoInfoList = todoInfoList;
        }


        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            DefaultListCellRenderer result = (DefaultListCellRenderer) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if(todoInfoList.get(index).getCategory().equals("Important")){
                result.setForeground(new Color(220, 20, 60));
            }else if(todoInfoList.get(index).getCategory().equals("General")){
                result.setForeground(new Color(0, 0, 255));
            }else if(todoInfoList.get(index).getCategory().equals("Company")){
                result.setForeground(new Color(255, 182, 193));
            }else if(todoInfoList.get(index).getCategory().equals("Family")){
                result.setForeground(new Color(255, 140, 0));
            }else if(todoInfoList.get(index).getCategory().equals("Campus")){
                result.setForeground(new Color(60, 179, 113));
            }else{
                result.setForeground(Color.BLACK);
            }
            return result;
        }

}
