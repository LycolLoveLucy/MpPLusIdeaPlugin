package com.personal.lycol.plugin.gen.dialog;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.intellij.openapi.ui.Messages;
import com.personal.lycol.plugin.gen.CodeGeneration;
import com.personal.lycol.plugin.gen.MybatisPlusDbProperties;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
    private JButton btn_choose_file;

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
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        list_table_views_btn.addActionListener(e -> {
            setJdbcDriver(txt_driver_url.getText());
           DefaultTableModel defaultTableModel= (DefaultTableModel) data_table.getModel();
            try {
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

        txt_driver_url.addInputMethodListener(new InputMethodListener() {
            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {
                System.out.println(event.getText());
            }

            @Override
            public void caretPositionChanged(InputMethodEvent event) {

            }
        });
        txt_driver_url.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JTextField source= (JTextField) e.getSource();
                System.out.println(source.getText());
                super.mouseReleased(e);

            }
        });
        final JFileChooser fileDialog = new JFileChooser();
        fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);


        btn_choose_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileDialog.showOpenDialog(contentPane);
                System.out.println(returnVal);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileDialog.getSelectedFile();
                    txt_out_dir.setText(file.getAbsolutePath());

                } else {
                }
            }
        });
    }

    private AutoGenerator autoGenerator() throws SQLException {
        MybatisPlusDbProperties mybatisPlusDbProperties=new MybatisPlusDbProperties();
        mybatisPlusDbProperties.setDriverName(driverNameLocal.get().replaceAll("%20",""));
        //jdbc:mysql://localhost:3336/TD_OA?nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=GMT%2B8
        mybatisPlusDbProperties.setJdbcUrl(txt_driver_url.getText().trim());
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
            try {
                setJdbcDriver(txt_driver_url.getText());
                autoGenerator().execute();

            } catch (SQLException e) {
                Messages.showErrorDialog("错误",e.getMessage());
            }
            dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    ThreadLocal<String> driverNameLocal=new ThreadLocal<>();
    private void setJdbcDriver(String jdbcUrl){
        if(jdbcUrl.contains("mysql")){
            driverNameLocal.set("com.mysql.cj.jdbc.Driver");
        }
        else   if(jdbcUrl.contains("oracle")){
            driverNameLocal.set("oracle.jdbc.driver.OracleDriver");
        }

    }

    public static void main(String[] args) {
        CodeDialog dialog = new CodeDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
