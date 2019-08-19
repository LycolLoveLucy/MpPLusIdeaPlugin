package com.personal.lycol.plugin.gen;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CodeDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txt_userName;
    private JTextField txt_pwd;
    private JTextField txt_out_dir;
    private JTextField textField4;
    private JTextField txt_driver_url;
    private JTabbedPane tabbedPane1;
    private JTable data_table;
    private JButton list_table_views_btn;
    private JButton choose_dir_btn;

    private void setModel(){


        DefaultTableModel dtm = new DefaultTableModel(new Object[] { "是否选中", "表名"}, 0);
        dtm
                .addRow(new Object[] { new Boolean(true),"" });

        data_table.setModel(dtm);
        TableColumnModel tcm = data_table.getColumnModel();

        tcm.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        tcm.getColumn(0).setCellRenderer(new MyTableRenderer());

        tcm.getColumn(0).setPreferredWidth(20);
        tcm.getColumn(0).setWidth(20);
        tcm.getColumn(0).setMaxWidth(20);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    class MyTableRenderer extends JCheckBox implements TableCellRenderer {
        // 此方法可以查考JDK文档的说明
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Boolean b = (Boolean) value;
            this.setSelected(b.booleanValue());
            return this;
        }
    }
    public CodeDialog() {
        setContentPane(contentPane);
        setModal(true);

        getRootPane().setDefaultButton(buttonOK);

        setModel();
        buttonOK.addActionListener(e -> onOK());



        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        list_table_views_btn.addActionListener(e -> {
           DefaultTableModel defaultTableModel= (DefaultTableModel) data_table.getModel();
            try {

                setDriverName();
                Connection conn=autoGenerator().getDataSource().getConn();
                DatabaseMetaData databaseMetaData=conn.getMetaData();
                ResultSet rs = databaseMetaData.getTables(conn.getCatalog(), txt_userName.getText(), "%", new String[]{"TABLE"});                   while (rs.next()){
                    defaultTableModel.addRow(new Object[]{new Boolean(true),rs.getString("TABLE_NAME")});
                }
                data_table.setModel(defaultTableModel);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
       final   JFileChooser jFileChooser=new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        choose_dir_btn.addActionListener(e -> {
            int returnval=jFileChooser.showDialog(contentPane, "选择文件夹");
            if(returnval==JFileChooser.APPROVE_OPTION)
            {
                String str=jFileChooser.getSelectedFile().getPath();
                txt_out_dir.setText(str);
            }        });
        buttonCancel.addActionListener(e -> {

            dispose();
        });
    }

    private AutoGenerator autoGenerator() throws SQLException {
        MybatisPlusDbProperties mybatisPlusDbProperties=new MybatisPlusDbProperties();
        mybatisPlusDbProperties.setDriverName(driverThreadLocal.get());
        //jdbc:mysql://localhost:3336/TD_OA?nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=GMT%2B8
        mybatisPlusDbProperties.setJdbcUrl(txt_driver_url.getText());
        mybatisPlusDbProperties.setOutDir(txt_out_dir.getText());
        mybatisPlusDbProperties.setEnable(true);
        mybatisPlusDbProperties.setSuperEntityClass("com.test");
        if(StringUtils.isNotBlank(textField4.getText())){

       mybatisPlusDbProperties.setTableList(Arrays.asList(textField4.getText().split(",")));
        }
        mybatisPlusDbProperties.setUserName(txt_userName.getText());
        mybatisPlusDbProperties.setPassword(txt_pwd.getText());
        return    new CodeGeneration(mybatisPlusDbProperties).genAutoGenerator();
    }
    private void onOK() {
        if(!checkInput()){
            return;
        }
            try {
                setDriverName();
                autoGenerator().execute();
            } catch (SQLException e) {
                Messages.showErrorDialog("错误",e.getMessage());
            }
            dispose();
    }

    private boolean checkInput(){
        if(StringUtils.isEmpty(txt_userName.getText())){
            JOptionPane.showMessageDialog(null, "用户名不能为空", "校验", JOptionPane.ERROR_MESSAGE);           // Messages.showErrorDialog(this.buttonOK,"","");
            return false;
        }
        if(StringUtils.isEmpty(txt_pwd.getText())){
            JOptionPane.showMessageDialog(null, "密码不能为空", "校验", JOptionPane.ERROR_MESSAGE);           // Messages.showErrorDialog(this.buttonOK,"","");
            return false;
        }
        if(StringUtils.isEmpty(txt_driver_url.getText())){
            JOptionPane.showMessageDialog(null, "URL路径不能为空", "校验", JOptionPane.ERROR_MESSAGE);           // Messages.showErrorDialog(this.buttonOK,"","");
            return false;
        }
        return  true;
    }

    ThreadLocal<String> driverThreadLocal=new ThreadLocal();
    private void setDriverName() {
        String jdbcUrl = txt_driver_url.getText();
        if (jdbcUrl.startsWith("jdbc:mysql:") && !jdbcUrl.startsWith("jdbc:cobar:") && !jdbcUrl.startsWith("jdbc:log4jdbc:mysql:")) {

            driverThreadLocal.set("com.mysql.cj.jdbc.Driver");
            return;
        }

        driverThreadLocal.set("oracle.jdbc.driver.OracleDriver");
    }


        private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        CodeDialog dialog = new CodeDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
