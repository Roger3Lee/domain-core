package com.artframework.domain.plugin.ui;

import com.artframework.domain.datasource.TableQuery;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.querys.PostgreSqlQuery;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.keywords.PostgreSqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.query.SQLQuery;
import com.intellij.notification.Notification;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.jetbrains.qodana.sarif.model.Message;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomainGeneratorDialog extends JDialog {
    private static final String MY_SQL = "Mysql";
    private static final String POLAR_DB = "Polar DB";
    private static final String PG = "Postgresql";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField db_url;
    private JTable db_tables;
    private JTextField db_user;
    private JPasswordField db_password;
    private JButton btn_loadTables;
    private JComboBox db_type;

    public DomainGeneratorDialog() {
        this.setTitle("DDD代碼生成器");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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

        btn_loadTables.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTables();
            }
        });

        db_type.addItem(MY_SQL);
        db_type.addItem(POLAR_DB);
        db_type.addItem(PG);
        db_type.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    onDbTypeChange(db_type.getSelectedItem().toString());
                }
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

        //
        db_tables.setModel(new DBTableModel(new ArrayList<>()));
    }

    private void onDbTypeChange(String selectedItem) {
    }

    private void loadTables() {
        try{
            DataSourceConfig.Builder builder= new DataSourceConfig
                    .Builder(db_url.getText(),db_user.getText() ,Arrays.toString(db_password.getPassword()));
            if (db_type.getSelectedItem().equals(MY_SQL)) {
                builder.dbQuery(new MySqlQuery())
                        .typeConvert(new MySqlTypeConvert())
                        .keyWordsHandler(new MySqlKeyWordsHandler())
                        .databaseQueryClass(SQLQuery.class);
            } else if (db_type.getSelectedItem().equals(POLAR_DB) || db_type.getSelectedItem().equals(PG)) {
                builder.dbQuery(new PostgreSqlQuery())
                        .schema("domain")
                        .typeConvert(new PostgreSqlTypeConvert())
                        .keyWordsHandler(new PostgreSqlKeyWordsHandler())
                        .databaseQueryClass(SQLQuery.class);
            }

            builder.dbQuery(new MySqlQuery()).typeConvert(new MySqlTypeConvert()).keyWordsHandler(new MySqlKeyWordsHandler()).databaseQueryClass(SQLQuery.class);
            DataSourceConfig dataSourceConfig = builder.build();
            TableQuery tableQuery = new TableQuery(dataSourceConfig);
            db_tables.setModel(new DBTableModel(tableQuery.queryTables()));
        }catch (Exception ex){
            Messages.showErrorDialog(ex.getMessage(),"錯誤");
        }
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        DomainGeneratorDialog dialog = new DomainGeneratorDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    class DBTableModel extends AbstractTableModel {

        String[] n = { "表名", "表描述" };

        Object[][] p = {};
       public DBTableModel(List<TableInfo> tableInfoList){

        }

        @Override
        public int getRowCount() {
            return p.length;
        }

        @Override
        public int getColumnCount() {
            return n.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return p[rowIndex][columnIndex];
        }

        @Override
        public String getColumnName(int column) {
            return n[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            p[rowIndex][columnIndex] = aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
}
