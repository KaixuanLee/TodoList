import entity.CalendarBean;
import entity.TodoInfo;
import org.apache.commons.lang.StringUtils;
import service.TodoInfoImpl;
import util.MyRenderer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TodoList {
    private JPanel rootPanel;
    private JPanel ToDoList;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField searchTextField;
    private JButton scheduledB;
    private JButton todayB;
    private JButton allB;
    private JTextArea detailTextArea;
    private JButton checkB;
    private JButton deleteB;
    private JList todoList;
    private JLabel myTodoListLabel;
    private JLabel detailLabel;
    private JLabel calendarLabel;
    private JLabel searchLabel;
    private JLabel categoryListLabel;
    private JButton saveB;
    private JButton newB;
    private JPanel calendarPanel;
    private JButton preMonthB;
    private JButton nextMonthB;
    private JLabel calendarDetailLable;
    private JScrollPane detailScrollP;
    private JTextField timeTF;
    private JLabel timeL;
    private JScrollPane todoListJSP;
    private JTextField detailTF;
    private JTextField categoryTF;
    private JCheckBox autoDelay1DayCB;
    private JRadioButton categoryRB1;
    private JRadioButton categoryRB2;
    private JRadioButton categoryRB3;
    private JRadioButton categoryRB4;
    private JPanel catelist;
    private JRadioButton categoryRB5;
    private JButton fileButton;

    private static final SimpleDateFormat sdfdate = new SimpleDateFormat("dd");
    private static final SimpleDateFormat sdfmonth = new SimpleDateFormat("MM");
    private static final SimpleDateFormat sdfyear = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat sdfday = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");


    static int year = 2022;
    static int month = 4;
    static String searchType = "";
    DefaultListModel listModel = new DefaultListModel();
    String filePath = "";
    List<TodoInfo> todoInfoList;

    public TodoList() {
        // init datatime
        Date date = new Date();
        year = Integer.parseInt(sdfyear.format(date));
        month = Integer.parseInt(sdfmonth.format(date));
        // init categoryList
        ButtonGroup group = new ButtonGroup();
        group.add(categoryRB1);
        group.add(categoryRB2);
        group.add(categoryRB3);
        group.add(categoryRB4);
        group.add(categoryRB5);
        // init category
//        categoryTF.setText(categoryRB1.getText());
        // init data
        todoInfoList = TodoInfoImpl.loadEntity("");
        // init todoList panel
        listModel = TodoInfoImpl.resetList(todoInfoList);
        todoList.setModel(listModel);
        todoList.setCellRenderer(new MyRenderer(todoInfoList));

        calendarPanel.setLayout(new GridLayout(7, 7));
        JButton labelDay[] = new JButton[42];
        JLabel titleName[] = new JLabel[7];
        String name[] = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        CalendarBean calendar;
        for (int i = 0; i < 7; i++) {
            titleName[i] = new JLabel(name[i], JLabel.CENTER);
            calendarPanel.add(titleName[i]);
        }
        for (int i = 0; i < 42; i++) {
            labelDay[i] = new JButton("");
            calendarPanel.add(labelDay[i]);
        }
        calendar = new CalendarBean();
        calendar.setYear(year);
        calendar.setMonth(month);
        String day[] = calendar.getCalendar();
        for (int i = 0; i < 42; i++) {
            labelDay[i].setText(day[i]);
            labelDay[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    System.out.println(e.getActionCommand());
                    if (e.getActionCommand() != null && e.getActionCommand() != "") {
                        for (int i = 0; i < 42; i++) {
                            if (labelDay[i].getText() == e.getActionCommand()) {
                                labelDay[i].setBackground(new Color(100, 160, 240));
                                String dateString = month + "/" + e.getActionCommand() + "/" + year;
                                try {
                                    timeTF.setText(sdfday.format(sdfday.parse(dateString)));
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                }
                                detailTF.setText("");
                                detailTextArea.setText("");
                                searchType = "";
                                //search
                                List<TodoInfo> todoInfoListDate = TodoInfoImpl.search(searchTextField.getText(), searchType, timeTF.getText(), categoryTF.getText(), filePath);
                                DefaultListModel listModelDate = TodoInfoImpl.resetList(todoInfoListDate);
                                todoList.setModel(listModelDate);
                                todoList.setCellRenderer(new MyRenderer(todoInfoListDate));
                            } else {
                                labelDay[i].setBackground(null);
                            }
                        }
                    }
                }
            });
        }
        calendarDetailLable.setText(calendar.getMonth() + " / " + calendar.getYear());

        preMonthB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                month = month - 1;
                if (month < 1) {
                    month = 12;
                    year = year - 1;
                }
                calendar.setMonth(month);
                calendar.setYear(year);
                String day[] = calendar.getCalendar();
                for (int i = 0; i < 42; i++) {
                    labelDay[i].setText(day[i]);
                    labelDay[i].setBackground(null);
                }
                calendarDetailLable.setText(calendar.getMonth() + " / " + calendar.getYear());
            }
        });
        nextMonthB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                month = month + 1;
                if (month > 12) {
                    month = 1;
                    year = year + 1;
                }
                calendar.setMonth(month);
                calendar.setYear(year);
                String day[] = calendar.getCalendar();

                for (int i = 0; i < 42; i++) {
                    labelDay[i].setText(day[i]);
                    labelDay[i].setBackground(null);
                }
                calendarDetailLable.setText(calendar.getMonth() + " / " + calendar.getYear());
            }
        });

        // save item button listener
        saveB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TodoInfo todoInfo;
                // RemindTime check
                if (StringUtils.isBlank(timeTF.getText())) {
                    JOptionPane.showMessageDialog(null, "Please choose Remind Time");
                    return;
                }
                if (StringUtils.isBlank(detailTF.getText())) {
                    JOptionPane.showMessageDialog(null, "Please add Remind Content");
                    return;
                }
                if (StringUtils.isBlank(categoryTF.getText())) {
                    JOptionPane.showMessageDialog(null, "Please choose a Category");
                    return;
                }

                Integer autoDelaySign = 0;
                if (todoList.getSelectedIndex() >= 0) { //update
                    todoInfo = todoInfoList.get(todoList.getSelectedIndex());
                    todoInfo.setContent(detailTF.getText());
                    todoInfo.setDescriptions(detailTextArea.getText());
                    //todoInfo.setRemindTime(sdfday.parse(timeTF.getText()).getTime());
                    if (autoDelay1DayCB.isSelected()) {
                        autoDelaySign = 1;
                    }
                    todoInfo.setAutoDelaySign(autoDelaySign);
                    todoInfo.setCategory(categoryTF.getText());
                    todoInfo.setUpdateTime(System.currentTimeMillis());
                } else { // create
                    todoInfo = new TodoInfo();
                    todoInfo.setContent(detailTF.getText());
                    todoInfo.setDescriptions(detailTextArea.getText());
                    try {
                        System.out.println(timeTF.getText());
                        System.out.println(sdfday.parse(timeTF.getText()));
                        todoInfo.setRemindTime(sdfday.parse(timeTF.getText()).getTime());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    if (autoDelay1DayCB.isSelected()) {
                        autoDelaySign = 1;
                    }
                    todoInfo.setAutoDelaySign(autoDelaySign);
                    todoInfo.setCategory(categoryTF.getText());
                    todoInfo.setCheckoffStatus(0);
                    todoInfo.setCreateTime(System.currentTimeMillis());
                    todoInfoList.add(todoInfo);
                }
                TodoInfoImpl.saveEntity(todoInfoList, filePath);
                // clear
                detailTF.setText("");
                detailTextArea.setText("");
                timeTF.setText("");
                for (int i = 0; i < 42; i++) {
                    labelDay[i].setBackground(null);
                }
                JOptionPane.showMessageDialog(null, "Save Item Success");
                // init
                listModel = TodoInfoImpl.resetList(todoInfoList);
                todoList.setModel(listModel);
                todoList.setCellRenderer(new MyRenderer(todoInfoList));
            }
        });

        // create item button listener
        newB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                todoList.clearSelection();
                detailTF.setText("");
                detailTextArea.setText("");
                timeTF.setText("");
                for (int i = 0; i < 42; i++) {
                    labelDay[i].setBackground(null);
                }
            }
        });

        //todoList listener
        todoList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectIndex = todoList.getSelectedIndex();
                //System.out.println(selectIndex);
                if (selectIndex >= 0) {
                    TodoInfo todoInfo = todoInfoList.get(selectIndex);
                    detailTF.setText(todoInfo.getContent());
                    detailTextArea.setText(todoInfo.getDescriptions());
                    timeTF.setText(sdfday.format(new Date(todoInfo.getRemindTime())));
                    categoryTF.setText(todoInfo.getCategory());
                    if (todoInfo.getAutoDelaySign() == 1) {
                        autoDelay1DayCB.setSelected(true);
                    } else {
                        autoDelay1DayCB.setSelected(false);
                    }
                    //System.out.println(todoInfoList.get(selectIndex).getContent());
                }
            }
        });

        //delete item button listener
        deleteB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(todoList.getSelectedIndex());
                int delIndex = todoList.getSelectedIndex();
                if (delIndex >= 0) {
                    todoInfoList.remove(delIndex);
                    TodoInfoImpl.saveEntity(todoInfoList, filePath);
                    listModel.remove(delIndex);
                    todoList.clearSelection();
                    detailTextArea.setText("");
                    JOptionPane.showMessageDialog(null, "Delete Item Success");

                }
            }
        });

        // check part
        checkB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TodoInfo todoInfo = todoInfoList.get(todoList.getSelectedIndex());
                if (todoInfo.getCheckoffStatus() == 0) {
                    todoInfo.setCheckoffStatus(1);
                    TodoInfoImpl.saveEntity(todoInfoList, filePath);
                    listModel.setElementAt("   √   " + timeTF.getText() + "   " + todoInfo.getContent(), todoList.getSelectedIndex());
                }
            }
        });

        // search part
        // text search
        searchTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e);
                //search
                List<TodoInfo> todoInfoListDate = TodoInfoImpl.search(searchTextField.getText(), searchType, timeTF.getText(), categoryTF.getText(), filePath);
                DefaultListModel listModelDate = TodoInfoImpl.resetList(todoInfoListDate);
                todoList.setModel(listModelDate);
                todoList.setCellRenderer(new MyRenderer(todoInfoListDate));
            }

        });

        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                System.out.println(searchTextField.getText());
                //search
                List<TodoInfo> todoInfoListDate = TodoInfoImpl.search(searchTextField.getText(), searchType, timeTF.getText(), categoryTF.getText(), filePath);
                DefaultListModel listModelDate = TodoInfoImpl.resetList(todoInfoListDate);
                todoList.setModel(listModelDate);
                todoList.setCellRenderer(new MyRenderer(todoInfoListDate));
            }
        });

        // category search
        categoryRB1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                categoryTF.setText(categoryRB1.getText());
                //search
                List<TodoInfo> todoInfoListDate = TodoInfoImpl.search(searchTextField.getText(), searchType, timeTF.getText(), categoryTF.getText(), filePath);
                DefaultListModel listModelDate = TodoInfoImpl.resetList(todoInfoListDate);
                todoList.setModel(listModelDate);
                todoList.setCellRenderer(new MyRenderer(todoInfoListDate));
            }
        });
        categoryRB2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                categoryTF.setText(categoryRB2.getText());
                //search
                List<TodoInfo> todoInfoListDate = TodoInfoImpl.search(searchTextField.getText(), searchType, timeTF.getText(), categoryTF.getText(), filePath);
                DefaultListModel listModelDate = TodoInfoImpl.resetList(todoInfoListDate);
                todoList.setModel(listModelDate);
                todoList.setCellRenderer(new MyRenderer(todoInfoListDate));
            }
        });
        categoryRB3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                categoryTF.setText(categoryRB3.getText());
                //search
                List<TodoInfo> todoInfoListDate = TodoInfoImpl.search(searchTextField.getText(), searchType, timeTF.getText(), categoryTF.getText(), filePath);
                DefaultListModel listModelDate = TodoInfoImpl.resetList(todoInfoListDate);
                todoList.setModel(listModelDate);
                todoList.setCellRenderer(new MyRenderer(todoInfoListDate));
            }
        });
        categoryRB4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                categoryTF.setText(categoryRB4.getText());
                //search
                List<TodoInfo> todoInfoListDate = TodoInfoImpl.search(searchTextField.getText(), searchType, timeTF.getText(), categoryTF.getText(), filePath);
                DefaultListModel listModelDate = TodoInfoImpl.resetList(todoInfoListDate);
                todoList.setModel(listModelDate);
                todoList.setCellRenderer(new MyRenderer(todoInfoListDate));
            }
        });
        categoryRB5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                categoryTF.setText(categoryRB5.getText());
                //search
                List<TodoInfo> todoInfoListDate = TodoInfoImpl.search(searchTextField.getText(), searchType, timeTF.getText(), categoryTF.getText(), filePath);
                DefaultListModel listModelDate = TodoInfoImpl.resetList(todoInfoListDate);
                todoList.setModel(listModelDate);
                todoList.setCellRenderer(new MyRenderer(todoInfoListDate));
            }
        });

        //search button
        scheduledB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchType = "Scheduled";
                timeTF.setText("");
                for (int i = 0; i < 42; i++) {
                    labelDay[i].setBackground(null);
                }
                //search
                List<TodoInfo> todoInfoListDate = TodoInfoImpl.search(searchTextField.getText(), searchType, timeTF.getText(), "", filePath);
                DefaultListModel listModelDate = TodoInfoImpl.resetList(todoInfoListDate);
                todoList.setModel(listModelDate);
                todoList.setCellRenderer(new MyRenderer(todoInfoListDate));
            }
        });
        allB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchType = "All";
                todoList.clearSelection();
                detailTextArea.setText("");
                searchTextField.setText("");
                detailTF.setText("");
                detailTextArea.setText("");
                timeTF.setText("");
                searchType = "";
                timeTF.setText("");
                categoryTF.setText("");
                group.clearSelection();
                for (int i = 0; i < 42; i++) {
                    labelDay[i].setBackground(null);
                }
//                group.setSelected(categoryRB1.getModel(), true);
                //search
                List<TodoInfo> todoInfoListDate = TodoInfoImpl.search("", searchType, "", "", filePath);
                DefaultListModel listModelDate = TodoInfoImpl.resetList(todoInfoListDate);
                todoList.setModel(listModelDate);
                todoList.setCellRenderer(new MyRenderer(todoInfoListDate));
            }
        });
        todayB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchType = "Today";
                String todayStr = sdfday.format(date);
                String days = sdfdate.format(date);
                timeTF.setText(todayStr);

                // reset calendar
                year = Integer.parseInt(sdfyear.format(date));
                month = Integer.parseInt(sdfmonth.format(date));
                calendar.setMonth(month);
                calendar.setYear(year);
                String day[] = calendar.getCalendar();
                for (int i = 0; i < 42; i++) {
                    labelDay[i].setText(day[i]);
                    if (!StringUtils.isEmpty(labelDay[i].getText()) && Integer.parseInt(labelDay[i].getText()) == Integer.parseInt(days)) {
                        labelDay[i].setBackground(new Color(100, 160, 240));
                    } else {
                        labelDay[i].setBackground(null);
                    }
                }
                calendarDetailLable.setText(calendar.getMonth() + " / " + calendar.getYear());
                //search
                List<TodoInfo> todoInfoListDate = TodoInfoImpl.search(searchTextField.getText(), searchType, "", "", filePath);
                DefaultListModel listModelDate = TodoInfoImpl.resetList(todoInfoListDate);
                todoList.setModel(listModelDate);
                todoList.setCellRenderer(new MyRenderer(todoInfoListDate));
            }
        });

        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileChooser.showDialog(new JLabel(), "choose file");
                File file = fileChooser.getSelectedFile();
                System.out.println(file);
                if (file != null && !StringUtils.isEmpty(file.getPath())) {
                    filePath = file.getPath();
                    todoInfoList = TodoInfoImpl.loadEntity(filePath);
                    listModel = TodoInfoImpl.resetList(todoInfoList);
                    todoList.setModel(listModel);
                    todoList.setCellRenderer(new MyRenderer(todoInfoList));
                }


//                int delIndex = todoList.getSelectedIndex();
//                todoInfoList.remove(delIndex);
//                listModel.remove(delIndex);
                todoList.clearSelection();
                detailTextArea.setText("");
                searchTextField.setText("");
                detailTF.setText("");
                detailTextArea.setText("");
                timeTF.setText("");
                searchType = "";
//                group.setSelected(categoryRB1.getModel(), true);
                categoryTF.setText(categoryRB1.getText());
                for (int i = 0; i < 42; i++) {
                    labelDay[i].setBackground(null);
                }
            }
        });


    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("todoList");
        frame.setContentPane(new TodoList().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
//        frame.setResizable(false);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.setMaximumSize(new Dimension(750, 700));
        rootPanel.setMinimumSize(new Dimension(750, 700));
        rootPanel.setOpaque(false);
        rootPanel.setPreferredSize(new Dimension(750, 700));
        ToDoList = new JPanel();
        ToDoList.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(ToDoList, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ToDoList.setBorder(BorderFactory.createTitledBorder(null, "ToDoList", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        leftPanel = new JPanel();
        leftPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(14, 3, new Insets(0, 0, 0, 0), -1, -1));
        ToDoList.add(leftPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchTextField = new JTextField();
        searchTextField.setText("");
        searchTextField.setToolTipText("search");
        leftPanel.add(searchTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchLabel = new JLabel();
        searchLabel.setText("Search");
        leftPanel.add(searchLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        calendarLabel = new JLabel();
        calendarLabel.setText("Calendar");
        leftPanel.add(calendarLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        calendarPanel = new JPanel();
        calendarPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        leftPanel.add(calendarPanel, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        preMonthB = new JButton();
        preMonthB.setText("Prev");
        leftPanel.add(preMonthB, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nextMonthB = new JButton();
        nextMonthB.setText("Next");
        leftPanel.add(nextMonthB, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        categoryListLabel = new JLabel();
        categoryListLabel.setText("Category List");
        leftPanel.add(categoryListLabel, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        calendarDetailLable = new JLabel();
        calendarDetailLable.setHorizontalAlignment(0);
        calendarDetailLable.setHorizontalTextPosition(0);
        calendarDetailLable.setText("Label");
        leftPanel.add(calendarDetailLable, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        categoryRB1 = new JRadioButton();
        categoryRB1.setForeground(new Color(-2354116));
        categoryRB1.setSelected(false);
        categoryRB1.setText("Important");
        leftPanel.add(categoryRB1, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        categoryRB2 = new JRadioButton();
        categoryRB2.setForeground(new Color(-16776961));
        categoryRB2.setText("General");
        leftPanel.add(categoryRB2, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        categoryRB3 = new JRadioButton();
        categoryRB3.setForeground(new Color(-18751));
        categoryRB3.setText("Company");
        leftPanel.add(categoryRB3, new com.intellij.uiDesigner.core.GridConstraints(10, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        categoryRB4 = new JRadioButton();
        categoryRB4.setForeground(new Color(-29696));
        categoryRB4.setText("Family");
        leftPanel.add(categoryRB4, new com.intellij.uiDesigner.core.GridConstraints(11, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        catelist = new JPanel();
        catelist.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        leftPanel.add(catelist, new com.intellij.uiDesigner.core.GridConstraints(13, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        categoryRB5 = new JRadioButton();
        categoryRB5.setForeground(new Color(-12799119));
        categoryRB5.setText("Campus");
        leftPanel.add(categoryRB5, new com.intellij.uiDesigner.core.GridConstraints(12, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scheduledB = new JButton();
        scheduledB.setText("Scheduled");
        leftPanel.add(scheduledB, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        todayB = new JButton();
        todayB.setText("Today");
        leftPanel.add(todayB, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        allB = new JButton();
        allB.setText("All");
        leftPanel.add(allB, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileButton = new JButton();
        fileButton.setText("Open File");
        leftPanel.add(fileButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightPanel = new JPanel();
        rightPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(13, 4, new Insets(0, 0, 0, 0), -1, -1));
        ToDoList.add(rightPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        detailLabel = new JLabel();
        detailLabel.setText("Detail");
        rightPanel.add(detailLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(74, 16), null, 0, false));
        myTodoListLabel = new JLabel();
        myTodoListLabel.setText("My Todo List");
        rightPanel.add(myTodoListLabel, new com.intellij.uiDesigner.core.GridConstraints(11, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(74, 16), null, 0, false));
        detailScrollP = new JScrollPane();
        rightPanel.add(detailScrollP, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 5, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(250, 150), null, 0, false));
        detailTextArea = new JTextArea();
        detailTextArea.setAutoscrolls(true);
        detailTextArea.setMinimumSize(new Dimension(-1, -1));
        detailScrollP.setViewportView(detailTextArea);
        todoListJSP = new JScrollPane();
        rightPanel.add(todoListJSP, new com.intellij.uiDesigner.core.GridConstraints(12, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        todoList = new JList();
        todoList.setSelectionMode(0);
        todoListJSP.setViewportView(todoList);
        detailTF = new JTextField();
        rightPanel.add(detailTF, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Descriptions");
        rightPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        newB = new JButton();
        newB.setText("New");
        rightPanel.add(newB, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        timeL = new JLabel();
        timeL.setRequestFocusEnabled(true);
        timeL.setText("Scheduled Day:");
        rightPanel.add(timeL, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        timeTF = new JTextField();
        timeTF.setEditable(false);
        timeTF.setEnabled(true);
        rightPanel.add(timeTF, new com.intellij.uiDesigner.core.GridConstraints(5, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        categoryTF = new JTextField();
        categoryTF.setEditable(false);
        rightPanel.add(categoryTF, new com.intellij.uiDesigner.core.GridConstraints(8, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Category:");
        rightPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(7, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        autoDelay1DayCB = new JCheckBox();
        autoDelay1DayCB.setSelected(false);
        autoDelay1DayCB.setText("Auto Delay 7 day");
        rightPanel.add(autoDelay1DayCB, new com.intellij.uiDesigner.core.GridConstraints(6, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkB = new JButton();
        checkB.setActionCommand("Check");
        checkB.setLabel("Check");
        checkB.setText("Check");
        rightPanel.add(checkB, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveB = new JButton();
        saveB.setText("Save");
        rightPanel.add(saveB, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteB = new JButton();
        deleteB.setText("Delete");
        rightPanel.add(deleteB, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
